package com.voteguar.app.votingsession;

import java.time.LocalDateTime;

public class CreateVotingSessionDTOTestMother {
    public static CreateVotingSessionDTO complete() {
        CreateVotingSessionDTO dto = new CreateVotingSessionDTO();
        dto.setTitle("Test Voting Session");
        dto.setStartDate(LocalDateTime.parse("2025-05-28T16:10:00"));
        dto.setEndDate(LocalDateTime.parse("2025-05-28T18:10:00"));
        return dto;
    }
}
