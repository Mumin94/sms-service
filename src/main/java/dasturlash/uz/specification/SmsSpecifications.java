package dasturlash.uz.specification;

import dasturlash.uz.entity.Sms;
import dasturlash.uz.enums.SmsStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class SmsSpecifications {
    public static Specification<Sms> belongsToClient(String clientId) {
        return (root, query, cb) -> cb.equal(root.get("client").get("id"), clientId);
    }

    public static Specification<Sms> createdFrom(LocalDate fromDate) {
        return (root, query, cb) -> {
            if (fromDate == null) {
                return cb.conjunction();
            } else {
                return cb.greaterThanOrEqualTo(root.get("createdDate"), fromDate.atStartOfDay());
            }
        };
    }

    public static Specification<Sms> createdTo(LocalDate toDate) {
        return (root, query, cb) -> {
            if (toDate == null) {
                return cb.conjunction();
            } else {
                return cb.lessThan(root.get("createdDate"), toDate.plusDays(1).atStartOfDay());
            }
        };
    }

    public static Specification<Sms> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.isBlank()) {
                return cb.conjunction();
            } else {
                return cb.equal(root.get("phone"), phone);
            }
        };
    }

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
