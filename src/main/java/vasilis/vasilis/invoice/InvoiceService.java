package vasilis.vasilis.invoice;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import vasilis.vasilis.invoice.DTO.InvoiceArgsDTO;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    InvoiceDTO toDTO(Invoice invoice);
    Invoice toEntity(InvoiceDTO invoiceDTO);
    Optional<Invoice> findById(Integer id);
    Invoice saveInvoice(MultipartFile file, String invoiceNumber, String description, Integer businessId, Date invoiceDate) throws IOException;

    List<Invoice> findInvoicesByArgs(InvoiceArgsDTO args);

    List<InvoiceDTO> findInvoicesDTOByArgs(InvoiceArgsDTO args);

    ResponseEntity<byte[]> downloadInvoiceById(Integer id);

    Page<InvoiceDTO> getList(int page, int size, String keyword, String dateFrom, String dateTo);

    ResponseEntity<Boolean> deleteById(Integer id);

    void updateInvoice(Integer id, String fileName, String invoiceNumber, String description, Date invoiceDate) throws Exception;
}
