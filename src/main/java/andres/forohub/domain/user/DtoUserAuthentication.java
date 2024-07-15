package andres.forohub.domain.user;

import jakarta.validation.constraints.NotBlank;

public record DtoUserAuthentication(
        @NotBlank
        String username,
        @NotBlank
        String password) {
}
