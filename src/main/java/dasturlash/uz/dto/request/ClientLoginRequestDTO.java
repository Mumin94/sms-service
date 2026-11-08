package dasturlash.uz.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientLoginRequestDTO {
    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
