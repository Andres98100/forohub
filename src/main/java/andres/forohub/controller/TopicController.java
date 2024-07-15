package andres.forohub.controller;

import andres.forohub.domain.topic.DtoRegisterTopic;
import andres.forohub.domain.topic.DtoResponseTopic;
import andres.forohub.domain.topic.Topic;
import andres.forohub.domain.topic.TopicRepository;
import andres.forohub.domain.user.DtoResponseUser;
import andres.forohub.domain.user.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicRepository topicRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DtoResponseTopic> createTopic(@RequestBody @Valid DtoRegisterTopic dtoRegisterTopic,
                                                        UriComponentsBuilder uriComponentsBuilder) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Topic topic = new Topic(dtoRegisterTopic, user);
        topicRepository.save(topic);
        DtoResponseTopic dtoResponseTopic = new DtoResponseTopic(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getCreationDate(),
                topic.getNameCourse(),
                new DtoResponseUser(topic.getUser().getId(), topic.getUser().getUsername(), topic.getUser().getEmail())
        );
        return ResponseEntity.created(uriComponentsBuilder.path("/topic/{id}").buildAndExpand(topic.getId()).toUri())
                .body(dtoResponseTopic);
    }

    @GetMapping
    public ResponseEntity<List<DtoResponseTopic>> getTopics() {
        List<DtoResponseTopic> dtoResponseTopics = topicRepository.findAllActive().stream()
                .map(topic -> new DtoResponseTopic(
                        topic.getId(),
                        topic.getTitle(),
                        topic.getContent(),
                        topic.getCreationDate(),
                        topic.getNameCourse(),
                        new DtoResponseUser(topic.getUser().getId(), topic.getUser().getUsername(), topic.getUser().getEmail())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoResponseTopics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoResponseTopic> getTopicByIdTopic(@PathVariable Long id) {
        Topic topic = topicRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found or is inactive"));
        return ResponseEntity.ok(new DtoResponseTopic(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getCreationDate(),
                topic.getNameCourse(),
                new DtoResponseUser(topic.getUser().getId(), topic.getUser().getUsername(), topic.getUser().getEmail())
        ));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<DtoResponseTopic>> getTopicsByUserId(@PathVariable Long id) {
        List<DtoResponseTopic> dtoResponseTopics = topicRepository.findActiveByUserId(id).stream()
                .map(topic -> new DtoResponseTopic(
                        topic.getId(),
                        topic.getTitle(),
                        topic.getContent(),
                        topic.getCreationDate(),
                        topic.getNameCourse(),
                        new DtoResponseUser(topic.getUser().getId(), topic.getUser().getUsername(), topic.getUser().getEmail())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoResponseTopics);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteTopic(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        if (!Objects.equals(topic.getUser().getId(), user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        topic.disableTopic();
        topicRepository.save(topic);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DtoResponseTopic> updateTopic(@PathVariable Long id, @RequestBody @Valid DtoRegisterTopic dtoRegisterTopic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Topic not found"));

        if (!Objects.equals(topic.getUser().getId(), user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        topic.updateTopic(dtoRegisterTopic);
        topicRepository.save(topic);
        DtoResponseTopic dtoResponseTopic = new DtoResponseTopic(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getCreationDate(),
                topic.getNameCourse(),
                new DtoResponseUser(topic.getUser().getId(), topic.getUser().getUsername(), topic.getUser().getEmail())
        );
        return ResponseEntity.ok(dtoResponseTopic);
    }
}
