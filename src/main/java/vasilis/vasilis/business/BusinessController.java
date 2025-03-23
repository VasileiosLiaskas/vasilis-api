package vasilis.vasilis.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasilis.vasilis.business.DTO.BusinessArgsDTO;
import vasilis.vasilis.business.DTO.BusinessDTO;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/business")
@CrossOrigin(origins="*")
public class BusinessController {

    @Autowired
    private BusinessService businessService;


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveBusiness(@RequestBody BusinessDTO businessDTO) {
        businessService.saveBusiness(businessDTO);

    }
//
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public List<BusinessDTO> getList() {
//        return businessService.getList();
//    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Page<BusinessDTO> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {
        return businessService.getList(page, size,keyword, dateFrom, dateTo);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteBusiness(@PathVariable Integer id) {
        boolean deleted = businessService.deleteBusinessById(id);
        if (deleted) {
            return ResponseEntity.ok("Business deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business not found.");
        }
    }

//    @GetMapping(value = "search-for-args")
//    public List<BusinessDTO> searchBusiness(@RequestBody BusinessArgsDTO businessArgsDTO){
//        return businessService.searchBusiness(businessArgsDTO);
//    }

}
