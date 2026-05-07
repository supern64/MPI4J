package me.cirnoslab.mpi4j.model.response;

import me.cirnoslab.mpi4j.model.transaction.Transaction;

public record GetTransactionListResponse(
    Transaction[] transactions
) {}
