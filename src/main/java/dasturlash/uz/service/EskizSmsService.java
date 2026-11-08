package dasturlash.uz.service;

import dasturlash.uz.dto.request.SmsSendRequestDTO;
import dasturlash.uz.dto.response.EskizSendResponseDTO;
import dasturlash.uz.dto.response.SmsSendResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class EskizSmsService {

    private final RestClient restClient;
    private final EskizAuthService eskizAuthService;

    @Value("${eskiz.base-url}")
    private String baseUrl;

    @Value("${eskiz.sender}")
    private String sender;

    public SmsSendResponseDTO send(SmsSendRequestDTO request) {
        String token = eskizAuthService.getToken();
        EskizSendResponseDTO providerResponse;

        try {
            providerResponse = sendToEskiz(request, token);
        } catch (HttpClientErrorException.Unauthorized ex) {
            // Retry once only after an explicit authentication rejection.
            String freshToken = eskizAuthService.refreshToken(token);
            providerResponse = sendToEskiz(request, freshToken);
        }

        if (providerResponse == null || providerResponse.getId() == null || providerResponse.getId().isBlank() || !"waiting".equalsIgnoreCase(providerResponse.getStatus())) {
            throw new IllegalStateException("Eskiz did not confirm acceptance of the SMS.");
        }

        SmsSendResponseDTO response = new SmsSendResponseDTO();
        response.setSuccess(true);
        response.setMock(false);
        response.setMessage("Eskiz accepted the SMS for processing.");

        return response;
    }

    private EskizSendResponseDTO sendToEskiz(SmsSendRequestDTO request, String token) {

        String phone = request.getPhone();
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("mobile_phone", phone);
        form.add("message", request.getMessage());
        form.add("from", sender);

        return restClient.post().uri(baseUrl + "/message/sms/send").headers(headers -> headers.setBearerAuth(token)).contentType(MediaType.MULTIPART_FORM_DATA).body(form).retrieve().body(EskizSendResponseDTO.class);
    }
}