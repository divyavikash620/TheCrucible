package com.learnos.repository;

import com.learnos.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findByUserId(Long userId);

    List<JournalEntry> findByEntryDate(LocalDate entryDate);

    List<JournalEntry> findByUserIdAndEntryDate(Long userId, LocalDate entryDate);
}
