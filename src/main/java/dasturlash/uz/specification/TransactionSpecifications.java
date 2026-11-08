package dasturlash.uz.specification;

import dasturlash.uz.entity.Transaction;
import dasturlash.uz.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TransactionSpecification {

    Specification<Transaction> belongsToClient(String clientId);

    Specification<Transaction> hasType(TransactionType type);

    Specification<Transaction> createdFrom(LocalDate fromDate);

    Specification<Transaction> createdTo(LocalDate toDate);
}
