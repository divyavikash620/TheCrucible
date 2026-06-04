package com.learnos.service;

import com.learnos.dto.CreateTopicRequest;
import com.learnos.dto.TopicResponse;
import com.learnos.dto.UpdateTopicRequest;
import java.util.List;

public interface TopicService {

    TopicResponse createTopic(CreateTopicRequest request);

    TopicResponse updateTopic(Long id, UpdateTopicRequest request);

    void deleteTopic(Long id);

    TopicResponse getTopicById(Long id);

    List<TopicResponse> getAllTopics();

    List<TopicResponse> getTopicTree();

    void validateTopicHierarchy(Long topicId, Long parentTopicId);

    void validatePrerequisiteCycle(Long topicId, Long prerequisiteTopicId);
}
