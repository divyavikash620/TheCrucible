package com.learnos.dto;

import java.time.LocalDate;

public record UpdateJournalEntryRequest(
        LocalDate entryDate,
        String title,
        String content
        ) {

}
