package me.cirnoslab.mpi4j.model.response;

import java.math.BigInteger;

public record CreatePaymentLinkResponse(
        int id,
        String token,
        BigInteger amount,
        String note,
        String url
) {
}
