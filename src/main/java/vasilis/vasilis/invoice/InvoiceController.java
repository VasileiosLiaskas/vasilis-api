package vasilis.vasilis.invoice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vasilis.vasilis.invoice.DTO.InvoiceArgsDTO;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;

import java.util.List;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(origins="*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/save")
    public ResponseEntity<String> saveInvoice(
            @RequestParam("file") MultipartFile file,
            @RequestParam("invoiceNumber") String invoiceNumber,
            @RequestParam("description") String description,
            @RequestParam("businessId") Integer businessId
//            @RequestParam("invoiceDate") String invoiceDate
    ) {
        try {
            invoiceService.saveInvoice(file, invoiceNumber, description, businessId);
            return ResponseEntity.ok("Invoice saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving invoice: " + e.getMessage());
        }
    }

    @GetMapping("/find")
    public List<InvoiceDTO> findInvoice(@ModelAttribute InvoiceArgsDTO args) {
        return invoiceService.findInvoicesByArgs(args);
    }

}
