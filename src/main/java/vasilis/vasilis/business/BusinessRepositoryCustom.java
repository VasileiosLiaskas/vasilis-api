package vasilis.vasilis.business;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import vasilis.vasilis.business.DTO.BusinessArgsDTO;
import vasilis.vasilis.business.DTO.BusinessDTO;

import java.util.Date;
import java.util.List;

@Repository
public interface BusinessRepositoryCustom {

//    List<BusinessDTO> searchBusiness(BusinessArgsDTO businessArgsDTO);
    Page<BusinessDTO> searchBusinessByKeyword(int page, int size, String keyword, Date dateFrom, Date dateTo);
}
