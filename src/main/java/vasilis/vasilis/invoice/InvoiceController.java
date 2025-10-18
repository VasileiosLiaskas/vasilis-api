package vasilis.vasilis.invoice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vasilis.vasilis.business.DTO.BusinessDTO;
import vasilis.vasilis.invoice.DTO.InvoiceArgsDTO;
import vasilis.vasilis.invoice.DTO.InvoiceDTO;

import java.util.Date;
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
            @RequestParam(value = "businessId",required = false) Integer businessId,
            @RequestParam("invoiceType") InvoiceEnum invoiceType,
            @RequestParam("invoiceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date invoiceDate
    ) {
        try {

            invoiceService.saveInvoice(file, invoiceNumber, description, businessId, invoiceDate,invoiceType);
            return ResponseEntity.ok("Invoice saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving invoice: " + e.getMessage());
        }
    }

    @GetMapping("/find")
    public List<InvoiceDTO> findInvoice(@ModelAttribute InvoiceArgsDTO args) {

        return invoiceService.findInvoicesDTOByArgs(args);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Integer id) {
        return invoiceService.downloadInvoiceById(id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Page<InvoiceDTO> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(required = false) Integer businessId
    ) {
        return invoiceService.getList(page, size, keyword, dateFrom, dateTo, businessId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteInvoice(@PathVariable Integer id) {
        return invoiceService.deleteById(id);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editInvoice(
            @PathVariable Integer id,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "invoiceNumber", required = false) String invoiceNumber,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "invoiceType", required = false) InvoiceEnum invoiceType,
            @RequestParam("invoiceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date invoiceDate
    ) {
        try {
            // Call your service to update the invoice
            invoiceService.updateInvoice(id, fileName, invoiceNumber, description,invoiceDate, invoiceType);
            return ResponseEntity.ok("Invoice updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating invoice: " + e.getMessage());
        }
    }
}
