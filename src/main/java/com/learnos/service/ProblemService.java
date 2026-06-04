package com.learnos.service;

import com.learnos.dto.CreateProblemRequest;
import com.learnos.dto.ProblemResponse;
import com.learnos.dto.UpdateProblemRequest;
import com.learnos.entity.enums.ProblemSource;

import java.util.List;

public interface ProblemService {

    ProblemResponse createProblem(Long userId, CreateProblemRequest request);

    ProblemResponse updateProblem(Long id, UpdateProblemRequest request);

    void deleteProblem(Long id);

    ProblemResponse getProblemById(Long id);

    List<ProblemResponse> getAllProblems();

    List<ProblemResponse> getProblemsBySource(ProblemSource source);

    List<ProblemResponse> getProblemsByTopic(Long topicId);
}
