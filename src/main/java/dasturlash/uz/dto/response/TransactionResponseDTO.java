package dasturlash.uz.dto.response;

import dasturlash.uz.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponseDTO {
    private String id;
    private String clientId;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private LocalDateTime createdDate;
}
