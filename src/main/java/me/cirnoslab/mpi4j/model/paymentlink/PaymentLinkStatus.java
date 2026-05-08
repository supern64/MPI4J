package me.cirnoslab.mpi4j.model.paymentlink;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the status a payment link can have.
 */
public enum PaymentLinkStatus {
    @SerializedName("active") ACTIVE,
    @SerializedName("claimed") CLAIMED,
    @SerializedName("refunded") REFUNDED
}
