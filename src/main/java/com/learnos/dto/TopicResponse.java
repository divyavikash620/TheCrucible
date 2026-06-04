package com.learnos.dto;

import com.learnos.entity.enums.LearningDomain;
import com.learnos.entity.enums.TopicStatus;
import java.time.Instant;

public record TopicResponse(
        Long id,
        String title,
        LearningDomain domain,
        String description,
        String difficulty,
        TopicStatus status,
        Long parentTopicId,
        Long prerequisiteTopicId,
        Instant createdAt,
        Instant updatedAt
        ) {

}
