package dasturlash.uz.service;

import dasturlash.uz.dto.request.SmsSendRequestDTO;
import dasturlash.uz.dto.response.SmsSendResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final EskizSmsService eskizSmsService;

    @Value("${sms.mock:false}")
    private boolean mock;

    public SmsSendResponseDTO send(SmsSendRequestDTO request) {
        if (!mock) {
            return eskizSmsService.send(request);
        }

        System.out.println("MOCK SMS phone: " + request.getPhone());
        System.out.println("MOCK SMS message: " + request.getMessage());

        SmsSendResponseDTO response = new SmsSendResponseDTO();
        response.setSuccess(true);
        response.setMock(true);
        response.setMessage("Mock SMS accepted. No real SMS was sent.");
        return response;
    }
}