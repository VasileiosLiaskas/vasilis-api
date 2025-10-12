package vasilis.vasilis.invoice;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vasilis.vasilis.business.Business;
import vasilis.vasilis.business.BusinessService;
import vasilis.vasilis.invoice.DTO.InvoiceArgsDTO;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {


    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    @Lazy
    private BusinessService businessService;


    @Override
    public InvoiceDTO toDTO(Invoice invoice) {

        if (invoice==null) return null;
        InvoiceDTO dto = new InvoiceDTO();
        BeanUtils.copyProperties(invoice, dto);
        if (invoice.getBusiness()!=null){
            dto.setBusinessId(invoice.getBusiness().getId());
        }
        return dto;

    }

    @Override
    public Invoice toEntity(InvoiceDTO invoiceDTO) {
        if (invoiceDTO == null) return null;
        Invoice invoice = new Invoice();

        BeanUtils.copyProperties(invoiceDTO, invoice);
        if (invoiceDTO.getBusinessId() != null) {
            Business business = businessService.getById(invoiceDTO.getBusinessId());
            invoice.setBusiness(business);

        }
        return invoice;
    }

    @Override
    public Optional<Invoice> findById(Integer id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Invoice saveInvoice(MultipartFile file, String invoiceNumber, String description, Integer businessId, Date invoiceDate, InvoiceEnum invoiceType) throws IOException {

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setDescription(description);
        invoice.setDate(new Date());
        invoice.setInvoiceDate(invoiceDate);
        if (businessId!=null) {
            Optional<Business> business = businessService.findById(businessId);
            business.ifPresent(invoice::setBusiness);
        }
        invoice.setFileData(file.getBytes());
        invoice.setFileName(file.getOriginalFilename());
        invoice.setFileType(file.getContentType());
        invoice.setInvoiceType(invoiceType);
        return invoiceRepository.save(invoice);
    }

    @Override
    public List<Invoice> findInvoicesByArgs(InvoiceArgsDTO args) {
        return invoiceRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (args.getInvoiceNumber() != null) {
                predicates.add(cb.equal(root.get("invoiceNumber"), args.getInvoiceNumber()));
            }
            if (args.getBusinessId() != null) {
                predicates.add(cb.equal(root.get("business").get("id"), args.getBusinessId()));
            }
            if (args.getInvoiceDate() != null) {
                predicates.add(cb.equal(root.get("invoiceDate"), args.getInvoiceDate()));
            }
            if (args.getDateCreated() != null) {
                predicates.add(cb.equal(root.get("dateCreated"), args.getDateCreated()));
            }
            System.out.println("Invoice number: " + args.getInvoiceNumber());
            System.out.println("Business ID: " + args.getBusinessId());
            System.out.println("Invoice date: " + args.getInvoiceDate());
            System.out.println("Date created: " + args.getDateCreated());

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public List<InvoiceDTO> findInvoicesDTOByArgs(InvoiceArgsDTO args) {
        List<Invoice> invoices = findInvoicesByArgs(args);
        List<InvoiceDTO> dtoList = new ArrayList<>();
        for (Invoice invoice : invoices) {
            dtoList.add(toDTO(invoice));
        }
        return dtoList;
    }

    @Override
    public ResponseEntity<Boolean> deleteById(Integer id) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isPresent()) {
            invoiceRepository.deleteById(id);
            return ResponseEntity.ok(true); // Successfully deleted
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Not found
        }
    }

    @Override
    public void updateInvoice(Integer id, String fileName, String invoiceNumber, String description, Date invoiceDate) throws Exception {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new Exception("Invoice not found"));

        if (invoiceNumber!=null) {
            invoice.setInvoiceNumber(invoiceNumber);
        }
        if (description!=null) {
            invoice.setDescription(description);
        }
        if (invoiceDate!=null){
            invoice.setInvoiceDate(invoiceDate);
        }

        if (fileName!=null) {
        invoice.setFileName(fileName);
        }
        this.invoiceRepository.save(invoice);
    }

    @Override
    public Page<InvoiceDTO> getList(int page, int size, String keyword, String dateFrom, String dateTo, Integer businessId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("invoiceDate").descending());

        Page<Invoice> invoices = invoiceRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // keyword search across multiple fields
            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                Predicate fileNamePredicate = cb.like(cb.lower(root.get("fileName")), likePattern);
                Predicate invoiceNumberPredicate = cb.like(cb.lower(root.get("invoiceNumber")), likePattern);
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), likePattern);

                predicates.add(cb.or(fileNamePredicate, invoiceNumberPredicate, descriptionPredicate));
            }

            // dateFrom (inclusive)
            if (dateFrom != null) {
                LocalDate fromDate = LocalDate.parse(dateFrom);
                Date from = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                predicates.add(cb.greaterThanOrEqualTo(root.get("invoiceDate"), from));
            }

            if (dateTo != null) {
                LocalDate toDate = LocalDate.parse(dateTo);
                Date to = Date.from(toDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
                predicates.add(cb.lessThanOrEqualTo(root.get("invoiceDate"), to));
            }

            if (businessId!=null) {
                predicates.add(cb.equal(root.get("business").get("id"), businessId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        // Map to DTO
        return invoices.map(this::toDTO);
    }

    @Override
    public ResponseEntity<byte[]> downloadInvoiceById(Integer id) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);

        if (invoiceOpt.isEmpty() || invoiceOpt.get().getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        Invoice invoice = invoiceOpt.get();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + invoice.getFileName() + "\"")
                .header("Content-Type", invoice.getFileType())
                .body(invoice.getFileData());
    }
}
