package me.cirnoslab.mpi4j.client.user;

import com.google.gson.JsonObject;
import me.cirnoslab.mpi4j.model.response.LoginResponse;
import me.cirnoslab.mpi4j.model.response.Response;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Client for authentication endpoints.
 */
public class AuthClient {
    private final UserClientContext c;

    public AuthClient(UserClientContext clientContext) {
        this.c = clientContext;
    }

    /**
     * Authenticate and receive a session ID.
     *
     * @param user     Username or email address
     * @param password Account password
     * @param otp      TOTP authenticator code (required if 2FA is enabled)
     * @return the session ID and user
     */
    public CompletableFuture<Response<LoginResponse>> login(String user, String password, @Nullable String otp) {
        if (c.useOTP() && otp == null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("An OTP must be provided.")
            );
        } else if (!c.useOTP() && otp != null) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("An OTP cannot be provided when OTP is disabled.")
            );
        }

        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("username", user);
        reqBody.addProperty("password", password);

        if (c.useOTP()) {
            reqBody.addProperty("totp_code", otp);
        }

        CompletableFuture<Response<LoginResponse>> res = c.post("/auth/login", reqBody, LoginResponse.class);
        res.thenAccept(r -> {
            if (r.success()) {
                c.recordExpiry();
                c.setSessionID(r.data().sessionID());
            }
        });
        return res;
    }

    /**
     * Authenticate and receive a session ID.
     *
     * @param user     Username or email address
     * @param password Account password
     * @return the session ID and user
     */
    public CompletableFuture<Response<LoginResponse>> login(String user, String password) {
        return login(user, password, null);
    }
}
