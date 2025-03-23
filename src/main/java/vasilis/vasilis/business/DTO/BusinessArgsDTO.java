package vasilis.vasilis.business.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BusinessArgsDTO {

    private String keyword;
    private Date dateFrom;
    private Date dateTo;

}
