package com.voteguar.app.votingsession;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateVotingSessionDTO {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // Validation method
    public void validate() {
        if (startDate != null && endDate != null && !startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}