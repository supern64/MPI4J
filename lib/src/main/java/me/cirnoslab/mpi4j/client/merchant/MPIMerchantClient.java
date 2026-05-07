package me.cirnoslab.mpi4j.client.merchant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import me.cirnoslab.mpi4j.model.APIError;
import me.cirnoslab.mpi4j.model.adapters.APIErrorAdapter;
import me.cirnoslab.mpi4j.model.adapters.InstantAdapter;
import me.cirnoslab.mpi4j.model.response.CreateCheckoutSessionResponse;
import me.cirnoslab.mpi4j.model.response.GetCheckoutSessionResponse;
import me.cirnoslab.mpi4j.model.response.Response;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static me.cirnoslab.mpi4j.client.Constants.BASE_URL;
import static me.cirnoslab.mpi4j.client.Constants.STAGING_URL;

/**
 * A merchant client for MyPayIndia. A valid merchant key is required.<br/>
 * All currency values are in paisa (1/100th of a rupee).
 * <p>
 * The payment flow:
 * <ol>
 *     <li>call {@link MPIMerchantClient#createCheckoutSession(BigInteger, String, String)} with your desired amount and return URL</li>
 *     <li>redirect the customer to {@link CreateCheckoutSessionResponse#checkoutURL()}</li>
 *     <li>the customer logs into MyPayIndia, reviews payment details, and pays</li>
 *     <li>MyPayIndia redirects the customer to your return URL with <code>session_id</code> and <code>status</code> parameters</li>
 *     <li>verify these parameters with {@link MPIMerchantClient#getCheckoutSession(String)}. Do not trust the parameters from the return URL.</li>
 * </ol>
 *
 */
public class MPIMerchantClient {
    private final String secretKey;
    private final HttpClient http;
    private final Gson gson;

    private final boolean useStaging;

    /**
     * Create a merchant client.
     * @param secretKey The secret merchant key
     * @param useStaging Whether to use the staging server
     */
    public MPIMerchantClient(String secretKey, boolean useStaging) {
        this.secretKey = secretKey;

        this.http = HttpClient.newHttpClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .registerTypeAdapter(APIError.class, new APIErrorAdapter())
                .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
                .create();

        this.useStaging = useStaging;
    }

    public String baseURL() {
        return useStaging ? STAGING_URL : BASE_URL;
    }

    /**
     * Creates a checkout session.
     * @param amount Amount you want the customer to pay in paisa (0 < amount < 1 000 000 00)
     * @param returnURL URL to redirect the customer to after payment, must be one of your allowed domains
     * @param orderID Your order reference, a maximum of 64 characters are allowed
     * @return the session ID, checkout URL and expiry time
     */
    public CompletableFuture<Response<CreateCheckoutSessionResponse>> createCheckoutSession(BigInteger amount, String returnURL, @Nullable String orderID) {
        if (orderID != null && orderID.length() > 64)
            return CompletableFuture.failedFuture(new IllegalArgumentException("Order ID length may not exceed 64."));
        if (amount.compareTo(BigInteger.ONE) < 0 || amount.compareTo(new BigInteger("100000000")) > 0)
            return CompletableFuture.failedFuture(new IllegalArgumentException("Amount must be > 0 and < 1 000 000 00."));

        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("amount", amount);
        reqBody.addProperty("return_url", returnURL);
        if (orderID != null) { reqBody.addProperty("order_id", orderID); }

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL() + "/pay/create"))
                .POST(HttpRequest.BodyPublishers.ofString(this.gson.toJson(reqBody)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.secretKey)
                .build();

        Type rType = new TypeToken<Response<CreateCheckoutSessionResponse>>() {}.getType();
        return this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> this.gson.fromJson(r.body(), rType));
    }

    /**
     * Creates a checkout session.
     * @param amount Amount you want the customer to pay in paisa (0 < amount < 100000000)
     * @param returnURL URL to redirect the customer to after payment, must be one of your allowed domains
     * @return the session ID, checkout URL and expiry time
     */
    public CompletableFuture<Response<CreateCheckoutSessionResponse>> createCheckoutSession(BigInteger amount, String returnURL) {
        return createCheckoutSession(amount, returnURL, null);
    }

    public CompletableFuture<Response<GetCheckoutSessionResponse>> getCheckoutSession(String sessionID) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL() + "/pay/status?session_id=" + sessionID))
                .header("Authorization", "Bearer " + this.secretKey)
                .build();

        Type rType = new TypeToken<Response<GetCheckoutSessionResponse>>() {}.getType();
        return this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> this.gson.fromJson(r.body(), rType));
    }
}
