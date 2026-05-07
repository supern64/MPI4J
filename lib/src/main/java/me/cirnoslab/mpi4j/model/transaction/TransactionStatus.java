package me.cirnoslab.mpi4j.model.transaction;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the status a transaction can have.
 */
public enum TransactionStatus {
    /**
     * Payment completed successfully. Funds are now in your account.
     */
    @SerializedName("confirmed") CONFIRMED,
    /**
     * Payment is under manual review (large transactions). Funds are not in your account yet.
     */
    @SerializedName("pending") PENDING,
    /**
     * Payment was rejected during review. Funds were returned to the sender.
     */
    @SerializedName("rejected") REJECTED,
    /**
     * Payment was reversed by MyPayIndia staff after being confirmed. Funds have been returned to the sender.
     */
    @SerializedName("reversed") REVERSED
}
