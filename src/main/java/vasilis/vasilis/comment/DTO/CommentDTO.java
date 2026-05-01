package vasilis.vasilis.comment.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import vasilis.vasilis.general.CustomDateDeserializer;
import vasilis.vasilis.general.CustomDateSerializer;

import java.util.Date;

@Data
public class CommentDTO {

    private Integer id;
    private String text;
    private String author;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdAt;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updatedAt;
}
