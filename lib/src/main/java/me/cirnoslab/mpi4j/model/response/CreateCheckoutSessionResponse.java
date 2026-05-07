package me.cirnoslab.mpi4j.model.response;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public record CreateCheckoutSessionResponse (
    @SerializedName("session_id") String sessionID,
    @SerializedName("checkout_url") String checkoutURL,
    @SerializedName("expires_at") Instant expiresAt
) {}
