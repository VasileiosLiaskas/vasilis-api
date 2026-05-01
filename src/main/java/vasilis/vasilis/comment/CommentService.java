package vasilis.vasilis.comment;

import org.springframework.http.ResponseEntity;
import vasilis.vasilis.comment.DTO.CommentDTO;
import vasilis.vasilis.comment.DTO.CommentRequestDTO;

import java.util.List;

public interface CommentService {

    CommentDTO toDTO(Comment comment);
    Comment toEntity(CommentDTO commentDTO);

    List<CommentDTO> getList();
    CommentDTO saveComment(CommentRequestDTO request);
    CommentDTO updateComment(Integer id, CommentRequestDTO request);
    ResponseEntity<Void> deleteComment(Integer id);
}
