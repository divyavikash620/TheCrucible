package com.learnos.dto;

import java.time.Instant;

public record UpdateStudySessionRequest(
        Instant startedAt,
        Instant endedAt,
        Long topicId,
        Long goalId,
        Integer durationMinutes,
        String notes
        ) {

}
