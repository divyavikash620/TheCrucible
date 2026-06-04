package com.learnos.dto;

import java.time.LocalDate;
import com.learnos.entity.enums.GoalStatus;
import jakarta.validation.constraints.NotBlank;

public record CreateGoalRequest(
        @NotBlank
        String title,
        String description,
        Long topicId,
        LocalDate targetDate,
        GoalStatus status
        ) {

}
