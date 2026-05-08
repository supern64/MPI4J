package me.cirnoslab.mpi4j.model.session;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the status a checkout status can have.
 */
public enum CheckoutSessionStatus {
    /**
     * Waiting for the customer to pay. Checkout session is still valid.
     */
    @SerializedName("open") OPEN,
    /**
     * Customer went through checkout and a transaction was <b>CREATED</b> (emphasized because it may be pending or any other state, you may not have gotten your funds yet). Checkout session is closed by this point.
     */
    @SerializedName("complete") COMPLETE,
    /**
     * Checkout session expired before the customer paid in the 30-minute window.
     */
    @SerializedName("expired") EXPIRED
}
