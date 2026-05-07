package me.cirnoslab.mpi4j.model.response;

import com.google.gson.annotations.SerializedName;
import me.cirnoslab.mpi4j.model.user.PartialUser;

public record LoginResponse(
    @SerializedName("session_id") String sessionID,
    PartialUser user
) {}
