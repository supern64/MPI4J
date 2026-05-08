package me.cirnoslab.mpi4j.client.user;

import com.google.gson.JsonObject;
import me.cirnoslab.mpi4j.model.response.*;

import java.util.concurrent.CompletableFuture;

/**
 * Client for user endpoints.
 */
public class UserClient {
    private final UserClientContext c;

    public UserClient(UserClientContext clientContext) {
        this.c = clientContext;
    }

    /**
     * Get profile and balance information for the authenticated user.
     * @return the user information
     */
    public CompletableFuture<Response<GetInfoResponse>> getInfo() {
        return c.getPrivileged("/user/info", GetInfoResponse.class);
    }

    /**
     * Get active restrictions on the authenticated user's account.
     * @return the restrictions
     */
    public CompletableFuture<Response<GetRestrictionsResponse>> getRestrictions() {
        return c.getPrivileged("/user/restrictions", GetRestrictionsResponse.class);
    }

    /**
     * List active and recently invalidated sessions for the authenticated user.
     * @return the sessions
     */
    public CompletableFuture<Response<GetSessionsResponse>> getSessions() {
        return c.getPrivileged("/user/session/list", GetSessionsResponse.class);
    }

    /**
     * Invalidate (log out) a specific session.
     * Automatically invalidates this client if the session ID for this session is the parameter.
     * @param sessionId The session ID to invalidate
     */
    public CompletableFuture<Response<Void>> invalidateSession(String sessionId) {
        JsonObject reqBody = new JsonObject();
        reqBody.addProperty("session_id", sessionId);

        return c.postPrivileged("/user/session/invalidate", reqBody, Void.class);
    }

    /**
     * Send a verification email to the authenticated user's email address.
     */
    public CompletableFuture<Response<Void>> verifyEmail() {
        return c.postPrivileged("/user/verify-email", Void.class);
    }
}
