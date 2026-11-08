package dasturlash.uz.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSendRequestDTO {
    @NotBlank
    @Pattern(regexp = "^\\+998\\d{9}$")
    private String phone;

    @NotBlank
    @JsonProperty("client_id")
    private String clientId;

    @NotBlank
    private String text;
}
