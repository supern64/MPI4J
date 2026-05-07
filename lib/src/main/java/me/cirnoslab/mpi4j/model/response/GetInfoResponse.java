package me.cirnoslab.mpi4j.model.response;

import com.google.gson.annotations.SerializedName;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;

public record GetInfoResponse(
    int id,
    String username,
    @SerializedName("first_name") String firstName,
    @SerializedName("last_name") String lastName,
    String email,
    @SerializedName("date_of_birth") LocalDate dateOfBirthOn,
    @SerializedName("created") Instant createdAt,
    BigInteger balance,
    String role,
    @SerializedName("mfa_enabled") boolean isMFA
) {}