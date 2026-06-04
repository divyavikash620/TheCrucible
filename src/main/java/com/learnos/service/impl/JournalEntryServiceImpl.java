package com.learnos.service.impl;

import com.learnos.dto.CreateJournalEntryRequest;
import com.learnos.dto.JournalEntryResponse;
import com.learnos.dto.UpdateJournalEntryRequest;
import com.learnos.entity.JournalEntry;
import com.learnos.entity.User;
import com.learnos.repository.JournalEntryRepository;
import com.learnos.repository.UserRepository;
import com.learnos.service.JournalEntryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;

    public JournalEntryServiceImpl(JournalEntryRepository journalEntryRepository,
            UserRepository userRepository) {
        this.journalEntryRepository = journalEntryRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public JournalEntryResponse createJournalEntry(Long userId, CreateJournalEntryRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + userId));

        JournalEntry entry = new JournalEntry();
        entry.setUser(user);
        entry.setEntryDate(request.entryDate());
        entry.setTitle(request.title());
        entry.setContent(request.content());

        JournalEntry saved = journalEntryRepository.save(entry);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public JournalEntryResponse updateJournalEntry(Long id, UpdateJournalEntryRequest request) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Journal entry not found: " + id));

        if (request.entryDate() != null) {
            entry.setEntryDate(request.entryDate());
        }
        if (request.title() != null) {
            entry.setTitle(request.title());
        }
        if (request.content() != null) {
            entry.setContent(request.content());
        }

        JournalEntry updated = journalEntryRepository.save(entry);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteJournalEntry(Long id) {
        if (!journalEntryRepository.existsById(id)) {
            throw new NoSuchElementException("Journal entry not found: " + id);
        }
        journalEntryRepository.deleteById(id);
    }

    @Override
    public JournalEntryResponse getJournalEntryById(Long id) {
        JournalEntry entry = journalEntryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Journal entry not found: " + id));
        return mapToResponse(entry);
    }

    @Override
    public List<JournalEntryResponse> getAllJournalEntries() {
        List<JournalEntryResponse> responses = new ArrayList<>();
        for (JournalEntry entry : journalEntryRepository.findAll()) {
            responses.add(mapToResponse(entry));
        }
        return responses;
    }

    @Override
    public List<JournalEntryResponse> getEntriesByDate(LocalDate entryDate) {
        List<JournalEntryResponse> responses = new ArrayList<>();
        for (JournalEntry entry : journalEntryRepository.findByEntryDate(entryDate)) {
            responses.add(mapToResponse(entry));
        }
        return responses;
    }

    private JournalEntryResponse mapToResponse(JournalEntry entry) {
        return new JournalEntryResponse(
                entry.getId(),
                entry.getUser().getId(),
                entry.getEntryDate(),
                entry.getTitle(),
                entry.getContent(),
                entry.getCreatedAt(),
                entry.getUpdatedAt()
        );
    }
}
