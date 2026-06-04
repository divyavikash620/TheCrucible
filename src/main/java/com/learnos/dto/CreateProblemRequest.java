package com.learnos.dto;

import com.learnos.entity.enums.ProblemSource;
import com.learnos.entity.enums.ProblemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CreateProblemRequest(
        @NotNull
        ProblemSource source,
        @NotBlank
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
