package com.voteguar.app.votingsession;

import java.util.Collections;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotingSessionController.class)
class VotingSessionControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private VotingSessionService votingSessionService;

    @Test
    void getAllSessions_ShouldReturnOk_WhenSessionsExist() throws Exception {
        VotingSessionDTO dto = VotingSessionDTOTestMother.complete();
        Page<VotingSessionDTO> page =
                new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);
        when(votingSessionService.getAllVotingSessions(any())).thenReturn(page);

        mockMvc.perform(get("/sessions").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    void getSessionById_ShouldReturnOk_WhenSessionExists() throws Exception {
        VotingSessionDTO dto = VotingSessionDTOTestMother.complete();
        when(votingSessionService.getVotingSessionById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/sessions/1"))
                .andExpect(status().isOk())
                .andDo(
                        result -> {
                            System.out.println(
                                    "GET Response body: '"
                                            + result.getResponse().getContentAsString()
                                            + "'");
                            System.out.println(
                                    "GET Content type: " + result.getResponse().getContentType());
                        });
    }

    @Test
    void createSession_ShouldReturnCreated_WhenValidDTO() throws Exception {
        // Arrange
        CreateVotingSessionDTO createDTO = CreateVotingSessionDTOTestMother.complete();
        VotingSession createdSession = new VotingSession();
        createdSession.setId(1L);
        createdSession.setTitle(createDTO.getTitle());
        createdSession.setStartDate(createDTO.getStartDate());
        createdSession.setEndDate(createDTO.getEndDate());
        createdSession.setStatus(VotingSessionStatus.CREATED);
        createdSession.setDeleted(false);

        VotingSessionDTO expectedDTO = VotingSessionDTO.toDTO(createdSession);
        when(votingSessionService.createVotingSession(any(CreateVotingSessionDTO.class)))
                .thenReturn(expectedDTO);
        String jsonRequest = objectMapper.writeValueAsString(createDTO);

        // Act & Assert
        mockMvc.perform(
                        post("/sessions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedDTO)));
    }

    @Test
    void deleteSession_ShouldReturnNoContent_WhenSessionExists() throws Exception {
        // Arrange
        Long id = 1L;
        VotingSession session = new VotingSession();
        session.setId(id);
        when(votingSessionService.deleteVotingSession(id)).thenReturn(Optional.of(session));

        // Act & Assert
        mockMvc.perform(delete("/sessions/" + id)).andExpect(status().isNoContent());
    }
}
