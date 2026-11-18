package com.paoduropay.backend.domain;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AccountStatement {
    // Calcula o saldo somente da carteira correspondente
    public static BigDecimal calculateBalance(List<Transaction> transactions, Wallet wallet) {
        return transactions.stream()
                .filter(transaction -> transaction.wallet() == wallet)
                .map(Transaction::signedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcula o saldo individual de cada carteira
    public static Map<Wallet, BigDecimal> balancesByWallet(List<Transaction> transactions) {
        Map<Wallet, BigDecimal> balances = new EnumMap<>(Wallet.class);

        for (Wallet wallet : Wallet.values()) {
            BigDecimal walletBalance = calculateBalance(transactions, wallet);

            balances.put(wallet, walletBalance);
        }
        return balances;
    }
}
