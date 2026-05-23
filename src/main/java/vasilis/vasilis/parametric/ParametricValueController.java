package vasilis.vasilis.parametric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasilis.vasilis.parametric.DTO.ParametricValueDTO;
import vasilis.vasilis.parametric.DTO.ParametricValuesTextareaRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/parametric-values")
@CrossOrigin(origins = "*")
public class ParametricValueController {

    @Autowired
    private ParametricValueService service;

    @GetMapping("/list")
    public List<ParametricValueDTO> getList(@RequestParam(value = "type", required = false) String type) {
        return service.getList(type);
    }

    @PostMapping("/save")
    public ResponseEntity<ParametricValueDTO> save(@RequestBody ParametricValueDTO request) {
        try {
            return ResponseEntity.ok(service.save(request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ParametricValueDTO> update(@PathVariable Long id, @RequestBody ParametricValueDTO request) {
        try {
            return ResponseEntity.ok(service.update(id, request));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @PutMapping("/replace-from-textarea")
    public ResponseEntity<List<ParametricValueDTO>> replaceFromTextarea(@RequestBody ParametricValuesTextareaRequestDTO request) {
        try {
            return ResponseEntity.ok(service.replaceFromTextarea(request.getType(), request.getValuesText()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/textarea-values")
    public ResponseEntity<String> getTextareaValues(@RequestParam("type") String type) {
        try {
            return ResponseEntity.ok(service.getTextareaValues(type));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

