package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PurchaseTest {

    @ParameterizedTest
    @CsvSource({
            "2026-11-05, 2026-11",
            "2026-11-06, 2026-12",
            "2026-11-01, 2026-11",
            "2026-12-31, 2027-01"
    })
    void calculatesBillingMonthCorrectly(LocalDate purchaseDate, YearMonth expectedBillingMonth) {
        // Arrange
        Purchase purchase = new Purchase(
                "Compra de teste",
                new BigDecimal("100.00"),
                purchaseDate,
                1
        );

        // Act
        YearMonth result = purchase.billingMonth();

        // Assert
        assertThat(result).isEqualTo(expectedBillingMonth);
    }

    @Test
    void generatesOneInstallmentForASinglePayment() {
        // Arrange
        Purchase purchase = new Purchase(
                "Compra de teste",
                new BigDecimal("50.00"),
                LocalDate.of(2026, 11, 5),
                1
        );
        // Act
        List<Installment> installments = purchase.generateInstallments();

        // Assert
        assertThat(installments).hasSize(1);
        assertThat(installments.getFirst().number()).isEqualTo(1);

        assertThat(installments.getFirst().amount()).isEqualByComparingTo("50.00");
        assertThat(installments.getFirst().billingMonth()).isEqualTo(YearMonth.of(2026, 11));
    }

    @Test
    void distributesTheAmountAmongThreeInstallments() {
        // Arrange
        Purchase purchase = new Purchase(
                "Compra de teste",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 10, 20),
                3
        );

        // Act
        List<Installment> installments = purchase.generateInstallments();

        // Assert
        assertThat(installments).hasSize(3);

        assertThat(installments)
                .extracting(Installment::amount)
                .usingElementComparator(BigDecimal::compareTo)
                .containsExactly(
                        new BigDecimal("33.34"),
                        new BigDecimal("33.33"),
                        new BigDecimal("33.33")
                );

        assertThat(installments)
                .extracting(Installment::number)
                .containsExactly(1, 2, 3);

        assertThat(installments)
                .extracting(Installment::billingMonth)
                .containsExactly(
                        YearMonth.of(2026, 11),
                        YearMonth.of(2026, 12),
                        YearMonth.of(2027, 1)
                );


    }

    @Test
    void advancesBillingMonthsAcrossYears() {
        // Arrange
        Purchase purchase = new Purchase(
                "Compra de teste",
                new BigDecimal("300.00"),
                LocalDate.of(2026, 11, 20),
                3
        );

        // Act
        List<Installment> installments = purchase.generateInstallments();

        // Assert
        assertThat(installments).hasSize(3);

        assertThat(installments)
                .extracting(Installment::billingMonth)
                .containsExactly(
                        YearMonth.of(2026, 12),
                        YearMonth.of(2027, 1),
                        YearMonth.of(2027, 2)
                );
    }

    @Test
    void rejectsZeroAmount() {
        assertThatThrownBy(() -> new Purchase(
                "Compra inválida",
                BigDecimal.ZERO,
                LocalDate.of(2026, 11, 5),
                1
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void rejectsNegativeAmount() {
        assertThatThrownBy(() -> new Purchase(
                "Compra inválida",
                new BigDecimal("-10.00"),
                LocalDate.of(2026, 11, 5),
                1
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void rejectsZeroInstallments() {
        assertThatThrownBy(() -> new Purchase(
                "Compra inválida",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 11, 5),
                0
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Deve ter pelo menos uma parcela.");
    }

    @Test
    void preservesTheTotalAmountWithSevenInstallments() {
        // Arrange
        BigDecimal totalAmount = new BigDecimal("99.99");


        Purchase purchase = new Purchase(
                "Compra de teste",
                totalAmount,
                LocalDate.of(2026, 11, 5),
                7
        );

        // Act
        List<Installment> installments = purchase.generateInstallments();

        BigDecimal installmentsSum = installments.stream()
                .map(Installment::amount).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Assert
        assertThat(installments).hasSize(7);
        assertThat(installmentsSum).isEqualByComparingTo(totalAmount);
    }
}
