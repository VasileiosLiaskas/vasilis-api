package vasilis.vasilis.business;

import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vasilis.vasilis.business.DTO.BusinessArgsDTO;
import vasilis.vasilis.business.DTO.BusinessDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.tomcat.util.http.FastHttpDateFormat.parseDate;

@Service
public class BusinessServiceImpl implements  BusinessService {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private BusinessRepositoryCustom businessRepositoryCustom;


    public Page<BusinessDTO> getList(int page, int size, String keyword, String dateFrom, String dateTo) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "date"));
        if ((keyword == null || keyword.trim().isEmpty())&&(dateFrom == null || dateTo == null )) {
            // If keyword is empty or null, use the simple findAll with pagination and sorting
            Page<Business> businessPage = businessRepository.findAll(pageable);
            Page<BusinessDTO> dtoPage = businessPage.map(this::toDTO);

            // Calculate totalIncome for current month
            Double totalIncome = getTotalIncomeForCurrentMonth();

            // Set totalIncome in each DTO
            dtoPage.forEach(dto -> dto.setTotalIncome(totalIncome));

            return dtoPage;
        } else {
            Date fromDate = parseDate(dateFrom);
            Date toDate = parseDate(dateTo);
            return searchBusinessByKeyword(page, size, keyword,fromDate, toDate);
        }
    }

    private Double getTotalIncomeForCurrentMonth() {
        return businessRepository.getTotalIncomeForCurrentMonth();
    }

    @Override
    @Transactional
    public boolean deleteBusinessById(Integer id) {
        boolean deleted = false;
        businessRepository.deleteById(id);
        // Check if the business exists first
        Optional<Business> business = businessRepository.findById(id);

        if (business.isPresent()) {
            // If the business exists, delete it
            businessRepository.deleteById(id);
            deleted = true;
        }

        return deleted;

    }

    @Override
    @Transactional
    public void saveBusiness(BusinessDTO businessDTO) {
        businessRepository.save(toEntity(businessDTO));
    }

    @Override
    public Business toEntity(BusinessDTO businessDTO) {
        if (businessDTO == null) {
            return null;
        }

        Business business = new Business();

        // Use BeanUtils to copy properties from BusinessDTO to Business
        BeanUtils.copyProperties(businessDTO, business);

        return business;
    }

    @Override
    public BusinessDTO toDTO(Business business) {
        if (business == null) {
            return null;
        }

        BusinessDTO businessDTO = new BusinessDTO();

        // Use BeanUtils to copy properties from Business to BusinessDTO
        BeanUtils.copyProperties(business, businessDTO);

        return businessDTO;
    }

    @Override
    public List<BusinessDTO> toDTOList(List<Business> businessList) {
        if (businessList == null) {
            return null;
        }

        List<BusinessDTO> businessDTOList = new ArrayList<>(businessList.size());
        for (Business business : businessList) {
            businessDTOList.add(toDTO(business)); // Copy properties from Business to BusinessDTO
        }

        return businessDTOList;
    }

    @Override
    public List<Business> toEntityList(List<BusinessDTO> businessDTOList) {
        if (businessDTOList == null) {
            return null;
        }

        List<Business> businessList = new ArrayList<>(businessDTOList.size());
        for (BusinessDTO businessDTO : businessDTOList) {
            businessList.add(toEntity(businessDTO)); // Copy properties from BusinessDTO to Business
        }

        return businessList;
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null; // Return null if date is missing
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: YYYY-MM-DD", e);
        }
    }
    private Page<BusinessDTO> searchBusinessByKeyword(int page, int size, String keyword, Date dateFrom, Date dateTo) {
        return businessRepositoryCustom.searchBusinessByKeyword(page, size, keyword, dateFrom, dateTo);
    }
}
