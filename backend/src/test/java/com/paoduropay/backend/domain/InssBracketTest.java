package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InssBracketTest {
    @Test
    void exposesTheProvidedValues() {
        // Arrange
        BigDecimal upperLimit = new BigDecimal("1621.00");
        BigDecimal rate = new BigDecimal("0.075");

        // Act
        InssBracket inssBracket = new InssBracket(upperLimit, rate);

        // Assert
        assertThat(inssBracket.upperLimit()).isEqualByComparingTo(upperLimit);
        assertThat(inssBracket.rate()).isEqualByComparingTo(rate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-100.00"})
    void rejectsNonPositiveUpperLimit(BigDecimal upperLimit) {
        assertThatThrownBy(() ->
                new InssBracket(upperLimit, new BigDecimal("0.075"))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O limite superior deve ser positivo.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1", "1.5", "-0.1"})
    void rejectsRateOutOfRange(BigDecimal rate) {
        assertThatThrownBy(() ->
                new InssBracket(new BigDecimal("1621.00"), rate)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A alíquota deve estar entre 0 e 1.");
    }

    @Test
    void rejectsNullUpperLimit() {
        assertThatThrownBy(() ->
                new InssBracket(null, new BigDecimal("0.075"))
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("O limite superior não pode ser nulo.");
    }

    @Test
    void rejectsNullRate() {
        assertThatThrownBy(() ->
                new InssBracket(new BigDecimal("1621.00"), null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("A alíquota não pode ser nula.");
    }
}
