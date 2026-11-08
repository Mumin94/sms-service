package dasturlash.uz.service;

import dasturlash.uz.dto.request.AdminTransactionFilterDTO;
import dasturlash.uz.dto.response.TransactionResponseDTO;
import dasturlash.uz.entity.Transaction;
import dasturlash.uz.exception.BadRequestException;
import dasturlash.uz.exception.ResourceNotFoundException;
import dasturlash.uz.repository.TransactionRepository;
import dasturlash.uz.specification.TransactionSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public TransactionResponseDTO getById(String id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + id));
        return toDTO(transaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> getAll(int page, int size, AdminTransactionFilterDTO filter) {
        if (filter.getFromDate() != null && filter.getToDate() != null && filter.getFromDate().isAfter(filter.getToDate())) {
            throw new BadRequestException("From date must not be after to date.");
        }
        Specification<Transaction> specification = TransactionSpecifications.createdFrom(filter.getFromDate())
                .and(TransactionSpecifications.createdTo(filter.getToDate()))
                .and(TransactionSpecifications.hasType(filter.getType()));

        if (filter.getClientId() != null &&  !filter.getClientId().isBlank()) {
            specification = specification.and(TransactionSpecifications.belongsToClient(filter.getClientId()));
        }

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate", "id"));
        Page<Transaction> result = transactionRepository.findAll(specification, pageable);
        return result.map(this::toDTO);

    }

    private TransactionResponseDTO toDTO(Transaction transaction) {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(transaction.getId());
        response.setClientId(transaction.getClient().getId());
        response.setType(transaction.getType());
        response.setAmount(transaction.getAmount());
        response.setDescription(transaction.getDescription());
        response.setBalanceBefore(transaction.getBalanceBefore());
        response.setBalanceAfter(transaction.getBalanceAfter());
        response.setCreatedDate(transaction.getCreatedDate());
        return response;
    }
}
