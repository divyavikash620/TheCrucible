package com.learnos.service.impl;

import com.learnos.dto.CreateStudySessionRequest;
import com.learnos.dto.StudySessionResponse;
import com.learnos.dto.UpdateStudySessionRequest;
import com.learnos.entity.Goal;
import com.learnos.entity.StudySession;
import com.learnos.entity.Topic;
import com.learnos.entity.User;
import com.learnos.repository.GoalRepository;
import com.learnos.repository.StudySessionRepository;
import com.learnos.repository.TopicRepository;
import com.learnos.repository.UserRepository;
import com.learnos.service.StudySessionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class StudySessionServiceImpl implements StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final GoalRepository goalRepository;

    public StudySessionServiceImpl(StudySessionRepository studySessionRepository,
            UserRepository userRepository,
            TopicRepository topicRepository,
            GoalRepository goalRepository) {
        this.studySessionRepository = studySessionRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.goalRepository = goalRepository;
    }

    @Override
    @Transactional
    public StudySessionResponse createStudySession(Long userId, CreateStudySessionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        Topic topic = null;
        if (request.topicId() != null) {
            topic = topicRepository.findById(request.topicId())
                    .orElseThrow(() -> new NoSuchElementException("Topic not found: " + request.topicId()));
        }

        Goal goal = null;
        if (request.goalId() != null) {
            goal = goalRepository.findById(request.goalId())
                    .orElseThrow(() -> new NoSuchElementException("Goal not found: " + request.goalId()));
        }

        StudySession session = new StudySession();
        session.setUser(user);
        session.setTopic(topic);
        session.setGoal(goal);
        session.setStartedAt(request.startedAt());
        session.setEndedAt(request.endedAt());
        session.setDurationMinutes(resolveDuration(request.startedAt(), request.endedAt(), request.durationMinutes()));
        session.setNotes(request.notes());

        StudySession saved = studySessionRepository.save(session);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public StudySessionResponse updateStudySession(Long id, UpdateStudySessionRequest request) {
        StudySession session = studySessionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Study session not found: " + id));

        if (request.topicId() != null) {
            Topic topic = topicRepository.findById(request.topicId())
                    .orElseThrow(() -> new NoSuchElementException("Topic not found: " + request.topicId()));
            session.setTopic(topic);
        }
        if (request.goalId() != null) {
            Goal goal = goalRepository.findById(request.goalId())
                    .orElseThrow(() -> new NoSuchElementException("Goal not found: " + request.goalId()));
            session.setGoal(goal);
        }
        if (request.startedAt() != null) {
            session.setStartedAt(request.startedAt());
        }
        if (request.endedAt() != null) {
            session.setEndedAt(request.endedAt());
        }
        if (request.durationMinutes() != null) {
            session.setDurationMinutes(request.durationMinutes());
        } else {
            session.setDurationMinutes(resolveDuration(session.getStartedAt(), session.getEndedAt(), session.getDurationMinutes()));
        }
        if (request.notes() != null) {
            session.setNotes(request.notes());
        }

        StudySession updated = studySessionRepository.save(session);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteStudySession(Long id) {
        if (!studySessionRepository.existsById(id)) {
            throw new NoSuchElementException("Study session not found: " + id);
        }
        studySessionRepository.deleteById(id);
    }

    @Override
    public StudySessionResponse getStudySessionById(Long id) {
        StudySession session = studySessionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Study session not found: " + id));
        return mapToResponse(session);
    }

    @Override
    public List<StudySessionResponse> getAllStudySessions() {
        List<StudySessionResponse> responses = new ArrayList<>();
        for (StudySession session : studySessionRepository.findAll()) {
            responses.add(mapToResponse(session));
        }
        return responses;
    }

    @Override
    public long getTotalStudyTime(Long userId) {
        List<StudySession> sessions = studySessionRepository.findByUserId(userId);
        return sessions.stream()
                .mapToLong(this::calculateDurationMinutes)
                .sum();
    }

    @Override
    public List<StudySessionResponse> getSessionsBetweenDates(Long userId, Instant start, Instant end) {
        List<StudySessionResponse> responses = new ArrayList<>();
        for (StudySession session : studySessionRepository.findByUserIdAndStartedAtBetween(userId, start, end)) {
            responses.add(mapToResponse(session));
        }
        return responses;
    }

    private Integer resolveDuration(Instant startedAt, Instant endedAt, Integer explicitDuration) {
        if (explicitDuration != null) {
            return explicitDuration;
        }
        if (startedAt != null && endedAt != null && !endedAt.isBefore(startedAt)) {
            return (int) Duration.between(startedAt, endedAt).toMinutes();
        }
        return null;
    }

    private long calculateDurationMinutes(StudySession session) {
        if (session.getDurationMinutes() != null) {
            return session.getDurationMinutes();
        }
        Integer duration = resolveDuration(session.getStartedAt(), session.getEndedAt(), null);
        return duration != null ? duration : 0;
    }

    private StudySessionResponse mapToResponse(StudySession session) {
        Long topicId = session.getTopic() != null ? session.getTopic().getId() : null;
        Long goalId = session.getGoal() != null ? session.getGoal().getId() : null;
        return new StudySessionResponse(
                session.getId(),
                session.getUser().getId(),
                topicId,
                goalId,
                session.getStartedAt(),
                session.getEndedAt(),
                session.getDurationMinutes(),
                session.getNotes(),
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }
}
