package vasilis.vasilis.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vasilis.vasilis.comment.DTO.CommentDTO;
import vasilis.vasilis.comment.DTO.CommentRequestDTO;
import vasilis.vasilis.security.CustomUserDetails;
import vasilis.vasilis.user.User;
import vasilis.vasilis.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Override
    public CommentDTO toDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());

        if (comment.getAuthor() != null) {
            commentDTO.setAuthor(comment.getAuthor().getUsername());
        }

        return commentDTO;
    }

    @Override
    public Comment toEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setText(commentDTO.getText());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setUpdatedAt(commentDTO.getUpdatedAt());
        return comment;
    }

    @Override
    public List<CommentDTO> getList() {
        List<Comment> comments = commentRepository.findAllByOrderByCreatedAtDesc();
        return comments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO saveComment(CommentRequestDTO request) {
        Comment comment = new Comment();
        comment.setText(request.getText());

        // Get current authenticated user
        User currentUser = getCurrentUser();
        comment.setAuthor(currentUser);

        Comment savedComment = commentRepository.save(comment);
        return toDTO(savedComment);
    }

    @Override
    public CommentDTO updateComment(Integer id, CommentRequestDTO request) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setText(request.getText());
            Comment updatedComment = commentRepository.save(comment);
            return toDTO(updatedComment);
        }
        throw new RuntimeException("Comment not found with id: " + id);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Integer id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            commentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            Optional<User> user = userService.findByUsername(username);
            return user.orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("No authenticated user found");
    }
}
