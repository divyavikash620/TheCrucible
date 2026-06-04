package com.learnos.service;

import com.learnos.dto.CreateStudySessionRequest;
import com.learnos.dto.StudySessionResponse;
import com.learnos.dto.UpdateStudySessionRequest;
import java.time.Instant;
import java.util.List;

public interface StudySessionService {

    StudySessionResponse createStudySession(Long userId, CreateStudySessionRequest request);

    StudySessionResponse updateStudySession(Long id, UpdateStudySessionRequest request);

    void deleteStudySession(Long id);

    StudySessionResponse getStudySessionById(Long id);

    List<StudySessionResponse> getAllStudySessions();

    long getTotalStudyTime(Long userId);

    List<StudySessionResponse> getSessionsBetweenDates(Long userId, Instant start, Instant end);
}
