package andres.forohub.domain.topic;

import andres.forohub.domain.user.DtoResponseUser;

import java.time.LocalDateTime;

public record DtoResponseTopic(
        long id,
        String title,
        String content,
        LocalDateTime creationDate,
        String nameCourse,
        DtoResponseUser user
) {
}
