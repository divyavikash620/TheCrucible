package com.learnos.service.impl;

import com.learnos.dto.CreateGoalRequest;
import com.learnos.dto.GoalResponse;
import com.learnos.dto.UpdateGoalRequest;
import com.learnos.entity.Goal;
import com.learnos.entity.Topic;
import com.learnos.entity.User;
import com.learnos.entity.enums.GoalStatus;
import com.learnos.repository.GoalRepository;
import com.learnos.repository.TopicRepository;
import com.learnos.repository.UserRepository;
import com.learnos.service.GoalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public GoalServiceImpl(GoalRepository goalRepository,
            UserRepository userRepository,
            TopicRepository topicRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    @Transactional
    public GoalResponse createGoal(Long userId, CreateGoalRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        Topic topic = null;
        if (request.topicId() != null) {
            topic = topicRepository.findById(request.topicId())
                    .orElseThrow(() -> new NoSuchElementException("Topic not found: " + request.topicId()));
        }

        Goal goal = new Goal();
        goal.setUser(user);
        goal.setTitle(request.title());
        goal.setDescription(request.description());
        goal.setTopic(topic);
        goal.setTargetDate(request.targetDate());
        goal.setStatus(request.status() == null ? GoalStatus.NOT_STARTED : request.status());
        goal.setProgressPercent(0.0);

        Goal saved = goalRepository.save(goal);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public GoalResponse updateGoal(Long id, UpdateGoalRequest request) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Goal not found: " + id));

        if (request.title() != null) {
            goal.setTitle(request.title());
        }
        if (request.description() != null) {
            goal.setDescription(request.description());
        }
        if (request.topicId() != null) {
            Topic topic = topicRepository.findById(request.topicId())
                    .orElseThrow(() -> new NoSuchElementException("Topic not found: " + request.topicId()));
            goal.setTopic(topic);
        }
        if (request.targetDate() != null) {
            goal.setTargetDate(request.targetDate());
        }
        if (request.status() != null) {
            goal.setStatus(request.status());
        }
        if (request.progressPercent() != null) {
            if (request.progressPercent() < 0 || request.progressPercent() > 100) {
                throw new IllegalArgumentException("Progress percent must be between 0 and 100.");
            }
            goal.setProgressPercent(request.progressPercent());
        }

        Goal updated = goalRepository.save(goal);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteGoal(Long id) {
        if (!goalRepository.existsById(id)) {
            throw new NoSuchElementException("Goal not found: " + id);
        }
        goalRepository.deleteById(id);
    }

    @Override
    public GoalResponse getGoalById(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Goal not found: " + id));
        return mapToResponse(goal);
    }

    @Override
    public List<GoalResponse> getAllGoals() {
        List<GoalResponse> responses = new ArrayList<>();
        for (Goal goal : goalRepository.findAll()) {
            responses.add(mapToResponse(goal));
        }
        return responses;
    }

    @Override
    @Transactional
    public GoalResponse updateProgress(Long id, Double progressPercent) {
        if (progressPercent == null || progressPercent < 0 || progressPercent > 100) {
            throw new IllegalArgumentException("Progress percent must be between 0 and 100.");
        }

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Goal not found: " + id));

        goal.setProgressPercent(progressPercent);
        if (progressPercent.equals(100.0)) {
            goal.setStatus(GoalStatus.COMPLETED);
        } else if (goal.getStatus() == GoalStatus.NOT_STARTED) {
            goal.setStatus(GoalStatus.IN_PROGRESS);
        }

        Goal updated = goalRepository.save(goal);
        return mapToResponse(updated);
    }

    private GoalResponse mapToResponse(Goal goal) {
        Long topicId = goal.getTopic() != null ? goal.getTopic().getId() : null;
        return new GoalResponse(
                goal.getId(),
                goal.getUser().getId(),
                goal.getTitle(),
                goal.getDescription(),
                topicId,
                goal.getTargetDate(),
                goal.getStatus(),
                goal.getProgressPercent(),
                goal.getCreatedAt(),
                goal.getUpdatedAt()
        );
    }
}
