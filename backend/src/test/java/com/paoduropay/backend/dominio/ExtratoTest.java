package com.paoduropay.backend.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExtratoTest {
    @Test
    void calculaSaldoSeparadamentePorCarteira() {
        // Arrange
        List<Lancamento> lancamentos = List.of(
                new Lancamento(
                        "Sálario",
                        new BigDecimal("3000.00"),
                        LocalDate.now(),
                        Carteira.CONTA_CORRENTE,
                        TipoDeLancamento.ENTRADA
                ),
                new Lancamento(
                        "Mercado",
                        new BigDecimal("200.00"),
                        LocalDate.now(),
                        Carteira.CONTA_CORRENTE,
                        TipoDeLancamento.SAIDA
                ),
                new Lancamento(
                        "VR",
                        new BigDecimal("462.00"),
                        LocalDate.now(),
                        Carteira.VR,
                        TipoDeLancamento.ENTRADA
                ),
                new Lancamento(
                        "VR",
                        new BigDecimal("35.00"),
                        LocalDate.now(),
                        Carteira.VR,
                        TipoDeLancamento.SAIDA
                )
        );

        // Act
        BigDecimal saldoContaCorrente = Extrato.calcularSaldo(lancamentos, Carteira.CONTA_CORRENTE);
        BigDecimal saldoVR = Extrato.calcularSaldo(lancamentos, Carteira.VR);

        // Assert
        assertThat(saldoContaCorrente).isEqualByComparingTo("2800.00");
        assertThat(saldoVR).isEqualByComparingTo("427.00");
    }

    @Test
    void retornaSaldoZeroSeAListaEstiverVazia() {
        // Arrange
        List<Lancamento> lancamentos = List.of();

        // Act
        BigDecimal saldo = Extrato.calcularSaldo(lancamentos, Carteira.CONTA_CORRENTE);

        // Assert
        assertThat(saldo).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
