package com.paoduropay.backend.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record Transaction(
        String description,
        BigDecimal amount,
        LocalDate date,
        Wallet wallet,
        TransactionType type
) {

    public Transaction {
        Objects.requireNonNull(description, "A descrição não pode ser nula.");
        Objects.requireNonNull(amount, "O valor não pode ser nulo.");
        Objects.requireNonNull(date, "A data não pode ser nula.");
        Objects.requireNonNull(wallet, "A carteira não pode ser nula.");
        Objects.requireNonNull(type, "O tipo não pode ser nulo.");

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }
    }

    // Inverte o valor se for saída
    public BigDecimal signedAmount() {
        return switch (type) {
            case INCOME -> amount;
            case EXPENSE -> amount.negate();
        };
    }
}
