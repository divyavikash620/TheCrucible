package com.learnos.dto;

import com.learnos.entity.enums.GoalStatus;
import java.time.LocalDate;

public record UpdateGoalRequest(
        String title,
        String description,
        Long topicId,
        LocalDate targetDate,
        GoalStatus status,
        Double progressPercent
        ) {

}
