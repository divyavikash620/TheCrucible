package com.learnos.service.impl;

import com.learnos.dto.CreateTopicRequest;
import com.learnos.dto.TopicResponse;
import com.learnos.dto.UpdateTopicRequest;
import com.learnos.entity.Topic;
import com.learnos.entity.enums.TopicStatus;
import com.learnos.repository.TopicRepository;
import com.learnos.service.TopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    @Transactional
    public TopicResponse createTopic(CreateTopicRequest request) {
        validateTopicHierarchy(null, request.parentTopicId());
        validatePrerequisiteCycle(null, request.prerequisiteTopicId());

        Topic topic = new Topic();
        topic.setTitle(request.title());
        topic.setDomain(request.domain());
        topic.setDescription(request.description());
        topic.setDifficulty(request.difficulty());
        topic.setStatus(request.status() == null ? TopicStatus.NOT_STARTED : request.status());

        if (request.parentTopicId() != null) {
            topic.setParentTopic(topicRepository.findById(request.parentTopicId())
                    .orElseThrow(() -> new NoSuchElementException("Parent topic not found: " + request.parentTopicId())));
        }

        if (request.prerequisiteTopicId() != null) {
            topic.setPrerequisiteTopic(topicRepository.findById(request.prerequisiteTopicId())
                    .orElseThrow(() -> new NoSuchElementException("Prerequisite topic not found: " + request.prerequisiteTopicId())));
        }

        Topic saved = topicRepository.save(topic);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public TopicResponse updateTopic(Long id, UpdateTopicRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Topic not found: " + id));

        validateTopicHierarchy(id, request.parentTopicId());
        validatePrerequisiteCycle(id, request.prerequisiteTopicId());

        if (request.title() != null) {
            topic.setTitle(request.title());
        }
        if (request.domain() != null) {
            topic.setDomain(request.domain());
        }
        if (request.description() != null) {
            topic.setDescription(request.description());
        }
        if (request.difficulty() != null) {
            topic.setDifficulty(request.difficulty());
        }
        if (request.status() != null) {
            topic.setStatus(request.status());
        }

        if (request.parentTopicId() != null) {
            topic.setParentTopic(topicRepository.findById(request.parentTopicId())
                    .orElseThrow(() -> new NoSuchElementException("Parent topic not found: " + request.parentTopicId())));
        }

        if (request.prerequisiteTopicId() != null) {
            topic.setPrerequisiteTopic(topicRepository.findById(request.prerequisiteTopicId())
                    .orElseThrow(() -> new NoSuchElementException("Prerequisite topic not found: " + request.prerequisiteTopicId())));
        }

        Topic updated = topicRepository.save(topic);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            throw new NoSuchElementException("Topic not found: " + id);
        }
        topicRepository.deleteById(id);
    }

    @Override
    public TopicResponse getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Topic not found: " + id));
        return mapToResponse(topic);
    }

    @Override
    public List<TopicResponse> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        List<TopicResponse> responses = new ArrayList<>();
        for (Topic topic : topics) {
            responses.add(mapToResponse(topic));
        }
        return responses;
    }

    @Override
    public List<TopicResponse> getTopicTree() {
        List<Topic> topics = topicRepository.findAll();
        Map<Long, List<Topic>> childrenByParent = new HashMap<>();
        List<Topic> roots = new ArrayList<>();

        for (Topic topic : topics) {
            if (topic.getParentTopic() == null) {
                roots.add(topic);
            } else {
                childrenByParent.computeIfAbsent(topic.getParentTopic().getId(), key -> new ArrayList<>())
                        .add(topic);
            }
        }

        List<TopicResponse> ordered = new ArrayList<>();
        for (Topic root : roots) {
            appendTree(root, childrenByParent, ordered);
        }
        return ordered;
    }

    @Override
    public void validateTopicHierarchy(Long topicId, Long parentTopicId) {
        if (parentTopicId == null) {
            return;
        }
        if (topicId != null && parentTopicId.equals(topicId)) {
            throw new IllegalArgumentException("A topic cannot be its own parent.");
        }
        Topic parent = topicRepository.findById(parentTopicId)
                .orElseThrow(() -> new NoSuchElementException("Parent topic not found: " + parentTopicId));
        if (topicId != null && hasParentCycle(topicId, parent)) {
            throw new IllegalArgumentException("Topic hierarchy contains a cycle.");
        }
    }

    @Override
    public void validatePrerequisiteCycle(Long topicId, Long prerequisiteTopicId) {
        if (prerequisiteTopicId == null) {
            return;
        }
        if (topicId != null && prerequisiteTopicId.equals(topicId)) {
            throw new IllegalArgumentException("A topic cannot be its own prerequisite.");
        }
        Topic prerequisite = topicRepository.findById(prerequisiteTopicId)
                .orElseThrow(() -> new NoSuchElementException("Prerequisite topic not found: " + prerequisiteTopicId));
        if (topicId != null && hasPrerequisiteCycle(topicId, prerequisite)) {
            throw new IllegalArgumentException("Prerequisite relation contains a cycle.");
        }
    }

    private void appendTree(Topic topic, Map<Long, List<Topic>> childrenByParent, List<TopicResponse> ordered) {
        ordered.add(mapToResponse(topic));
        List<Topic> children = childrenByParent.get(topic.getId());
        if (children == null) {
            return;
        }
        children.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        for (Topic child : children) {
            appendTree(child, childrenByParent, ordered);
        }
    }

    private boolean hasParentCycle(Long topicId, Topic parent) {
        Topic current = parent;
        while (current != null) {
            if (Objects.equals(current.getId(), topicId)) {
                return true;
            }
            current = current.getParentTopic();
        }
        return false;
    }

    private boolean hasPrerequisiteCycle(Long topicId, Topic prerequisite) {
        Topic current = prerequisite;
        while (current != null) {
            if (Objects.equals(current.getId(), topicId)) {
                return true;
            }
            current = current.getPrerequisiteTopic();
        }
        return false;
    }

    private TopicResponse mapToResponse(Topic topic) {
        Long parentId = topic.getParentTopic() != null ? topic.getParentTopic().getId() : null;
        Long prerequisiteId = topic.getPrerequisiteTopic() != null ? topic.getPrerequisiteTopic().getId() : null;
        return new TopicResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getDomain(),
                topic.getDescription(),
                topic.getDifficulty(),
                topic.getStatus(),
                parentId,
                prerequisiteId,
                topic.getCreatedAt(),
                topic.getUpdatedAt()
        );
    }
}
