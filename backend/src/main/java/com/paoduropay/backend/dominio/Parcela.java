package com.paoduropay.backend.dominio;

import java.math.BigDecimal;
import java.time.YearMonth;

public record Parcela(int numeroDaParcela, BigDecimal valor, YearMonth fatura) {

    public Parcela {
        if (numeroDaParcela < 1) {
            throw new IllegalArgumentException("Deve ter pelo menos uma parcela.");
        }
    }
}
