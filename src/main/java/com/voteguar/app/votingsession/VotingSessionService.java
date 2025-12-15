package com.voteguar.app.votingsession;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VotingSessionService {
    VotingSessionDTO createVotingSession(CreateVotingSessionDTO createVotingSessionDTO);

    Optional<VotingSessionDTO> getVotingSessionById(Long id);

    Page<VotingSessionDTO> getAllVotingSessions(Pageable pageable);

    Optional<VotingSession> activateVotingSession(Long id);

    Optional<VotingSession> deleteVotingSession(Long id);
}
