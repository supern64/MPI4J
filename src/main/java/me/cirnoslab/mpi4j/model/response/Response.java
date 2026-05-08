package me.cirnoslab.mpi4j.model.response;

import me.cirnoslab.mpi4j.model.APIError;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a response from the server.
 * Should be used with the other *Response classes to form a full reply.
 */
public record Response<T>(
        boolean success,
        @Nullable APIError error,
        T data
) {
}