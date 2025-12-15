package com.voteguar.app.votingsession;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VotingSessionStatus {
    CREATED("The session has been created but not yet activated"),
    ACTIVE("The session is currently open for voting"),
    CLOSED("The session is closed and voting has ended");

    private final String description;
}
