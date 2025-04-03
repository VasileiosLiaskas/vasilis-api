package vasilis.vasilis.business;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vasilis.vasilis.business.DTO.BusinessArgsDTO;
import vasilis.vasilis.business.DTO.BusinessDTO;

import java.util.Date;
import java.util.List;

@Service
public interface BusinessService {

    Page<BusinessDTO> getList(int page, int size, String keyword, String dateFrom, String dateTo);

    boolean deleteBusinessById(Integer id);

    void saveBusiness(BusinessDTO businessDTO);

    Business toEntity(BusinessDTO businessDTO);

    BusinessDTO toDTO(Business business);

    List<BusinessDTO> toDTOList(List<Business> businessList);

    List<Business> toEntityList(List<BusinessDTO> businessDTOList);

    ResponseEntity<byte[]> export();

//    List<BusinessDTO> searchBusiness(BusinessArgsDTO businessArgsDTO);
}
