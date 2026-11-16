package com.paoduropay.backend.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExtratoTest {
    @Test
    void calculaSaldoSeparadamentePorCarteira() {
        // Arrange
        List<Lancamento> lancamentos = List.of(
                new Lancamento(
                        "Salário",
                        new BigDecimal("3000.00"),
                        LocalDate.of(2026, 11, 6),
                        Carteira.CONTA_CORRENTE,
                        TipoDeLancamento.ENTRADA
                ),
                new Lancamento(
                        "Mercado",
                        new BigDecimal("200.00"),
                        LocalDate.of(2026, 10, 21),
                        Carteira.CONTA_CORRENTE,
                        TipoDeLancamento.SAIDA
                ),
                new Lancamento(
                        "VR",
                        new BigDecimal("462.00"),
                        LocalDate.of(2026, 9, 30),
                        Carteira.VR,
                        TipoDeLancamento.ENTRADA
                ),
                new Lancamento(
                        "VR",
                        new BigDecimal("35.00"),
                        LocalDate.of(2026, 10, 10),
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

    @Test
    void verificaOsSaldosNasCarteirasCorretamente() {
        // Arrange
        List<Lancamento> lancamentos = List.of(
                new Lancamento(
                        "Salário",
                        new BigDecimal("3000.00"),
                        LocalDate.of(2026, 11, 16),
                        Carteira.CONTA_CORRENTE,
                        TipoDeLancamento.ENTRADA
                ),
                new Lancamento(
                        "Mercado",
                        new BigDecimal("200.00"),
                        LocalDate.of(2026, 11, 26),
                        Carteira.CONTA_CORRENTE,
                        TipoDeLancamento.SAIDA
                ),
                new Lancamento(
                        "VR",
                        new BigDecimal("462.00"),
                        LocalDate.of(2026, 12, 31),
                        Carteira.VR,
                        TipoDeLancamento.ENTRADA
                ),
                new Lancamento(
                        "VR",
                        new BigDecimal("35.00"),
                        LocalDate.of(2027, 1, 10),
                        Carteira.VR,
                        TipoDeLancamento.SAIDA
                )
        );

        // Act
        Map<Carteira, BigDecimal> saldos = Extrato.saldosPorCarteira(lancamentos);

        // Assert
        assertThat(saldos.get(Carteira.CONTA_CORRENTE)).isEqualByComparingTo("2800.00");
        assertThat(saldos.get(Carteira.VR)).isEqualByComparingTo("427.00");

    }
}
