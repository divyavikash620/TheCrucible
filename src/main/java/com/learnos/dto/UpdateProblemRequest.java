package com.learnos.dto;

import com.learnos.entity.enums.ProblemSource;
import com.learnos.entity.enums.ProblemStatus;
import java.time.Instant;

public record UpdateProblemRequest(
        ProblemSource source,
        String title,
        Long topicId,
        String difficulty,
        ProblemStatus status,
        String url,
        String solutionNotes,
        String tags,
        Instant attemptedAt
        ) {

}
