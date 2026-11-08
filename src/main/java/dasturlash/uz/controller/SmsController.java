package dasturlash.uz.controller;

import dasturlash.uz.dto.request.SmsHistoryFilterDTO;
import dasturlash.uz.dto.request.SmsSendRequestDTO;
import dasturlash.uz.dto.response.SmsResponseDTO;
import dasturlash.uz.dto.response.SmsSendResponseDTO;
import dasturlash.uz.service.SmsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<SmsSendResponseDTO> sendSms(@Valid @RequestBody SmsSendRequestDTO request) {
        return ResponseEntity.ok(smsService.send(request));
    }

    @GetMapping("/my")
    public ResponseEntity<Page<SmsResponseDTO>> getMySms(@RequestHeader("ClientId") String clientId, @Min(1) @RequestParam(defaultValue = "1") int page, @Min(1) @Max(100) @RequestParam(defaultValue = "10") int size, @ModelAttribute SmsHistoryFilterDTO filter) {
        return ResponseEntity.ok(smsService.getMySms(clientId, page - 1, size, filter));
    }
}
