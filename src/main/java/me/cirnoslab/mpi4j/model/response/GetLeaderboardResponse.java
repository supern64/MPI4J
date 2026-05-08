package me.cirnoslab.mpi4j.model.response;

import me.cirnoslab.mpi4j.model.user.LeaderboardUser;

public record GetLeaderboardResponse(
        LeaderboardUser[] leaderboard
) {
}
