package dasturlash.uz.service;

import dasturlash.uz.dto.request.ClientLoginRequestDTO;
import dasturlash.uz.dto.request.ClientRegistrationRequestDTO;
import dasturlash.uz.dto.response.ClientResponseDTO;
import dasturlash.uz.entity.Client;
import dasturlash.uz.enums.ClientStatus;
import dasturlash.uz.exception.BadRequestException;
import dasturlash.uz.exception.DuplicateResourceException;
import dasturlash.uz.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientResponseDTO login(ClientLoginRequestDTO request) {
        Client client = clientRepository.findByLoginAndVisibleTrue(request.getLogin()).orElseThrow(() -> new BadRequestException("Login or password is incorrect."));

        //Compare the raw password with stored encoded password
        if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
            throw new BadRequestException("Login or password is incorrect.");
        }

        return toDTO(client);
    }

    public ClientResponseDTO register(ClientRegistrationRequestDTO request) {
        if (clientRepository.existsByLogin(request.getLogin())) {
            throw new DuplicateResourceException("Login already exists.");
        }

        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        Client client = createClient(request);
        return toDTO(client);
    }


    private ClientResponseDTO toDTO(Client client) {
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setId(client.getId());
        clientResponseDTO.setCompanyName(client.getCompanyName());
        clientResponseDTO.setBalance(client.getBalance());
        clientResponseDTO.setLogin(client.getLogin());
        clientResponseDTO.setOwnerName(client.getOwnerName());
        clientResponseDTO.setOwnerSurname(client.getOwnerSurname());
        clientResponseDTO.setPhone(client.getPhone());
        clientResponseDTO.setEmail(client.getEmail());
        clientResponseDTO.setStatus(client.getStatus());
        clientResponseDTO.setVisible(client.getVisible());
        clientResponseDTO.setCreatedDate(client.getCreatedDate());
        return clientResponseDTO;
    }

    private Client createClient(ClientRegistrationRequestDTO request) {
        Client client = new Client();
        client.setCompanyName(request.getCompanyName());
        client.setOwnerName(request.getOwnerName());
        client.setOwnerSurname(request.getOwnerSurname());
        client.setPhone(request.getPhone());
        client.setEmail(request.getEmail());
        client.setLogin(request.getLogin());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setBalance(BigDecimal.ZERO);
        client.setCreatedDate(LocalDateTime.now());
        client.setVisible(true);
        client.setStatus(ClientStatus.ACTIVE);
        return clientRepository.save(client);
    }
}
