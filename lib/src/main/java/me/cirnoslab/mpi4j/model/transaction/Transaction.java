package me.cirnoslab.mpi4j.model.transaction;

import com.google.gson.annotations.SerializedName;
import me.cirnoslab.mpi4j.model.user.PartialUser;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.time.Instant;

/**
 * Represents a transaction. Amounts are in paisa.
 */
public record Transaction(
    int id,
    @SerializedName("transaction_id") String transactionID,
    PartialUser sender,
    PartialUser recipient,
    BigInteger amount,
    @Nullable String note,
    TransactionStatus status,
    @SerializedName("created") Instant createdAt
) {}
