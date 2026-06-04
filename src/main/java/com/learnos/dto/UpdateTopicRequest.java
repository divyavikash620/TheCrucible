package com.learnos.dto;

import com.learnos.entity.enums.LearningDomain;
import com.learnos.entity.enums.TopicStatus;

public record UpdateTopicRequest(
        String title,
        LearningDomain domain,
        String description,
        String difficulty,
        TopicStatus status,
        Long parentTopicId,
        Long prerequisiteTopicId
        ) {

}
