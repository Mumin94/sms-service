package dasturlash.uz.controller;

import dasturlash.uz.dto.request.AdminSmsFilterDTO;
import dasturlash.uz.dto.response.SmsResponseDTO;
import dasturlash.uz.service.SmsService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sms")
public class AdminSmsController {
    private final SmsService smsService;

    @GetMapping("/{id}")
    public ResponseEntity<SmsResponseDTO> getSmsById(@PathVariable("id") String id) {
        return ResponseEntity.ok(smsService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SmsResponseDTO>> getAll(@Min(1) @RequestParam(defaultValue = "1") int page, @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size, @ModelAttribute AdminSmsFilterDTO filter) {
        return ResponseEntity.ok(smsService.getAll(page - 1, size, filter));
    }
}
