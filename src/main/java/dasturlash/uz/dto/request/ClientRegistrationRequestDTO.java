package dasturlash.uz.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRegistrationRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String companyName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String ownerName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String ownerSurname;

    @NotBlank
    @Pattern(regexp = "^\\+998\\d{9}$")
    private String phone;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 3, max = 50)
    private String login;

    @NotBlank
    @Size(min = 8)
    private String password;
}
