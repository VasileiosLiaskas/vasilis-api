package vasilis.vasilis.invoice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vasilis.vasilis.invoice.DTO.InvoiceArgsDTO;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    InvoiceDTO toDTO(Invoice invoice);
    Invoice toEntity(InvoiceDTO invoiceDTO);
    Optional<Invoice> findById(Integer id);
    Invoice saveInvoice(MultipartFile file, String invoiceNumber, String description, Integer businessId) throws IOException;

    List<Invoice> findInvoicesByArgs(InvoiceArgsDTO args);

    List<InvoiceDTO> findInvoicesDTOByArgs(InvoiceArgsDTO args);

    ResponseEntity<byte[]> downloadInvoiceById(Integer id);
}
