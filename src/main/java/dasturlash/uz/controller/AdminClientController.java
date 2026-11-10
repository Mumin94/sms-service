package dasturlash.uz.controller;

import dasturlash.uz.dto.request.AdminClientFilterDTO;
import dasturlash.uz.dto.response.ClientResponseDTO;
import dasturlash.uz.service.ClientService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/client")
public class AdminClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> getAll(@RequestParam(defaultValue = "1")
         @Min(1) int page, @RequestParam(defaultValue = "10") @Min(1)
         @Max(100) int size, @ModelAttribute AdminClientFilterDTO filter) {

        return ResponseEntity.ok(clientService.getAll(page - 1, size, filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable("id") String id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<ClientResponseDTO> block(@PathVariable("id") String id) {
        return ResponseEntity.ok(clientService.block(id));
    }

    @PutMapping("/{id}/unblock")
    public ResponseEntity<ClientResponseDTO> unblock(@PathVariable("id") String id) {
        return ResponseEntity.ok(clientService.unblock(id));
    }
}