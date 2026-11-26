package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InssTableTest {

    @Test
    void rejectsEmptyBracketList() {
        assertThatThrownBy(() -> new InssTable(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A tabela precisa de pelo menos uma faixa.");
    }

    @Test
    void rejectsNullBracketList() {
        assertThatThrownBy(() -> new InssTable(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("A lista de faixas não pode ser nula.");
    }

    private InssTable createTestTable() {
        return new InssTable(List.of(
                new InssBracket(new BigDecimal("100.00"), new BigDecimal("0.10")),
                new InssBracket(new BigDecimal("200.00"), new BigDecimal("0.20"))
        ));
    }

    @Test
    void calculatesContributionProgressively() {
        // Arrange
        InssTable table = createTestTable();

        // Act
        BigDecimal result = table.calculateContribution(new BigDecimal("150.00"));

        // Assert
        assertThat(result).isEqualByComparingTo("20.00");
    }

    @Test
    void capsContributionAtTheLastBracket() {
        // Arrange
        InssTable table = createTestTable();

        // Act
        BigDecimal result = table.calculateContribution(new BigDecimal("500.00"));

        // Assert
        assertThat(result).isEqualByComparingTo("30.00");
    }

    @Test
    void rejectsNegativeContributionBase() {
        InssTable table = createTestTable();

        assertThatThrownBy(() -> table.calculateContribution(new BigDecimal("-1.00")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O salário de contribuição não pode ser negativo.");
    }

    @Test
    void rejectsNullContributionBase() {
        InssTable table = createTestTable();

        assertThatThrownBy(() -> table.calculateContribution(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("O salário de contribuição não pode ser nulo.");
    }

    @Test
    void returnsContributionWithTwoDecimalPlaces() {
        // Arrange
        InssTable table = createTestTable();

        // Act
        BigDecimal result = table.calculateContribution(new BigDecimal("1000.00"));

        // Assert
        assertThat(result.scale()).isEqualTo(2);
    }

    @Test
    void keepsADefensiveCopyOfTheBrackets() {
        // Arrange
        List<InssBracket> brackets = new ArrayList<>();

        brackets.add(new InssBracket(
                new BigDecimal("100.00"),
                new BigDecimal("0.10")
        ));

        InssTable table = new InssTable(brackets);

        brackets.add(new InssBracket(
                new BigDecimal("200.00"),
                new BigDecimal("0.20")
        ));

        // Act
        BigDecimal result = table.calculateContribution(new BigDecimal("200.00"));

        // Assert
        assertThat(result).isEqualByComparingTo("10.00");
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0.00",
            "1000.00, 75.00",
            "1621.00, 121.58",
            "2000.00, 155.69",
            "3000.00, 248.60",
            "5000.00, 501.51",
            "8475.55, 988.09",
            "10000.00, 988.09"
    })
    void calculatesContributionWith2026Table(String salary, String expected) {
        BigDecimal result = InssTable.TABLE_2026.calculateContribution(
                new BigDecimal(salary)
        );

        assertThat(result).isEqualByComparingTo(expected);
    }

    @Test
    void rejectsBracketsOutOfAscendingOrder() {
        List<InssBracket> brackets = List.of(
                new InssBracket(new BigDecimal("200.00"), new BigDecimal("0.10")),
                new InssBracket(new BigDecimal("100.00"), new BigDecimal("0.20"))
        );

        assertThatThrownBy(() -> new InssTable(brackets))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("As faixas devem estar em ordem crescente.");
    }

    @Test
    void rejectsBracketsWithRepeatedUpperLimit() {
        List<InssBracket> brackets = List.of(
                new InssBracket(new BigDecimal("100.00"), new BigDecimal("0.10")),
                new InssBracket(new BigDecimal("100.00"), new BigDecimal("0.20"))
        );

        assertThatThrownBy(() -> new InssTable(brackets))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("As faixas devem estar em ordem crescente.");
    }
}
