package dasturlash.uz.service;

import dasturlash.uz.dto.request.ClientLoginRequestDTO;
import dasturlash.uz.dto.request.FillBalanceRequestDTO;
import dasturlash.uz.dto.response.BalanceResponseDTO;
import dasturlash.uz.dto.response.ClientResponseDTO;
import dasturlash.uz.entity.Client;
import dasturlash.uz.entity.Transaction;
import dasturlash.uz.enums.TransactionType;
import dasturlash.uz.exception.BadRequestException;
import dasturlash.uz.exception.ResourceNotFoundException;
import dasturlash.uz.repository.ClientRepository;
import dasturlash.uz.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;

    public ClientResponseDTO login(ClientLoginRequestDTO request) {
        Client client = clientRepository.findByLoginAndVisibleTrue(request.getLogin()).orElseThrow(() -> new BadRequestException("Login or password is incorrect."));
        if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
            throw new BadRequestException("Login or password is incorrect.");
        }
        return toDTO(client);
    }

    public BalanceResponseDTO getBalance(String clientId) {
        Client client = clientRepository.findByIdAndVisibleTrue(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
        BalanceResponseDTO balanceResponseDTO = new BalanceResponseDTO();
        balanceResponseDTO.setBalance(client.getBalance());
        balanceResponseDTO.setClientId(client.getId());
        return balanceResponseDTO;
    }

    @Transactional
    public BalanceResponseDTO fillBalance(String clientId, FillBalanceRequestDTO request) {
        Client client = clientRepository.findForUpdateByIdAndVisibleTrue(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));
        BigDecimal balanceBefore = client.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());
        client.setBalance(balanceAfter);

        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setType(TransactionType.CREDIT);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setDescription("Balance top-up");
        transactionRepository.save(transaction);

        BalanceResponseDTO balanceDTO = new BalanceResponseDTO();
        balanceDTO.setBalance(client.getBalance());
        balanceDTO.setClientId(client.getId());

        return balanceDTO;

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
}
