package com.learnos.dto;

import java.time.Instant;
import java.time.LocalDate;

public record JournalEntryResponse(
        Long id,
        Long userId,
        LocalDate entryDate,
        String title,
        String content,
        Instant createdAt,
        Instant updatedAt
        ) {

}
