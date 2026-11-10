package dasturlash.uz.dto.request;

import dasturlash.uz.enums.ClientStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminClientFilterDTO {
    private String search;
    private ClientStatus status;
}
