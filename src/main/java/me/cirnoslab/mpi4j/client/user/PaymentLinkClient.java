package me.cirnoslab.mpi4j.client.user;

import com.google.gson.JsonObject;
import me.cirnoslab.mpi4j.model.response.*;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * Client for payment link endpoints.
 */
public class PaymentLinkClient {
    private final UserClientContext c;

    public PaymentLinkClient(UserClientContext clientContext) {
        this.c = clientContext;
    }

    /**
     * Create a payment link. Deducts the amount from your balance immediately.
     *
     * @param amount Amount in paisa
     * @param note   Optional note
     * @return the resulting payment link
     */
    public CompletableFuture<Response<CreatePaymentLinkResponse>> create(BigInteger amount, @Nullable String note) {
        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("amount", amount);
        if (note != null) {
            reqBody.addProperty("note", note);
        }

        return c.postPrivileged("/payment-link/create", reqBody, CreatePaymentLinkResponse.class);
    }

    /**
     * Create a payment link. Deducts the amount from your balance immediately.
     *
     * @param amount Amount in paisa
     * @return the resulting payment link
     */
    public CompletableFuture<Response<CreatePaymentLinkResponse>> create(BigInteger amount) {
        return create(amount, null);
    }

    /**
     * List all payment links created by the authenticated user.
     *
     * @return the list of payment links
     */
    public CompletableFuture<Response<GetPaymentLinkListResponse>> getList() {
        return c.getPrivileged("/payment-link/list", GetPaymentLinkListResponse.class);
    }

    /**
     * Get details of a payment link by its token.
     * User details do not include database ID.
     *
     * @param token Payment link token
     * @return the payment link
     */
    public CompletableFuture<Response<GetPaymentLinkResponse>> get(String token) {
        return c.get("/payment-link/get?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8), GetPaymentLinkResponse.class);
    }

    /**
     * Claim a payment link and receive the funds.
     *
     * @param token Payment link token
     * @return the transaction ID
     */
    public CompletableFuture<Response<ClaimPaymentLinkResponse>> claim(String token) {
        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("token", token);

        return c.postPrivileged("/payment-link/claim", reqBody, ClaimPaymentLinkResponse.class);
    }

    /**
     * Cancel your own payment link and refund the amount to your balance.
     *
     * @param token Payment link token
     */
    public CompletableFuture<Response<Void>> cancel(String token) {
        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("token", token);

        return c.postPrivileged("/payment-link/cancel", reqBody, Void.class);
    }
}
