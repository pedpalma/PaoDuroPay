package com.paoduropay.backend.dominio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LancamentoTest {

    @Test
    void retornaValorPositivoSeForEntrada() {
        // Arrange
        Lancamento lancamento = new Lancamento(
                "Lançamento de teste",
                new BigDecimal("3000.00"),
                LocalDate.of(2026, 11, 6),
                Carteira.CONTA_CORRENTE,
                TipoDeLancamento.ENTRADA
        );

        // Act
        BigDecimal resultado = lancamento.valorComSinal();

        // Assert
        assertThat(resultado).isEqualByComparingTo("3000.00");
    }

    @Test
    void retornaValorNegativoSeForSaida() {
        // Arrange
        Lancamento lancamento = new Lancamento(
                "Lançamento de teste",
                new BigDecimal("200.00"),
                LocalDate.of(2026, 11, 20),
                Carteira.CONTA_CORRENTE,
                TipoDeLancamento.SAIDA
        );

        // Act
        BigDecimal resultado = lancamento.valorComSinal();

        // Assert
        assertThat(resultado).isEqualByComparingTo("-200.00");
    }

    @Test
    void lancaExcecaoSeValorForZero() {
        assertThatThrownBy(() -> new Lancamento(
                "Lançamento inválido",
                BigDecimal.ZERO,
                LocalDate.of(2026, 11, 15),
                Carteira.CONTA_CORRENTE,
                TipoDeLancamento.SAIDA
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void lancaExcecaoSeValorForNegativo() {
        assertThatThrownBy(() -> new Lancamento(
                "Lançamento inválido",
                new BigDecimal("-10.00"),
                LocalDate.of(2026, 11, 15),
                Carteira.CONTA_CORRENTE,
                TipoDeLancamento.ENTRADA
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void lancaExcecaoSeCarteiraForNula() {
        assertThatThrownBy(() -> new Lancamento(
                "Lançamento inválido",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 11, 15),
                null,
                TipoDeLancamento.ENTRADA
        )).isInstanceOf(NullPointerException.class).hasMessage("A carteira não pode ser nula.");
    }

    @ParameterizedTest
    @CsvSource({
            "ENTRADA, 100.00",
            "SAIDA, -100.00"
    })
    void retornaOValorComOSinalCorreto(TipoDeLancamento tipo, String esperado) {
        // Arrange
        Lancamento lancamento = new Lancamento(
                "Lançamento de teste",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 11, 15),
                Carteira.CONTA_CORRENTE,
                tipo
        );

        // Act
        BigDecimal resultado = lancamento.valorComSinal();

        // Assert
        assertThat(resultado).isEqualByComparingTo(esperado);
    }
}
