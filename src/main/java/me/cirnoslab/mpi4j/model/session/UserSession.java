package me.cirnoslab.mpi4j.model.session;

import com.google.gson.annotations.SerializedName;
import me.cirnoslab.mpi4j.model.response.LoginResponse;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * Represents a user session
 *
 * @param id session identifier, distinct from {@link LoginResponse#sessionID()}
 */
public record UserSession(
        String id,
        @SerializedName("device_info") String deviceInfo,
        String ip,
        @SerializedName("created_at") Instant createdAt,
        @SerializedName("last_active") Instant lastActive,
        @SerializedName("invalidated") boolean isInvalidated,
        @Nullable @SerializedName("invalidated_at") Instant invalidatedAt,
        @SerializedName("current") boolean isCurrent
) {
}
