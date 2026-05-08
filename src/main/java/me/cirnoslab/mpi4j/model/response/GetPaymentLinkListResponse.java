package me.cirnoslab.mpi4j.model.response;

import me.cirnoslab.mpi4j.model.paymentlink.PaymentLink;

public record GetPaymentLinkListResponse(
        PaymentLink[] links
) {
}
