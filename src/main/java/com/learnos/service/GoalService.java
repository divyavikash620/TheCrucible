package com.learnos.service;

import com.learnos.dto.CreateGoalRequest;
import com.learnos.dto.GoalResponse;
import com.learnos.dto.UpdateGoalRequest;
import java.util.List;

public interface GoalService {

    GoalResponse createGoal(Long userId, CreateGoalRequest request);

    GoalResponse updateGoal(Long id, UpdateGoalRequest request);

    void deleteGoal(Long id);

    GoalResponse getGoalById(Long id);

    List<GoalResponse> getAllGoals();

    GoalResponse updateProgress(Long id, Double progressPercent);
}
