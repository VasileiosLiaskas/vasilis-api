package vasilis.vasilis.invoice;

import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vasilis.vasilis.business.Business;
import vasilis.vasilis.business.BusinessService;
import vasilis.vasilis.invoice.DTO.InvoiceArgsDTO;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;

import java.io.IOException;
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
    public Invoice saveInvoice(MultipartFile file, String invoiceNumber, String description, Integer businessId) throws IOException {

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setDescription(description);
        invoice.setDate(new Date());
//        invoice.setInvoiceDate();
        if (businessId!=null) {
            Optional<Business> business = businessService.findById(businessId);
            business.ifPresent(invoice::setBusiness);
        }
        invoice.setFileData(file.getBytes());
        invoice.setFileName(file.getOriginalFilename());
        invoice.setFileType(file.getContentType());
        invoice.setInvoiceType(InvoiceEnum.FEE_INVOICE);
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
}
