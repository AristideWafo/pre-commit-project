package com.voteguar.app.votingsession;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table
public class VotingSessionDTO {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private VotingSessionStatus status;
    private String description;

    public static VotingSessionDTO toDTO(VotingSession votingSession) {
        VotingSessionDTO dto = new VotingSessionDTO();
        dto.setId(votingSession.getId());
        dto.setTitle(votingSession.getTitle());
        dto.setStartDate(votingSession.getStartDate());
        dto.setEndDate(votingSession.getEndDate());
        dto.setStatus(votingSession.getStatus()); // Direct enum assignment
        dto.setDescription(votingSession.getStatus() != null ? votingSession.getStatus().getDescription() : "No description");
        return dto;
    }

}
