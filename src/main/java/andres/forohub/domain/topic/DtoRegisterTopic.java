package andres.forohub.domain.topic;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record DtoRegisterTopic(
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotBlank
        String nameCourse
) {
}
