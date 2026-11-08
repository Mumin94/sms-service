package dasturlash.uz.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FillBalanceRequestDTO {
    @NotNull
    @Positive
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;
}
