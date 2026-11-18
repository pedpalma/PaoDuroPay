package com.paoduropay.backend.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Purchase(
        String description,
        BigDecimal totalAmount,
        LocalDate purchaseDate,
        int installmentCount
) {

    public static final int CLOSING_DAY = 5;

    public Purchase {
        Objects.requireNonNull(description, "A descrição não pode ser nula.");
        Objects.requireNonNull(totalAmount, "O valor total não pode ser nulo.");
        Objects.requireNonNull(purchaseDate, "A data da compra não pode ser nula.");

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }
        if (installmentCount <= 0) {
            throw new IllegalArgumentException("Deve ter pelo menos uma parcela.");
        }
    }

    // Garante que uma compra após o dia 5 será passada para a fatura do mês seguinte
    public YearMonth billingMonth() {
        YearMonth month = YearMonth.from(purchaseDate);
        int purchaseDay = purchaseDate.getDayOfMonth();

        if (purchaseDay > CLOSING_DAY) {
            month = month.plusMonths(1);
        }
        return month;
    }

    public List<Installment> generateInstallments() {
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(installmentCount), 2, RoundingMode.DOWN);

        // Calcula o resto para adicionar na primeira fatura
        BigDecimal installmentTotal = installmentAmount.multiply(BigDecimal.valueOf(installmentCount));
        BigDecimal remainder = totalAmount.subtract(installmentTotal);

        List<Installment> installments = new ArrayList<>();
        YearMonth firstBillingMonth = billingMonth();

        for (int i = 0; i < installmentCount; i++) {
            BigDecimal currentAmount = (i == 0) ? installmentAmount.add(remainder) : installmentAmount;

            YearMonth installmentBillingMonth = firstBillingMonth.plusMonths(i);

            Installment installment = new Installment(i + 1, currentAmount, installmentBillingMonth);
            installments.add(installment);
        }
        return installments;
    }
}
