package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AccountStatementTest {
    @Test
    void calculatesBalanceSeparatelyByWallet() {
        // Arrange
        List<Transaction> transactions = List.of(
                new Transaction(
                        "Salário",
                        new BigDecimal("3000.00"),
                        LocalDate.of(2026, 11, 6),
                        Wallet.CHECKING_ACCOUNT,
                        TransactionType.INCOME
                ),
                new Transaction(
                        "Mercado",
                        new BigDecimal("200.00"),
                        LocalDate.of(2026, 10, 21),
                        Wallet.CHECKING_ACCOUNT,
                        TransactionType.EXPENSE
                ),
                new Transaction(
                        "VR",
                        new BigDecimal("462.00"),
                        LocalDate.of(2026, 9, 30),
                        Wallet.MEAL_VOUCHER,
                        TransactionType.INCOME
                ),
                new Transaction(
                        "VR",
                        new BigDecimal("35.00"),
                        LocalDate.of(2026, 10, 10),
                        Wallet.MEAL_VOUCHER,
                        TransactionType.EXPENSE
                )
        );

        // Act
        BigDecimal checkingAccountBalance = AccountStatement.calculateBalance(transactions, Wallet.CHECKING_ACCOUNT);
        BigDecimal mealVoucherBalance = AccountStatement.calculateBalance(transactions, Wallet.MEAL_VOUCHER);

        // Assert
        assertThat(checkingAccountBalance).isEqualByComparingTo("2800.00");
        assertThat(mealVoucherBalance).isEqualByComparingTo("427.00");
    }

    @Test
    void returnsZeroBalanceIfListIsEmpty() {
        // Arrange
        List<Transaction> transactions = List.of();

        // Act
        BigDecimal balance = AccountStatement.calculateBalance(transactions, Wallet.CHECKING_ACCOUNT);

        // Assert
        assertThat(balance).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void returnsTheBalanceOfEachWallet() {
        // Arrange
        List<Transaction> transactions = List.of(
                new Transaction(
                        "Salário",
                        new BigDecimal("3000.00"),
                        LocalDate.of(2026, 11, 16),
                        Wallet.CHECKING_ACCOUNT,
                        TransactionType.INCOME
                ),
                new Transaction(
                        "Mercado",
                        new BigDecimal("200.00"),
                        LocalDate.of(2026, 11, 26),
                        Wallet.CHECKING_ACCOUNT,
                        TransactionType.EXPENSE
                ),
                new Transaction(
                        "VR",
                        new BigDecimal("462.00"),
                        LocalDate.of(2026, 12, 31),
                        Wallet.MEAL_VOUCHER,
                        TransactionType.INCOME
                ),
                new Transaction(
                        "VR",
                        new BigDecimal("35.00"),
                        LocalDate.of(2027, 1, 10),
                        Wallet.MEAL_VOUCHER,
                        TransactionType.EXPENSE
                )
        );

        // Act
        Map<Wallet, BigDecimal> balances = AccountStatement.balancesByWallet(transactions);

        // Assert
        assertThat(balances.get(Wallet.CHECKING_ACCOUNT)).isEqualByComparingTo("2800.00");
        assertThat(balances.get(Wallet.MEAL_VOUCHER)).isEqualByComparingTo("427.00");

    }
}
