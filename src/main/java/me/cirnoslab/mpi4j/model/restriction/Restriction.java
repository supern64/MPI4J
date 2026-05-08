package me.cirnoslab.mpi4j.model.restriction;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * Represents a restriction on a user
 */
public record Restriction(
    boolean active,
    @Nullable String value,
    @SerializedName("expires_at") Instant expiresAt
) {}
