package me.cirnoslab.mpi4j.model.response;

import com.google.gson.annotations.SerializedName;

public record ClaimPaymentLinkResponse(
    @SerializedName("transaction_id") String transactionID
) {}
