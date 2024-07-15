package andres.forohub.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoRegisterUser(
        @NotBlank
        String username,
        @NotBlank
        @Email
        String email,
        @NotNull
        String password
) {
}
