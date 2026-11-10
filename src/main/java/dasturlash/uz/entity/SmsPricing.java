package dasturlash.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sms_pricing")
public class SmsPricing {
    @Id
    private Integer id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;
}
