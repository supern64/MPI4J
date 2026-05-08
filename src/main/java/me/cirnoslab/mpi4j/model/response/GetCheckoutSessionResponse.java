package me.cirnoslab.mpi4j.model.response;

import com.google.gson.annotations.SerializedName;
import me.cirnoslab.mpi4j.model.session.CheckoutSessionStatus;
import me.cirnoslab.mpi4j.model.transaction.TransactionStatus;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.time.Instant;

public record GetCheckoutSessionResponse(
        @SerializedName("session_id") String sessionID,
        CheckoutSessionStatus status,
        BigInteger amount,
        @SerializedName("order_id") String orderID,
        @SerializedName("transaction_id") String transactionID,
        @SerializedName("transaction_status") TransactionStatus transactionStatus,
        @SerializedName("sender_username") String sender,
        @SerializedName("created_at") Instant createdAt,
        @SerializedName("expires_at") Instant expiresAt,
        @Nullable @SerializedName("completed_at") Instant completedAt
) {
}
