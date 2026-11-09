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
@Profile("dev") // Create test data only when the dev profile is active.
public class TestClientInitializer {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    // Run once during application startup.
    @Bean
    public CommandLineRunner initializeTestClient() {
        return args -> {

            // Preserve the existing client's password, balance, and details.
            if (clientRepository.existsByLogin("test-client")) return;

            Client client = new Client();
            client.setCompanyName("Demo");
            client.setOwnerName("James");
            client.setOwnerSurname("Wilson");
            client.setPhone("+998901234567");
            client.setEmail("test-client@example.com");
            client.setLogin("test-client");

            // Store a hash instead of the plain-text test password.
            client.setPassword(passwordEncoder.encode("test-password"));

            client.setCreatedDate(LocalDateTime.now());

            // Start unfunded so top-up and insufficient-balance tests are possible.
            client.setBalance(BigDecimal.ZERO);
            client.setStatus(ClientStatus.ACTIVE);
            client.setVisible(true);
            clientRepository.save(client);
        };
    }
}