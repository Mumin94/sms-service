package dasturlash.uz.dto.response;

import dasturlash.uz.enums.ClientStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClientResponseDTO {
    private String id;
    private String companyName;
    private String ownerName;
    private String ownerSurname;
    private String email;
    private String phone;
    private String login;
    private BigDecimal balance;
    private ClientStatus status;
    private LocalDateTime createdDate;
    private Boolean visible;
}
