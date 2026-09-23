package vasilis.vasilis.todo;

import org.springframework.http.ResponseEntity;
import vasilis.vasilis.todo.DTO.TodoDTO;

import java.util.List;

public interface TodoService {

    List<TodoDTO> getList();

    TodoDTO save(TodoDTO request);

    TodoDTO update(Long id, TodoDTO request);

    ResponseEntity<Void> delete(Long id);
}
