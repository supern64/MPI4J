package me.cirnoslab.mpi4j.client.user;

import me.cirnoslab.mpi4j.model.response.GetLeaderboardResponse;
import me.cirnoslab.mpi4j.model.response.GetTeamResponse;
import me.cirnoslab.mpi4j.model.response.Response;

import java.util.concurrent.CompletableFuture;

/**
 * Client for public information endpoints.
 */
public class InfoClient {
    private final UserClientContext c;

    public InfoClient(UserClientContext clientContext) {
        this.c = clientContext;
    }

    /**
     * Get the top 10 users by balance.
     * @return the users
     */
    public CompletableFuture<Response<GetLeaderboardResponse>> getLeaderboard() {
        return c.get("/info/leaderboard", GetLeaderboardResponse.class);
    }

    /**
     * Get the list of MyPayIndia team members.
     * @return the list of members
     */
    public CompletableFuture<Response<GetTeamResponse>> getTeam() {
        return c.get("/info/team", GetTeamResponse.class);
    }
}
