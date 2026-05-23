package vasilis.vasilis.parametric;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vasilis.vasilis.parametric.DTO.ParametricValueDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParametricValueServiceImpl implements ParametricValueService {

    @Autowired
    private ParametricValueRepository repository;

    @Override
    public List<ParametricValueDTO> getList(String type) {
        List<ParametricValue> values;
        if (type == null || type.trim().isEmpty()) {
            values = repository.findAllByOrderByTypeAscDescriptionAsc();
        } else {
            values = repository.findByTypeOrderByDescriptionAsc(type.trim());
        }
        return values.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ParametricValueDTO save(ParametricValueDTO request) {
        validateRequest(request);
        ParametricValue entity = new ParametricValue();
        BeanUtils.copyProperties(request, entity);
        return toDTO(repository.save(entity));
    }

    @Override
    public ParametricValueDTO update(Long id, ParametricValueDTO request) {
        validateRequest(request);
        ParametricValue existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parametric value not found with id: " + id));

        existing.setDescription(request.getDescription().trim());
        existing.setType(request.getType().trim());
        return toDTO(repository.save(existing));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @Transactional
    public List<ParametricValueDTO> replaceFromTextarea(String type, String valuesText) {
        String normalizedType = normalizeType(type);

        repository.deleteByType(normalizedType);

        List<String> lines = splitLines(valuesText);
        if (lines.isEmpty()) {
            return Collections.emptyList();
        }

        List<ParametricValue> entities = lines.stream()
                .map(line -> {
                    ParametricValue value = new ParametricValue();
                    value.setType(normalizedType);
                    value.setDescription(line);
                    return value;
                })
                .collect(Collectors.toList());

        repository.saveAll(entities);

        return repository.findByTypeOrderByDescriptionAsc(normalizedType)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String getTextareaValues(String type) {
        String normalizedType = normalizeType(type);
        return repository.findByTypeOrderByDescriptionAsc(normalizedType)
                .stream()
                .map(ParametricValue::getDescription)
                .collect(Collectors.joining("\n"));
    }

    private ParametricValueDTO toDTO(ParametricValue entity) {
        ParametricValueDTO dto = new ParametricValueDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private void validateRequest(ParametricValueDTO request) {
        if (request == null) {
            throw new RuntimeException("Request body is required");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new RuntimeException("Description is required");
        }
        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new RuntimeException("Type is required");
        }
        request.setDescription(request.getDescription().trim());
        request.setType(request.getType().trim());
    }

    private String normalizeType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new RuntimeException("Type is required");
        }
        return type.trim();
    }

    private List<String> splitLines(String valuesText) {
        if (valuesText == null || valuesText.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(valuesText.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
}

