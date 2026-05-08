package me.cirnoslab.mpi4j.model.response;

import me.cirnoslab.mpi4j.model.restriction.Restrictions;

public record GetRestrictionsResponse(
        Restrictions restrictions
) {
}
