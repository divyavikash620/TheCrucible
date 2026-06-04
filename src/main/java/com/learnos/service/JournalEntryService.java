package com.learnos.service;

import com.learnos.dto.CreateJournalEntryRequest;
import com.learnos.dto.JournalEntryResponse;
import com.learnos.dto.UpdateJournalEntryRequest;
import java.time.LocalDate;
import java.util.List;

public interface JournalEntryService {

    JournalEntryResponse createJournalEntry(Long userId, CreateJournalEntryRequest request);

    JournalEntryResponse updateJournalEntry(Long id, UpdateJournalEntryRequest request);

    void deleteJournalEntry(Long id);

    JournalEntryResponse getJournalEntryById(Long id);

    List<JournalEntryResponse> getAllJournalEntries();

    List<JournalEntryResponse> getEntriesByDate(LocalDate entryDate);
}
