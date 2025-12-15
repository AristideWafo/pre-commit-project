package com.voteguar.app.votingsession;

import java.util.Optional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotingSessionServiceImpl implements VotingSessionService {
    private final VotingSessionRepository votingSessionRepository;

    @Override
    @Transactional
    public VotingSessionDTO createVotingSession(CreateVotingSessionDTO createDTO) {
        log.info("Creating new voting session with title: {}", createDTO.getTitle());

        // Validate input
        createDTO.validate();

        // Convert DTO to Entity
        VotingSession votingSession = new VotingSession();
        votingSession.setTitle(createDTO.getTitle());
        votingSession.setStartDate(createDTO.getStartDate());
        votingSession.setEndDate(createDTO.getEndDate());
        votingSession.setStatus(VotingSessionStatus.CREATED); // Service sets this
        votingSession.setDeleted(false); // Service sets this

        VotingSession savedSession = votingSessionRepository.save(votingSession);
        log.info("Successfully created voting session with id: {}", savedSession.getId());

        return VotingSessionDTO.toDTO(savedSession);
    }

    @Override
    public Optional<VotingSessionDTO> getVotingSessionById(Long id) {
        log.debug("Fetching voting session with id: {}", id);
        return votingSessionRepository
                .findById(id)
                .filter(session -> !session.isDeleted()) // Don't return deleted sessions
                .map(VotingSessionDTO::toDTO);
    }

    @Override
    public Page<VotingSessionDTO> getAllVotingSessions(Pageable pageable) {
        log.debug("Fetching all voting sessions with pagination: {}", pageable);
        return votingSessionRepository.findByDeletedFalse(pageable).map(VotingSessionDTO::toDTO);
    }

    @Override
    @Transactional
    public Optional<VotingSession> deleteVotingSession(Long id) {
        log.info("Deleting voting session with id: {}", id);
        return votingSessionRepository
                .findById(id)
                .map(
                        votingSession -> {
                            if (!votingSession.isDeleted()) {
                                votingSession.setDeleted(true);
                                votingSessionRepository.save(votingSession);
                                return votingSession;
                            }
                            return null; // Return null if already deleted, resulting in
                            // Optional.empty()
                        });
    }

    @Override
    @Transactional
    public Optional<VotingSession> activateVotingSession(Long id) {
        log.info("Activating voting session with id: {}", id);

        return votingSessionRepository
                .findById(id)
                .map(
                        votingSession -> {
                            /*LocalDateTime now = LocalDateTime.now();
                            if (votingSession.getStartDate().isAfter(now)) {
                                throw new IllegalStateException("Cannot activate session before start date");
                            }
                            if (votingSession.getEndDate().isBefore(now)) {
                                throw new IllegalStateException("Cannot activate session after end date");
                            }*/

                            votingSession.setStatus(VotingSessionStatus.ACTIVE);
                            votingSessionRepository.save(votingSession);

                            log.info("Successfully activated voting session with id: {}", id);
                            return votingSession;
                        });
    }
}
