package me.cirnoslab.mpi4j.model.response;

import com.google.gson.annotations.SerializedName;
import me.cirnoslab.mpi4j.model.user.PartialUser;

import java.math.BigInteger;
import java.time.Instant;

public record GetPaymentLinkResponse(
    BigInteger amount,
    PartialUser creator,
    String note,
    @SerializedName("created") Instant createdAt
) {}
