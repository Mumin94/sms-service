package dasturlash.uz.service;

import dasturlash.uz.dto.request.AdminSmsFilterDTO;
import dasturlash.uz.dto.request.SmsHistoryFilterDTO;
import dasturlash.uz.dto.request.SmsSendRequestDTO;
import dasturlash.uz.dto.response.SmsResponseDTO;
import dasturlash.uz.dto.response.SmsSendResponseDTO;
import dasturlash.uz.entity.Client;
import dasturlash.uz.entity.Sms;
import dasturlash.uz.entity.Transaction;
import dasturlash.uz.enums.ClientStatus;
import dasturlash.uz.enums.SmsStatus;
import dasturlash.uz.enums.TransactionType;
import dasturlash.uz.exception.BadRequestException;
import dasturlash.uz.exception.ResourceNotFoundException;
import dasturlash.uz.repository.ClientRepository;
import dasturlash.uz.repository.SmsRepository;
import dasturlash.uz.repository.TransactionRepository;
import dasturlash.uz.specification.SmsSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final ClientRepository clientRepository;
    private final SmsRepository smsRepository;
    private final TransactionRepository transactionRepository;

    // Balance change, SMS and DEBIT record commit together.
    // If an operation fails with a runtime exception, all changes roll back.
    @Transactional
    public SmsSendResponseDTO send(SmsSendRequestDTO request) {

        // Lock this client's row until the transaction finishes.
        // Concurrent balance changes for the same client must wait.
        Client client = clientRepository.findForUpdateByIdAndVisibleTrue(request.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        // BLOCKED clients may log in, but cannot send SMS.
        if (client.getStatus() != ClientStatus.ACTIVE) {
            throw new BadRequestException("Client is not active");
        }

        BigDecimal smsPrice = new BigDecimal("125.00");

        // Reject before changing any data if the balance is insufficient.
        if (smsPrice.compareTo(client.getBalance()) > 0) {
            throw new BadRequestException("Insufficient balance.");
        }

        // Keep both balances for the transaction history.
        BigDecimal balanceBefore = client.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(smsPrice);

        //Set client new balance
        client.setBalance(balanceAfter);

        // Store the SMS and its fixed price.
        Sms sms = new Sms();
        sms.setClient(client);
        sms.setPhone(request.getPhone());
        sms.setText(request.getText());
        sms.setPrice(smsPrice);
        sms.setStatus(SmsStatus.SENT);
        sms.setCreatedDate(LocalDateTime.now());
        smsRepository.save(sms);

        // Record why money was deducted and the balance before/after.
        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setType(TransactionType.DEBIT);
        transaction.setAmount(smsPrice);
        transaction.setDescription("SMS sending: " + sms.getId());
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setCreatedDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        SmsSendResponseDTO response = new SmsSendResponseDTO();

        // SMS ID and client ID identify different records.
        response.setId(sms.getId());
        response.setClientId(client.getId());

        response.setPhone(sms.getPhone());
        response.setText(sms.getText());
        response.setPrice(sms.getPrice());
        response.setStatus(sms.getStatus());
        response.setBalanceAfter(balanceAfter);
        response.setCreatedDate(sms.getCreatedDate());

        return response;
    }

    private SmsResponseDTO toDTO(Sms sms) {
        SmsResponseDTO response = new SmsResponseDTO();
        response.setId(sms.getId());
        response.setClientId(sms.getClient().getId());
        response.setPhone(sms.getPhone());
        response.setText(sms.getText());
        response.setPrice(sms.getPrice());
        response.setStatus(sms.getStatus());
        response.setCreatedDate(sms.getCreatedDate());
        return response;
    }

    // Get this client's SMS history with optional date filters and pagination.
    @Transactional(readOnly = true)
    public Page<SmsResponseDTO> getMySms(String clientId, int page, int size, SmsHistoryFilterDTO filter) {
        if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().isAfter(filter.getToDate())) {
            throw new BadRequestException("From date must not be after to date.");
        }
        Specification<Sms> specification = SmsSpecifications.belongsToClient(clientId)
                .and(SmsSpecifications.createdFrom(filter.getFromDate()))
                .and(SmsSpecifications.createdTo(filter.getToDate()));

        clientRepository.findByIdAndVisibleTrue(clientId).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate", "id"));
        Page<Sms> result = smsRepository.findAll(specification, pageable);
        return result.map(this::toDTO);
    }

    // Get one SMS by ID for admin reporting.
    @Transactional(readOnly = true)
    public SmsResponseDTO getById(String id) {
        Sms sms = smsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SMS not found: " + id));
        return toDTO(sms);
    }

    //Get ALL SMS for admin
    @Transactional(readOnly = true)
    public Page<SmsResponseDTO> getAll(int page, int size, AdminSmsFilterDTO filter) {
        if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().isAfter(filter.getToDate())) {
            throw new BadRequestException("From date must not be after to date.");
        }

        Specification<Sms> specification =
                SmsSpecifications.createdFrom(filter.getFromDate())
                        .and(SmsSpecifications.createdTo(filter.getToDate()))
                        .and(SmsSpecifications.hasPhone(filter.getPhone()))
                        .and(SmsSpecifications.hasStatus(filter.getStatus()));

        if (filter.getClientId() != null && !filter.getClientId().isBlank()) {
            specification = specification.and(SmsSpecifications.belongsToClient(filter.getClientId()));
        }
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate", "id"));
        Page<Sms> result = smsRepository.findAll(specification, pageable);
        return result.map(this::toDTO);
    }
}