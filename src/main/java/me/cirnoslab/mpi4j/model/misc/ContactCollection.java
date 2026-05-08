package me.cirnoslab.mpi4j.model.misc;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the social media contacts for a team member.
 */
public record ContactCollection(
        @SerializedName("Website") @Nullable String website,
        @SerializedName("Twitter") @Nullable String twitter,
        @SerializedName("GitHub") @Nullable String github,
        @SerializedName("YouTube") @Nullable String youtube
) {
}
