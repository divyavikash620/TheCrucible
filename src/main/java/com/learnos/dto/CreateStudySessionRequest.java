package com.learnos.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CreateStudySessionRequest(
        @NotNull
        Instant startedAt,
        Instant endedAt,
        Long topicId,
        Long goalId,
        Integer durationMinutes,
        String notes
        ) {

}
