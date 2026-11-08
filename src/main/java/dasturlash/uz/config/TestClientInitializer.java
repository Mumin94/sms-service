package dasturlash.uz.config;

import dasturlash.uz.entity.Client;
import dasturlash.uz.enums.ClientStatus;
import dasturlash.uz.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
public class TestClientInitializer {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeTestClient() {
        return args -> {
            if (clientRepository.existsByLogin("test-client")) return;
            Client client = new Client();
            client.setCompanyName("Demo");
            client.setOwnerName("James");
            client.setOwnerSurname("Wilson");
            client.setPhone("+998901234567");
            client.setEmail("test-client@example.com");
            client.setLogin("test-client");
            client.setPassword(passwordEncoder.encode("test-password"));
            client.setCreatedDate(LocalDateTime.now());
            client.setBalance(BigDecimal.ZERO);
            client.setStatus(ClientStatus.ACTIVE);
            client.setVisible(true);
            clientRepository.save(client);

        };
    }

}
