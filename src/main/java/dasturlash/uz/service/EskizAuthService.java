package dasturlash.uz.service;

import dasturlash.uz.dto.response.EskizLoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class EskizAuthService {

    private final RestClient restClient;

    @Value("${eskiz.base-url}")
    private String baseUrl;

    @Value("${eskiz.email}")
    private String email;

    @Value("${eskiz.password}")
    private String password;

    private String token;
    private Instant refreshAt;

    public synchronized String getToken() {
        if (token == null || token.isBlank() || refreshAt == null || !Instant.now().isBefore(refreshAt)) {
            login();
        }
        return token;
    }

    public synchronized String refreshToken(String rejectedToken) {
        // Another request may already have refreshed the rejected token.
        if (token == null || token.equals(rejectedToken)) {
            token = null;
            login();
        }
        return token;
    }

    private void login() {
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("email", email);
        form.add("password", password);

        EskizLoginResponseDTO response = restClient.post().uri(baseUrl + "/auth/login").contentType(MediaType.MULTIPART_FORM_DATA).body(form).retrieve().body(EskizLoginResponseDTO.class);
        if (response == null || response.getData() == null || response.getData().getToken() == null || response.getData().getToken().isBlank()) {
            throw new IllegalStateException("Eskiz login returned no token.");
        }
        token = response.getData().getToken();

        // Documentation says 30 days; renew a little earlier.
        refreshAt = Instant.now().plus(29, ChronoUnit.DAYS);
    }
}