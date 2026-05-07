package me.cirnoslab.mpi4j.model.user;

import java.math.BigInteger;

/**
 * Represents a user on the leaderboard.
 */
public record LeaderboardUser(
    String username,
    BigInteger balance
) {}
