package com.learnos.repository;

import com.learnos.entity.Topic;
import com.learnos.entity.enums.LearningDomain;
import com.learnos.entity.enums.TopicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByDomain(LearningDomain domain);

    List<Topic> findByParentTopic(Topic parentTopic);

    List<Topic> findByPrerequisiteTopic(Topic prerequisiteTopic);

    List<Topic> findByStatus(TopicStatus status);
}
