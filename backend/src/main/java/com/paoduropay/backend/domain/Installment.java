package com.paoduropay.backend.domain;

import java.math.BigDecimal;
import java.time.YearMonth;

public record Installment(int number, BigDecimal amount, YearMonth billingMonth) {

    public Installment {
        if (number < 1) {
            throw new IllegalArgumentException("Deve ter pelo menos uma parcela.");
        }
    }
}
