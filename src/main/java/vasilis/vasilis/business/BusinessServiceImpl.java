package vasilis.vasilis.business;

import jakarta.transaction.Transactional;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vasilis.vasilis.business.DTO.BusinessDTO;
import vasilis.vasilis.general.TranslationUtil;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;
import vasilis.vasilis.invoice.Invoice;
import vasilis.vasilis.invoice.InvoiceService;
import vasilis.vasilis.invoice.InvoiceServiceImpl;

import java.time.ZoneId;
import java.util.Date;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessServiceImpl implements  BusinessService {

    @Autowired
    private BusinessRepository businessRepository;
    @Autowired
    private BusinessRepositoryCustom businessRepositoryCustom;
    @Autowired
    @Lazy
    private InvoiceService invoiceService;


    @Override
    public Optional<Business> findById(Integer id) {
        return businessRepository.findById(id);
    }

    @Override
    public List<BusinessDTO> getList() {
        return toDTOList(businessRepository.findAll(Sort.by(Sort.Direction.DESC, "date")));
    }

    @Override
    public boolean deleteBusinessById(Integer id) {
        if (businessRepository.existsById(id)) {
            businessRepository.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    @Transactional
    public BusinessDTO saveBusiness(BusinessDTO businessDTO) {
        return toDTO(businessRepository.save(toEntity(businessDTO)));
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
        if (business.getInvoices() != null) {
            List<Integer> invoiceIds = business.getInvoices().stream()
                    .map(Invoice::getId) // extract ID only
                    .collect(Collectors.toList());
            businessDTO.setInvoicesId(invoiceIds);
        }

        return businessDTO;
    }
    private InvoiceDTO convertInvoiceToDTO(Invoice invoice) {

        return invoiceService.toDTO(invoice);
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
    private Page<BusinessDTO> searchBusinessByKeyword(int page, int size, String keyword, Date dateFrom, Date dateTo,  Boolean filterFilesDelivered, Boolean filterFilesCompleted, Boolean filterPayout) {
        return businessRepositoryCustom.searchBusinessByKeyword(page, size, keyword, dateFrom, dateTo,filterFilesDelivered, filterFilesCompleted, filterPayout);
    }

    @Override
    public ResponseEntity<byte[]> export() {
        List<BusinessDTO> businessList = toDTOList(businessRepository.findAll()); // Assuming it fetches all data
        Map<String, List<BusinessDTO>> groupedByMonth = businessList.stream()
                .collect(Collectors.groupingBy(b -> getMonthYear(b.getDate())));

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            for (String month : groupedByMonth.keySet()) {
                Sheet sheet = workbook.createSheet(month);
                createHeaderRow(sheet);
                fillDataRows(sheet, groupedByMonth.get(month));
            }

            workbook.write(out);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Business_Report.xlsx");
            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public Optional<Business> getById(Integer businessId) {
        return businessRepository.findById(businessId);
    }

    private String getMonthYear(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonth().toString() + "_" + localDate.getYear();
    }

    private void createHeaderRow(Sheet sheet) {
        Row header = sheet.createRow(0);
        String[] columns = {"ID", "Date", "Type", "Who", "Area", "Details", "Fee", "Advance Payment", "Remaining Money", "Costs", "Payout", "Files Completed", "Files Delivered", "Comments"};
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(TranslationUtil.getTranslation(columns[i]));
        }
    }

    private void fillDataRows(Sheet sheet, List<BusinessDTO> data) {
        int rowNum = 1;
        for (BusinessDTO business : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(business.getId() != null ? business.getId() : 0);
            row.createCell(1).setCellValue(business.getDate() != null ? business.getDate().toString() : "");
            row.createCell(2).setCellValue(business.getType() != null ? business.getType() : "");
            row.createCell(3).setCellValue(business.getWho() != null ? business.getWho() : "");
            row.createCell(4).setCellValue(business.getArea() != null ? business.getArea() : "");
            row.createCell(5).setCellValue(business.getDetails() != null ? business.getDetails() : "");
            row.createCell(6).setCellValue(business.getFee() != null ? business.getFee() : 0.0);
            row.createCell(7).setCellValue(business.getAdvancePayment() != null ? business.getAdvancePayment() : 0.0);
            row.createCell(8).setCellValue(business.getRemainingMoney() != null ? business.getRemainingMoney() : 0.0);
            row.createCell(9).setCellValue(business.getCosts() != null ? business.getCosts() : 0.0);
            row.createCell(10).setCellValue(business.getPayout() != null ? business.getPayout() : false);
            row.createCell(11).setCellValue(business.getFilesCompleted() != null ? business.getFilesCompleted() : false);
            row.createCell(12).setCellValue(business.getFilesDelivered() != null ? business.getFilesDelivered() : false);
            row.createCell(13).setCellValue(business.getComments() != null ? business.getComments() : "");
        }
    }

    @Override
    public boolean updateGoogleCalendarId(Integer id, String calendarId) {
        return businessRepository.findById(id).map(business -> {
            business.setGoogleCalendarId(calendarId);
            businessRepository.save(business);
            return true;
        }).orElse(false);
    }
}
