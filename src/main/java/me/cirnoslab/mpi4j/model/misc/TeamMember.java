package me.cirnoslab.mpi4j.model.misc;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents a team member.
 */
public record TeamMember(
        String name,
        String role,
        @SerializedName("avatar") String avatarURL,
        @SerializedName("joined") Instant joinedAt,
        ContactCollection socials
) {
}
