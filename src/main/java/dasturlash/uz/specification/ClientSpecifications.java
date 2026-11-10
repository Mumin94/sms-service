package dasturlash.uz.specification;

import dasturlash.uz.entity.Client;
import dasturlash.uz.enums.ClientStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class ClientSpecifications {
    public static Specification<Client> isVisible() {
        return (root, query, cb) -> cb.isTrue(root.get("visible"));
    }

    public static Specification<Client> hasStatus(ClientStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            } else {
                return cb.equal(root.get("status"), status);
            }
        };
    }

    public static Specification<Client> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return cb.conjunction();
            } else {
                String value = "%" + search.strip().toLowerCase(Locale.ROOT) + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("companyName")), value),
                        cb.like(cb.lower(root.get("ownerName")), value),
                        cb.like(cb.lower(root.get("ownerSurname")), value),
                        cb.like(cb.lower(root.get("login")), value),
                        cb.like(cb.lower(root.get("email")), value),
                        cb.like(cb.lower(root.get("phone")), value)
                );
            }
        };

    }
}
