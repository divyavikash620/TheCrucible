package com.learnos.repository;

import com.learnos.entity.Problem;
import com.learnos.entity.enums.ProblemSource;
import com.learnos.entity.enums.ProblemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByUserId(Long userId);

    List<Problem> findBySource(ProblemSource source);

    List<Problem> findByTopicId(Long topicId);

    List<Problem> findByStatus(ProblemStatus status);

    List<Problem> findByUserIdAndStatus(Long userId, ProblemStatus status);
}
