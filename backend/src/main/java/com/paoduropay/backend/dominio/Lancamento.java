package com.paoduropay.backend.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public record Lancamento(
        String descricao,
        BigDecimal valor,
        LocalDate data,
        Carteira carteira,
        TipoDeLancamento tipo
) {

    public Lancamento {
        Objects.requireNonNull(descricao, "A descrição não pode ser nula.");
        Objects.requireNonNull(valor, "O valor não pode ser nulo.");
        Objects.requireNonNull(data, "A data não pode ser nula.");
        Objects.requireNonNull(carteira, "A carteira não pode ser nula.");
        Objects.requireNonNull(tipo, "O tipo não pode ser nulo.");

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }
    }

    // Inverte o valor se for saída
    public BigDecimal valorComSinal() {
        return switch (tipo) {
            case ENTRADA -> valor;
            case SAIDA -> valor.negate();
        };
    }
}
