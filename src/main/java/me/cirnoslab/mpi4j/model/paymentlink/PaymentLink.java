package me.cirnoslab.mpi4j.model.paymentlink;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.time.Instant;

/**
 * Represents a payment link.
 */
public record PaymentLink(
        int id,
        String token,
        BigInteger amount,
        String note,
        @SerializedName("created") Instant createdAt,
        PaymentLinkStatus status,
        String url
) {
}
