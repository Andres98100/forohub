package andres.forohub.domain.topic;

import andres.forohub.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "topic")
@EqualsAndHashCode(of="id")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String content;
    @Column(nullable = false)
    private LocalDateTime creationDate;
    private boolean status;
    private String nameCourse;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Topic() {}
    public Topic(DtoRegisterTopic dtoRegisterTopic, User user) {
        this.status = true;
        this.title = dtoRegisterTopic.title();
        this.content = dtoRegisterTopic.content();
        this.creationDate = LocalDateTime.now();
        this.nameCourse = dtoRegisterTopic.nameCourse();
        this.user = user;
    }

    public void disableTopic() {
        this.status = false;
    }

    public void updateTopic(DtoRegisterTopic dtoRegisterTopic) {
        if (dtoRegisterTopic.title() != null) {
            this.title = dtoRegisterTopic.title();
        }
        if (dtoRegisterTopic.content() != null) {
            this.content = dtoRegisterTopic.content();
        }
        if (dtoRegisterTopic.nameCourse() != null) {
            this.nameCourse = dtoRegisterTopic.nameCourse();
        }
    }
}
