package me.cirnoslab.mpi4j.model.user;

import me.cirnoslab.mpi4j.client.user.PaymentLinkClient;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a partial user, only containing username.<br>
 * id is null with {@link PaymentLinkClient#get(String)}
 */
public record PartialUser(
    @Nullable Integer id,
    String username
) {}
