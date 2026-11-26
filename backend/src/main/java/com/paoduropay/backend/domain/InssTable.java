package com.paoduropay.backend.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class InssTable {
    // Baseado no Anexo II da Portaria Interministerial MPS/MF nº 13, de 9 de janeiro de 2026.
    public static final InssTable TABLE_2026 = new InssTable(List.of(
            new InssBracket(new BigDecimal("1621.00"), new BigDecimal("0.075")),
            new InssBracket(new BigDecimal("2902.84"), new BigDecimal("0.09")),
            new InssBracket(new BigDecimal("4354.27"), new BigDecimal("0.12")),
            new InssBracket(new BigDecimal("8475.55"), new BigDecimal("0.14"))
    ));
    private final List<InssBracket> brackets;

    public InssTable(List<InssBracket> brackets) {
        Objects.requireNonNull(brackets, "A lista de faixas não pode ser nula.");

        if (brackets.isEmpty()) {
            throw new IllegalArgumentException("A tabela precisa de pelo menos uma faixa.");
        }

        for (int i = 1; i < brackets.size(); i++) {
            InssBracket previous = brackets.get(i - 1);
            InssBracket current = brackets.get(i);

            if (current.upperLimit().compareTo(previous.upperLimit()) <= 0) {
                throw new IllegalArgumentException("As faixas devem estar em ordem crescente.");
            }
        }
        this.brackets = List.copyOf(brackets);
    }

    public BigDecimal calculateContribution(BigDecimal contributionBase) {
        Objects.requireNonNull(contributionBase, "O salário de contribuição não pode ser nulo.");

        if (contributionBase.signum() < 0) {
            throw new IllegalArgumentException("O salário de contribuição não pode ser negativo.");
        }

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal previousLimit = BigDecimal.ZERO;

        for (InssBracket bracket : brackets) {
            if (contributionBase.compareTo(previousLimit) <= 0) {
                break;
            }

            BigDecimal part = contributionBase.min(bracket.upperLimit()).subtract(previousLimit);
            total = total.add(part.multiply(bracket.rate()));
            previousLimit = bracket.upperLimit();
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

}
