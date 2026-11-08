package dasturlash.uz.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSendResponseDTO {
    private String message;
    private boolean success;
    private boolean mock;
}
