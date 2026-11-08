package dasturlash.uz.controller;

import dasturlash.uz.dto.request.SmsSendRequestDTO;
import dasturlash.uz.dto.response.SmsSendResponseDTO;
import dasturlash.uz.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sms")
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<SmsSendResponseDTO> sendSms(@Valid @RequestBody SmsSendRequestDTO smsSendRequestDTO) {
        return ResponseEntity.ok(smsService.send(smsSendRequestDTO));
    }
}
