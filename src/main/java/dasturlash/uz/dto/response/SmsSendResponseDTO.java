package dasturlash.uz.dto.response;

import dasturlash.uz.enums.SmsStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class SmsSendResponseDTO {
    private String id;
    private String clientId;
    private String phone;
    private String text;
    private BigDecimal price;
    private SmsStatus status;
    private LocalDateTime createdDate;
    private BigDecimal balanceAfter;
}
