package com.learnos.dto;

import com.learnos.entity.enums.GoalStatus;
import java.time.Instant;
import java.time.LocalDate;

public record GoalResponse(
        Long id,
        Long userId,
        String title,
        String description,
        Long topicId,
        LocalDate targetDate,
        GoalStatus status,
        Double progressPercent,
        Instant createdAt,
        Instant updatedAt
        ) {

}
