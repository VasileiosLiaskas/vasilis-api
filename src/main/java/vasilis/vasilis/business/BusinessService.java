package vasilis.vasilis.business;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vasilis.vasilis.business.DTO.BusinessDTO;

import java.util.List;
import java.util.Optional;

@Service
public interface BusinessService {

    Optional<Business> findById(Integer id);

    Page<BusinessDTO> getList(int page, int size, String keyword, String dateFrom, String dateTo,  Boolean filterFilesDelivered, Boolean filterFilesCompleted, Boolean filterPayout);

    boolean deleteBusinessById(Integer id);

    BusinessDTO saveBusiness(BusinessDTO businessDTO);

    Business toEntity(BusinessDTO businessDTO);

    BusinessDTO toDTO(Business business);

    List<BusinessDTO> toDTOList(List<Business> businessList);

    List<Business> toEntityList(List<BusinessDTO> businessDTOList);

    ResponseEntity<byte[]> export();

    Business getById(Integer businessId);

    boolean updateGoogleCalendarId(Integer id, String calendarId);

//    List<BusinessDTO> searchBusiness(BusinessArgsDTO businessArgsDTO);
}
