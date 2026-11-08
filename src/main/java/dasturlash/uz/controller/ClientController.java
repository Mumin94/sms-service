package dasturlash.uz.controller;

import dasturlash.uz.dto.request.FillBalanceRequestDTO;
import dasturlash.uz.dto.response.BalanceResponseDTO;
import dasturlash.uz.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceResponseDTO> getBalance(@RequestHeader("ClientId") String clientId) {
        return ResponseEntity.ok(clientService.getBalance(clientId));

    }

    @PostMapping("/fill-balance")
    public ResponseEntity<BalanceResponseDTO>fillBalance(@RequestHeader("ClientId") String clientId, @Valid @RequestBody FillBalanceRequestDTO request) {
        return ResponseEntity.ok(clientService.fillBalance(clientId, request));
    }

}
