package com.learnos.dto;

import java.time.Instant;

public record StudySessionResponse(
        Long id,
        Long userId,
        Long topicId,
        Long goalId,
        Instant startedAt,
        Instant endedAt,
        Integer durationMinutes,
        String notes,
        Instant createdAt,
        Instant updatedAt
        ) {

}
