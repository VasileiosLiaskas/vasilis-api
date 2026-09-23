package vasilis.vasilis.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vasilis.vasilis.todo.DTO.TodoDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository repository;

    @Override
    public List<TodoDTO> getList() {
        return repository.findAllByOrderByIdAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TodoDTO save(TodoDTO request) {
        normalizeAndValidate(request);

        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        return toDTO(repository.save(todo));
    }

    @Override
    public TodoDTO update(Long id, TodoDTO request) {
        normalizeAndValidate(request);

        Todo todo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        return toDTO(repository.save(todo));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private TodoDTO toDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        return dto;
    }

    private void normalizeAndValidate(TodoDTO request) {
        if (request == null) {
            throw new RuntimeException("Request body is required");
        }
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Title is required");
        }

        request.setTitle(request.getTitle().trim());
        if (request.getDescription() != null) {
            request.setDescription(request.getDescription().trim());
        }
    }
}
