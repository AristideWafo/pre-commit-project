package com.voteguar.app.votingsession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
    Page<VotingSession> findByDeletedFalse(Pageable pageable);
}
