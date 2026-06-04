package com.learnos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateJournalEntryRequest(
        @NotNull
        LocalDate entryDate,
        String title,
        @NotBlank
        String content
        ) {

}
