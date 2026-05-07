package me.cirnoslab.mpi4j.model.restriction;

import org.jetbrains.annotations.Nullable;

/**
 * Represents the set of restrictions a user can have.
 * @param frozen Whether the account is frozen
 * @param eightysixed Whether the account is banned from Investment Opportunities
 */
public record Restrictions(
    @Nullable Restriction frozen,
    @Nullable Restriction eightysixed
) {}
