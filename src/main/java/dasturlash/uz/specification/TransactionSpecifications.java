package dasturlash.uz.specification;

import dasturlash.uz.entity.Transaction;
import dasturlash.uz.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TransactionSpecifications {

    // Restrict results to transactions owned by this client.
    public static Specification<Transaction> belongsToClient(String clientId) {
        return (root, query, cb) -> cb.equal(root.get("client").get("id"), clientId);
    }

    // Ignore an omitted type; otherwise match CREDIT or DEBIT.
    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> {
            if (type == null) {
                return cb.conjunction();
            } else {
                return cb.equal(root.get("type"), type);
            }
        };
    }

    // Include records from the start of the supplied date.
    public static Specification<Transaction> createdFrom(LocalDate fromDate) {
        return (root, query, cb) -> {
            if (fromDate == null) {
                return cb.conjunction();
            } else {
                return cb.greaterThanOrEqualTo(root.get("createdDate"), fromDate.atStartOfDay());
            }
        };
    }

    // Include the entire final date by excluding the next midnight.
    public static Specification<Transaction> createdTo(LocalDate toDate) {
        return (root, query, cb) -> {
            if (toDate == null) {
                return cb.conjunction();
            } else {
                return cb.lessThan(root.get("createdDate"), toDate.plusDays(1).atStartOfDay());
            }
        };
    }
}