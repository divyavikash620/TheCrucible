package com.learnos.dto;

import com.learnos.entity.enums.LearningDomain;
import com.learnos.entity.enums.TopicStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTopicRequest(
        @NotBlank
        String title,
        @NotNull
        LearningDomain domain,
        String description,
        String difficulty,
        TopicStatus status,
        Long parentTopicId,
        Long prerequisiteTopicId
        ) {

}
