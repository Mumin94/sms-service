package dasturlash.uz.specification;

import dasturlash.uz.entity.Sms;
import dasturlash.uz.enums.SmsStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SmsSpecifications {

    // Restrict results to SMS records owned by this client.
    public static Specification<Sms> belongsToClient(String clientId) {
        return (root, query, cb) -> cb.equal(root.get("client").get("id"), clientId);
    }

    // Include records from the start of the supplied date.
    public static Specification<Sms> createdFrom(LocalDate fromDate) {
        return (root, query, cb) -> {
            if (fromDate == null) {
                return cb.conjunction();
            } else {
                return cb.greaterThanOrEqualTo(root.get("createdDate"), fromDate.atStartOfDay());
            }
        };
    }

    // Include the entire final date by excluding the next midnight.
    public static Specification<Sms> createdTo(LocalDate toDate) {
        return (root, query, cb) -> {
            if (toDate == null) {
                return cb.conjunction();
            } else {
                return cb.lessThan(root.get("createdDate"), toDate.plusDays(1).atStartOfDay());
            }
        };
    }

    // Ignore blank input; otherwise match the phone number exactly.
    public static Specification<Sms> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.isBlank()) {
                return cb.conjunction();
            } else {
                return cb.equal(root.get("phone"), phone);
            }
        };
    }

    // Ignore an omitted status; otherwise require an exact match.
    public static Specification<Sms> hasStatus(SmsStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            } else {
                return cb.equal(root.get("status"), status);
            }
        };
    }
}