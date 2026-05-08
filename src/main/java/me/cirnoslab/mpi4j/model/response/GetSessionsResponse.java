package me.cirnoslab.mpi4j.model.response;

import me.cirnoslab.mpi4j.model.session.UserSession;

public record GetSessionsResponse(
        UserSession[] sessions
) {
}
