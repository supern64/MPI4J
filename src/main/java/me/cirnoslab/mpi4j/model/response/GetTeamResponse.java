package me.cirnoslab.mpi4j.model.response;

import me.cirnoslab.mpi4j.model.misc.TeamMember;

public record GetTeamResponse(
        TeamMember[] team
) {
}
