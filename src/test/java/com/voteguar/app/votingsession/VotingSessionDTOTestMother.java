package com.voteguar.app.votingsession;

import java.time.LocalDateTime;

public class VotingSessionDTOTestMother {
    public static VotingSessionDTO complete() {
        VotingSessionDTO dto = new VotingSessionDTO();
        dto.setId(1L);
        dto.setTitle("Test Voting Session");
        dto.setStartDate(LocalDateTime.parse("2025-05-28T16:10:00"));
        dto.setEndDate(LocalDateTime.parse("2025-05-28T18:10:00"));
        dto.setStatus(VotingSessionStatus.CREATED);
        dto.setDescription(VotingSessionStatus.CREATED.getDescription());
        return dto;
    }
}
