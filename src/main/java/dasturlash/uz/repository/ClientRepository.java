package dasturlash.uz.repository;

import dasturlash.uz.entity.Client;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String>, JpaSpecificationExecutor<Client> {
    Optional<Client> findByLoginAndVisibleTrue(String login);

    Optional<Client> findByIdAndVisibleTrue(String id);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Client> findForUpdateByIdAndVisibleTrue (String id);
}
