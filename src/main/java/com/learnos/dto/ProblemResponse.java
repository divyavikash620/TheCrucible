package com.learnos.dto;

import com.learnos.entity.enums.ProblemSource;
import com.learnos.entity.enums.ProblemStatus;
import java.time.Instant;

public record ProblemResponse(
        Long id,
        Long userId,
        Long topicId,
        ProblemSource source,
        String title,
        String difficulty,
        ProblemStatus status,
        String url,
        String solutionNotes,
        String tags,
        Instant attemptedAt,
        Instant createdAt,
        Instant updatedAt
        ) {

}
