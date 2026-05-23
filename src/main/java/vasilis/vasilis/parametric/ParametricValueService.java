package vasilis.vasilis.parametric;

import org.springframework.http.ResponseEntity;
import vasilis.vasilis.parametric.DTO.ParametricValueDTO;

import java.util.List;

public interface ParametricValueService {

    List<ParametricValueDTO> getList(String type);

    ParametricValueDTO save(ParametricValueDTO request);

    ParametricValueDTO update(Long id, ParametricValueDTO request);

    ResponseEntity<Void> delete(Long id);

    List<ParametricValueDTO> replaceFromTextarea(String type, String valuesText);

    String getTextareaValues(String type);
}

