package dasturlash.uz.repository;

import dasturlash.uz.entity.Sms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsRepository extends JpaRepository<Sms, String>, JpaSpecificationExecutor<Sms> {
    Page<Sms> findAllByClient_Id(String clientId, Pageable pageable);
}
