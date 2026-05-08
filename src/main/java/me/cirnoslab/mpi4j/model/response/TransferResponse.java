package me.cirnoslab.mpi4j.model.response;

import com.google.gson.annotations.SerializedName;
import me.cirnoslab.mpi4j.model.transaction.TransactionStatus;

public record TransferResponse(
        @SerializedName("transaction_id") String transactionID,
        TransactionStatus status
) {
}
