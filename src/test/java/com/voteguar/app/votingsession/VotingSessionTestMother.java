package com.voteguar.app.votingsession;

import java.time.LocalDateTime;

public class VotingSessionTestMother {
    public static final Long ID = 1L;
    public static final String TITLE = "Test Voting Session";
    public static final LocalDateTime START_DATE = LocalDateTime.parse("2025-05-28T16:10:00");
    public static final LocalDateTime END_DATE = LocalDateTime.parse("2025-05-28T18:10:00");
    public static final VotingSessionStatus STATUS = VotingSessionStatus.CREATED;
    public static final boolean DELETED = false;

    public static VotingSession complete() {
        VotingSession votingSession = new VotingSession();
        votingSession.setId(ID);
        votingSession.setTitle(TITLE);
        votingSession.setStartDate(START_DATE);
        votingSession.setEndDate(END_DATE);
        votingSession.setStatus(STATUS);
        votingSession.setDeleted(DELETED);
        return votingSession;
    }
}
