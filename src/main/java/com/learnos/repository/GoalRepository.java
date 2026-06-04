package com.learnos.repository;

import com.learnos.entity.Goal;
import com.learnos.entity.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserId(Long userId);

    List<Goal> findByStatus(GoalStatus status);

    List<Goal> findByUserIdAndStatus(Long userId, GoalStatus status);
}
