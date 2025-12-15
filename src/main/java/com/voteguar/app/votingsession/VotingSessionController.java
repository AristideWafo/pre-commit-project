package com.voteguar.app.votingsession;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class VotingSessionController {
    private final VotingSessionService votingSessionService;

    @PostMapping
    public ResponseEntity<VotingSessionDTO> createSession(@RequestBody CreateVotingSessionDTO createDTO) {
        VotingSessionDTO createdSession = votingSessionService.createVotingSession(createDTO);
        // ADD THESE DEBUG LINES
        System.out.println("=== CONTROLLER DEBUG ===");
        System.out.println("Service returned: " + createdSession);
        System.out.println("Service returned ID: " + (createdSession != null ? createdSession.getId() : "NULL"));
        System.out.println("About to return ResponseEntity with: " + createdSession);

        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VotingSessionDTO> getSessionById(@PathVariable Long id) {
        VotingSessionDTO sessionDTO = votingSessionService.getVotingSessionById(id)
                .orElseThrow(() -> new RuntimeException("VotingSession not found"));
        return new ResponseEntity<>(sessionDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<VotingSessionDTO>> getAllSessions(Pageable pageable) {
        Page<VotingSessionDTO> page = votingSessionService.getAllVotingSessions(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        votingSessionService.deleteVotingSession(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}