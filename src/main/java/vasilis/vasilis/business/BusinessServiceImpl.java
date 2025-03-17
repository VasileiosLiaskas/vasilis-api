package vasilis.vasilis.business;

import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vasilis.vasilis.business.DTO.BusinessDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BusinessServiceImpl implements  BusinessService {

    @Autowired
    private BusinessRepository businessRepository;


    public Page<BusinessDTO> getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "date"));
        Page<Business> businessPage = businessRepository.findAll(pageable);
        return businessPage.map(this::toDTO);
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
}
