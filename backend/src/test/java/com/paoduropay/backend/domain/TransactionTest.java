package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {

    @Test
    void returnsPositiveAmountForIncome() {
        // Arrange
        Transaction transaction = new Transaction(
                "Lançamento de teste",
                new BigDecimal("3000.00"),
                LocalDate.of(2026, 11, 6),
                Wallet.CHECKING_ACCOUNT,
                TransactionType.INCOME
        );

        // Act
        BigDecimal result = transaction.signedAmount();

        // Assert
        assertThat(result).isEqualByComparingTo("3000.00");
    }

    @Test
    void returnsNegativeAmountForExpense() {
        // Arrange
        Transaction transaction = new Transaction(
                "Lançamento de teste",
                new BigDecimal("200.00"),
                LocalDate.of(2026, 11, 20),
                Wallet.CHECKING_ACCOUNT,
                TransactionType.EXPENSE
        );

        // Act
        BigDecimal result = transaction.signedAmount();

        // Assert
        assertThat(result).isEqualByComparingTo("-200.00");
    }

    @Test
    void throwsExceptionIfAmountIsZero() {
        assertThatThrownBy(() -> new Transaction(
                "Lançamento inválido",
                BigDecimal.ZERO,
                LocalDate.of(2026, 11, 15),
                Wallet.CHECKING_ACCOUNT,
                TransactionType.EXPENSE
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void throwsExceptionIfAmountIsNegative() {
        assertThatThrownBy(() -> new Transaction(
                "Lançamento inválido",
                new BigDecimal("-10.00"),
                LocalDate.of(2026, 11, 15),
                Wallet.CHECKING_ACCOUNT,
                TransactionType.INCOME
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void throwsExceptionIfWalletIsNull() {
        assertThatThrownBy(() -> new Transaction(
                "Lançamento inválido",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 11, 15),
                null,
                TransactionType.INCOME
        )).isInstanceOf(NullPointerException.class).hasMessage("A carteira não pode ser nula.");
    }

    @ParameterizedTest
    @CsvSource({
            "INCOME, 100.00",
            "EXPENSE, -100.00"
    })
    void returnsTheAmountWithTheCorrectSign(TransactionType type, String expected) {
        // Arrange
        Transaction transaction = new Transaction(
                "Lançamento de teste",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 11, 15),
                Wallet.CHECKING_ACCOUNT,
                type
        );

        // Act
        BigDecimal result = transaction.signedAmount();

        // Assert
        assertThat(result).isEqualByComparingTo(expected);
    }
}
