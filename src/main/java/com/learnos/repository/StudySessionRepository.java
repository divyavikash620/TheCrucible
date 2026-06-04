package com.learnos.repository;

import com.learnos.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUserId(Long userId);

    List<StudySession> findByStartedAtBetween(Instant start, Instant end);

    List<StudySession> findByTopicId(Long topicId);

    List<StudySession> findByUserIdAndStartedAtBetween(Long userId, Instant start, Instant end);
}
