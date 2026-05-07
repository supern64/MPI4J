package me.cirnoslab.mpi4j.client.user;

import com.google.gson.JsonObject;
import me.cirnoslab.mpi4j.model.response.GetTransactionListResponse;
import me.cirnoslab.mpi4j.model.response.Response;
import me.cirnoslab.mpi4j.model.response.TransferResponse;
import me.cirnoslab.mpi4j.model.transaction.Transaction;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * Client for transaction endpoints.
 */
public class TransactionClient {
    private final UserClientContext c;

    public TransactionClient(UserClientContext clientContext) {
        this.c = clientContext;
    }

    /**
     * Transfer funds to another user.
     * @param recipient Recipient username
     * @param amount Amount in paisa
     * @param note Optional transfer note
     * @return the transaction ID and status
     */
    public CompletableFuture<Response<TransferResponse>> transfer(String recipient, BigInteger amount, @Nullable String note) {
        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("recipient", recipient);
        reqBody.addProperty("amount", amount);
        if (note != null) { reqBody.addProperty("note", note); }

        return c.postPrivileged("/transaction/transfer", reqBody, TransferResponse.class);
    }

    /**
     * Transfer funds to another user.
     * @param recipient Recipient username
     * @param amount Amount in paisa
     * @return the transaction ID and status
     */
    public CompletableFuture<Response<TransferResponse>> transfer(String recipient, BigInteger amount) {
        return transfer(recipient, amount, null);
    }

    /**
     * Get details of a specific transaction.
     * @param id Transaction ID (TXN-xxx)
     * @return the transaction
     */
    public CompletableFuture<Response<Transaction>> get(String id) {
        return c.getPrivileged("/transaction/get?id=" + URLEncoder.encode(id, StandardCharsets.UTF_8), Transaction.class);
    }

    /**
     * Get transaction history for the authenticated user.
     * @return the list of transactions
     */
    public CompletableFuture<Response<GetTransactionListResponse>> getList() {
        return c.getPrivileged("/transaction/list", GetTransactionListResponse.class);
    }
}
