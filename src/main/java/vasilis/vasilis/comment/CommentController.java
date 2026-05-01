package vasilis.vasilis.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasilis.vasilis.comment.DTO.CommentDTO;
import vasilis.vasilis.comment.DTO.CommentRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins="*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public List<CommentDTO> getComments() {
        return commentService.getList();
    }

    @PostMapping("/save")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentRequestDTO request) {
        try {
            CommentDTO savedComment = commentService.saveComment(request);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Integer id,
            @RequestBody CommentRequestDTO request) {
        try {
            CommentDTO updatedComment = commentService.updateComment(id, request);
            return ResponseEntity.ok(updatedComment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        return commentService.deleteComment(id);
    }
}
