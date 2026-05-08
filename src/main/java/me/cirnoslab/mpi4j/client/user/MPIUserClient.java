package me.cirnoslab.mpi4j.client.user;

import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import me.cirnoslab.mpi4j.model.APIError;
import me.cirnoslab.mpi4j.model.adapters.APIErrorAdapter;
import me.cirnoslab.mpi4j.model.adapters.InstantAdapter;
import me.cirnoslab.mpi4j.model.adapters.LocalDateAdapter;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A user client for MyPayIndia. Users should use this class as the main entrypoint.
 * All currency values are in paisa (1/100th of a rupee).
 */
public class MPIUserClient {
    private final UserClientContext c;
    private final UserClient userClient;
    private final AuthClient authClient;
    private final InfoClient infoClient;
    private final TransactionClient transactionClient;
    private final PaymentLinkClient paymentLinkClient;

    /**
     * @param useOTP     Whether the account will use OTP
     * @param useStaging Whether the client will use the staging server
     */
    public MPIUserClient(boolean useOTP, boolean useStaging) {
        CookieManager cookie = new CookieManager();

        this.c = new UserClientContext(
                HttpClient.newBuilder().cookieHandler(cookie).build(),
                new GsonBuilder()
                        .registerTypeAdapter(Instant.class, new InstantAdapter())
                        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                        .registerTypeAdapter(APIError.class, new APIErrorAdapter())
                        .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
                        .create(),
                useOTP,
                useStaging
        );

        this.userClient = new UserClient(c);
        this.authClient = new AuthClient(c);
        this.infoClient = new InfoClient(c);
        this.transactionClient = new TransactionClient(c);
        this.paymentLinkClient = new PaymentLinkClient(c);
    }

    public MPIUserClient() {
        this(false, false);
    }

    /**
     * @return the UserClientContext being used
     */
    public UserClientContext context() {
        return c;
    }

    /**
     * @return the AuthClient for authentication endpoints
     */
    public AuthClient auth() {
        return authClient;
    }

    /**
     * @return the UserClient for user endpoints
     */
    public UserClient user() {
        return userClient;
    }

    /**
     * @return the InfoClient for public information endpoints
     */
    public InfoClient info() {
        return infoClient;
    }

    /**
     * @return the PaymentLinkClient for payment link endpoints
     */
    public PaymentLinkClient paymentLink() {
        return paymentLinkClient;
    }

    /**
     * @return the TransactionClient for transaction endpoints
     */
    public TransactionClient transaction() {
        return transactionClient;
    }
}
