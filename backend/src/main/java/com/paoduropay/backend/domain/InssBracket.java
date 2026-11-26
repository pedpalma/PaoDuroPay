package com.paoduropay.backend.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record InssBracket(BigDecimal upperLimit, BigDecimal rate) {
    public InssBracket {
        Objects.requireNonNull(upperLimit, "O limite superior não pode ser nulo.");
        Objects.requireNonNull(rate, "A alíquota não pode ser nula.");

        if (upperLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O limite superior deve ser positivo.");
        }
        if ((rate.compareTo(BigDecimal.ZERO) <= 0) || (rate.compareTo(BigDecimal.ONE) >= 0)) {
            throw new IllegalArgumentException("A alíquota deve estar entre 0 e 1.");
        }
    }
}
