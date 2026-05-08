package me.cirnoslab.mpi4j.client.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.cirnoslab.mpi4j.model.response.Response;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;

import static me.cirnoslab.mpi4j.client.Constants.BASE_URL;
import static me.cirnoslab.mpi4j.client.Constants.STAGING_URL;

/**
 * Context holder for MPIUserClient.
 */
public class UserClientContext {
    private final HttpClient http;
    private final Gson gson;
    private final boolean useStaging;
    private final boolean useOTP;

    private Instant sessionExpiry = null;
    private String sessionID = null;

    public UserClientContext(HttpClient http, Gson gson, boolean useOTP, boolean useStaging) {
        this.http = http;
        this.gson = gson;
        this.useOTP = useOTP;
        this.useStaging = useStaging;
    }

    /**
     * @return the GSON instance
     */
    public Gson gson() {
        return gson;
    }

    /**
     * @return the base URL for the current API
     */
    public String baseURL() {
        return useStaging ? STAGING_URL : BASE_URL;
    }

    /**
     * @return whether this client uses OTP
     */
    public boolean useOTP() {
        return useOTP;
    }

    /**
     * @return whether the session exists and hasn't expired (30 days after any request)
     */
    public boolean isLoggedIn() {
        return sessionExpiry != null && Instant.now().isBefore(sessionExpiry);
    }

    void recordExpiry() {
        sessionExpiry = Instant.now().plus(30, ChronoUnit.DAYS);
    }

    void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    String sessionID() {
        return this.sessionID;
    }

    void destroySession() {
        sessionExpiry = null;
        sessionID = null;
    }

    // helper functions for requests
    <T> CompletableFuture<Response<T>> get(String path, Class<T> inner) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL() + path))
                .build();

        Type rType = TypeToken.getParameterized(Response.class, inner).getType();
        return this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> this.gson.fromJson(r.body(), rType));
    }

    <T> CompletableFuture<Response<T>> getPrivileged(String path, Class<T> inner) {
        if (!this.isLoggedIn()) return CompletableFuture.failedFuture(new IllegalStateException("Client is not logged in."));
        CompletableFuture<Response<T>> res = get(path, inner);
        res.thenAccept(a -> {
            if (a.success()) recordExpiry();
        });
        return res;
    }

    <T> CompletableFuture<Response<T>> post(String path, Object body, Class<T> inner) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL() + path))
                .POST(body != null
                        ? HttpRequest.BodyPublishers.ofString(this.gson.toJson(body))
                        : HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .build();

        Type rType = TypeToken.getParameterized(Response.class, inner).getType();
        return this.http.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(r -> this.gson.fromJson(r.body(), rType));
    }

    <T> CompletableFuture<Response<T>> postPrivileged(String path, Object body, Class<T> inner) {
        if (!this.isLoggedIn()) return CompletableFuture.failedFuture(new IllegalStateException("Client is not logged in."));
        CompletableFuture<Response<T>> res = post(path, body, inner);
        res.thenAccept(a -> {
            if (a.success()) recordExpiry();
        });
        return res;
    }

    <T> CompletableFuture<Response<T>> post(String path, Class<T> inner) {
        return post(path, null, inner);
    }

    <T> CompletableFuture<Response<T>> postPrivileged(String path, Class<T> inner) {
        return postPrivileged(path, null, inner);
    }
}
