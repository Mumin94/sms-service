package dasturlash.uz.service;

import dasturlash.uz.dto.request.AdminClientFilterDTO;
import dasturlash.uz.dto.request.FillBalanceRequestDTO;
import dasturlash.uz.dto.response.BalanceResponseDTO;
import dasturlash.uz.dto.response.ClientResponseDTO;
import dasturlash.uz.entity.Client;
import dasturlash.uz.enums.ClientStatus;
import dasturlash.uz.exception.ResourceNotFoundException;
import dasturlash.uz.repository.ClientRepository;
import dasturlash.uz.specification.ClientSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final TransactionService transactionService;

    // Read the balance without modifying the client.
    public BalanceResponseDTO getBalance(String clientId) {
        Client client = clientRepository.findByIdAndVisibleTrue(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));

        return toBalanceDTO(client);
    }

    // Commit the balance increase and its CREDIT record together.
    @Transactional
    public BalanceResponseDTO fillBalance(String clientId, FillBalanceRequestDTO request) {

        // Use the same row lock as SMS deductions to serialize balance changes.
        Client client = clientRepository.findForUpdateByIdAndVisibleTrue(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found."));

        BigDecimal balanceBefore = client.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());

        // JPA persists this managed client's balance change at commit.
        client.setBalance(balanceAfter);

        // Preserve the reason, amount, and balances for transaction reporting.
        transactionService.createCredit(client, request.getAmount(), balanceBefore, balanceAfter);
        return toBalanceDTO(client);
    }
    // Combine optional filters and return visible clients, newest first.
    @Transactional(readOnly = true)
    public Page<ClientResponseDTO> getAll(int page, int size, AdminClientFilterDTO filter) {
        Specification<Client> specification = ClientSpecifications.isVisible()
                .and(ClientSpecifications.hasStatus(filter.getStatus()))
                .and(ClientSpecifications.search(filter.getSearch()));
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate", "id"));
        Page<Client> result = clientRepository.findAll(specification, pageable);
        return result.map(this::toDTO);
    }

    // Exclude hidden clients from the lookup.
    @Transactional(readOnly = true)
    public ClientResponseDTO getById(String id) {
        Client client = clientRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> new ResourceNotFoundException("Client not found: " + id));
        return toDTO(client);
    }

    // Use the same client lock as SMS sending to coordinate status changes.
    @Transactional
    public ClientResponseDTO block(String id) {
        Client client = clientRepository.findForUpdateByIdAndVisibleTrue(id).orElseThrow(() -> new ResourceNotFoundException("Client not found: " + id));
        client.setStatus(ClientStatus.BLOCKED);
        return toDTO(client);
    }
    // Restore SMS access; the managed status change is saved at commit.
    @Transactional
    public ClientResponseDTO unblock(String id) {
        Client client = clientRepository.findForUpdateByIdAndVisibleTrue(id).orElseThrow(() -> new ResourceNotFoundException("Client not found: " + id));
        client.setStatus(ClientStatus.ACTIVE);
        return toDTO(client);
    }

    private ClientResponseDTO toDTO(Client client) {
        ClientResponseDTO response = new ClientResponseDTO();
        response.setId(client.getId());
        response.setEmail(client.getEmail());
        response.setOwnerName(client.getOwnerName());
        response.setOwnerSurname(client.getOwnerSurname());
        response.setCreatedDate(client.getCreatedDate());
        response.setBalance(client.getBalance());
        response.setVisible(client.getVisible());
        response.setStatus(client.getStatus());
        response.setPhone(client.getPhone());
        response.setCompanyName(client.getCompanyName());
        response.setLogin(client.getLogin());
        return response;
    }

    private BalanceResponseDTO toBalanceDTO(Client client) {
        BalanceResponseDTO balanceDTO = new BalanceResponseDTO();
        balanceDTO.setBalance(client.getBalance());
        balanceDTO.setClientId(client.getId());
        return balanceDTO;
    }
}