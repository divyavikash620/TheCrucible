package com.learnos.service.impl;

import com.learnos.dto.CreateProblemRequest;
import com.learnos.dto.ProblemResponse;
import com.learnos.dto.UpdateProblemRequest;
import com.learnos.entity.Problem;
import com.learnos.entity.Topic;
import com.learnos.entity.User;
import com.learnos.entity.enums.ProblemSource;
import com.learnos.entity.enums.ProblemStatus;
import com.learnos.repository.ProblemRepository;
import com.learnos.repository.TopicRepository;
import com.learnos.repository.UserRepository;
import com.learnos.service.ProblemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository,
            UserRepository userRepository,
            TopicRepository topicRepository) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    @Transactional
    public ProblemResponse createProblem(Long userId, CreateProblemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        Topic topic = null;
        if (request.topicId() != null) {
            topic = topicRepository.findById(request.topicId())
                    .orElseThrow(() -> new NoSuchElementException("Topic not found: " + request.topicId()));
        }

        if (request.source() == null) {
            throw new IllegalArgumentException("Problem source is required.");
        }

        Problem problem = new Problem();
        problem.setUser(user);
        problem.setTopic(topic);
        problem.setSource(request.source());
        problem.setTitle(request.title());
        problem.setDifficulty(request.difficulty());
        problem.setStatus(request.status() == null ? ProblemStatus.UNATTEMPTED : request.status());
        problem.setUrl(request.url());
        problem.setSolutionNotes(request.solutionNotes());
        problem.setTags(request.tags());
        problem.setAttemptedAt(request.attemptedAt());

        Problem saved = problemRepository.save(problem);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ProblemResponse updateProblem(Long id, UpdateProblemRequest request) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Problem not found: " + id));

        if (request.topicId() != null) {
            Topic topic = topicRepository.findById(request.topicId())
                    .orElseThrow(() -> new NoSuchElementException("Topic not found: " + request.topicId()));
            problem.setTopic(topic);
        }
        if (request.source() != null) {
            problem.setSource(request.source());
        }
        if (request.title() != null) {
            problem.setTitle(request.title());
        }
        if (request.difficulty() != null) {
            problem.setDifficulty(request.difficulty());
        }
        if (request.status() != null) {
            problem.setStatus(request.status());
        }
        if (request.url() != null) {
            problem.setUrl(request.url());
        }
        if (request.solutionNotes() != null) {
            problem.setSolutionNotes(request.solutionNotes());
        }
        if (request.tags() != null) {
            problem.setTags(request.tags());
        }
        if (request.attemptedAt() != null) {
            problem.setAttemptedAt(request.attemptedAt());
        }

        Problem updated = problemRepository.save(problem);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProblem(Long id) {
        if (!problemRepository.existsById(id)) {
            throw new NoSuchElementException("Problem not found: " + id);
        }
        problemRepository.deleteById(id);
    }

    @Override
    public ProblemResponse getProblemById(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Problem not found: " + id));
        return mapToResponse(problem);
    }

    @Override
    public List<ProblemResponse> getAllProblems() {
        List<ProblemResponse> responses = new ArrayList<>();
        for (Problem problem : problemRepository.findAll()) {
            responses.add(mapToResponse(problem));
        }
        return responses;
    }

    @Override
    public List<ProblemResponse> getProblemsBySource(ProblemSource source) {
        List<ProblemResponse> responses = new ArrayList<>();
        for (Problem problem : problemRepository.findBySource(source)) {
            responses.add(mapToResponse(problem));
        }
        return responses;
    }

    @Override
    public List<ProblemResponse> getProblemsByTopic(Long topicId) {
        List<ProblemResponse> responses = new ArrayList<>();
        for (Problem problem : problemRepository.findByTopicId(topicId)) {
            responses.add(mapToResponse(problem));
        }
        return responses;
    }

    private ProblemResponse mapToResponse(Problem problem) {
        Long topicId = problem.getTopic() != null ? problem.getTopic().getId() : null;
        return new ProblemResponse(
                problem.getId(),
                problem.getUser().getId(),
                topicId,
                problem.getSource(),
                problem.getTitle(),
                problem.getDifficulty(),
                problem.getStatus(),
                problem.getUrl(),
                problem.getSolutionNotes(),
                problem.getTags(),
                problem.getAttemptedAt(),
                problem.getCreatedAt(),
                problem.getUpdatedAt()
        );
    }
}
