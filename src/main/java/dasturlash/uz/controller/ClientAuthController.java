package dasturlash.uz.controller;

import dasturlash.uz.dto.request.ClientLoginRequestDTO;
import dasturlash.uz.dto.response.ClientResponseDTO;
import dasturlash.uz.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/auth")
public class ClientAuthController {
    private final ClientService clientService;

    @PostMapping("/login")
    public ResponseEntity<ClientResponseDTO> login(@Valid @RequestBody ClientLoginRequestDTO request) {
        ClientResponseDTO client = clientService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }
}
