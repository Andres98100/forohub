package andres.forohub.domain.topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("SELECT t FROM Topic t WHERE t.status = true")
    List<Topic> findAllActive();

    @Query("SELECT t FROM Topic t WHERE t.id = :id AND t.status = true")
    Optional<Topic> findActiveById(Long id);

    @Query("SELECT t FROM Topic t WHERE t.user.id = :userId AND t.status = true")
    List<Topic> findActiveByUserId(Long userId);
}
