package com.voteguar.app.votingsession;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotingSessionServiceImplTest {

    @Mock private VotingSessionRepository repository;

    @InjectMocks private VotingSessionServiceImpl service;

    @Test
    void getVotingSessionById_ShouldReturnDTO_WhenSessionExists() {
        VotingSession session = VotingSessionTestMother.complete();
        Long id = 1L;
        session.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(session));

        Optional<VotingSessionDTO> result = service.getVotingSessionById(id);

        assertTrue(result.isPresent());
        assertEquals(session.getTitle(), result.get().getTitle());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void getAllVotingSessions_ShouldReturnPagedDTOs_WhenSessionsExist() {
        VotingSession session = VotingSessionTestMother.complete();
        Long id = 1L;
        session.setId(id);

        Page<VotingSession> page = new PageImpl<>(Collections.singletonList(session));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findByDeletedFalse(pageable)).thenReturn(page);

        Page<VotingSessionDTO> result = service.getAllVotingSessions(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(session.getTitle(), result.getContent().get(0).getTitle());
        verify(repository, times(1)).findByDeletedFalse(pageable);
    }

    @Test
    void deleteVotingSession_ShouldMarkAsDeleted_WhenNotDeleted() {
        VotingSession session = VotingSessionTestMother.complete();
        Long id = 1L;
        session.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(session));

        Optional<VotingSession> result = service.deleteVotingSession(id);

        assertTrue(result.isPresent());
        assertTrue(session.isDeleted());
        verify(repository, times(1)).save(session);
    }

    @Test
    void activateVotingSession_ShouldActivate_WhenInCreatedState() {
        VotingSession session = VotingSessionTestMother.complete();
        Long id = 1L;
        session.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(session));

        Optional<VotingSession> result = service.activateVotingSession(id);

        assertTrue(result.isPresent());
        assertEquals(VotingSessionStatus.ACTIVE, session.getStatus());
        verify(repository, times(1)).save(session);
    }
}
