package com.paoduropay.backend.dominio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiasUteisTest {
    static Stream<Arguments> fornecerCenariosParaEhDiaUtil() {
        // Arrange
        return Stream.of(
                Arguments.of(
                        LocalDate.of(2026, 11, 3),
                        Set.of(),
                        true
                ),
                Arguments.of(
                        LocalDate.of(2026, 11, 7),
                        Set.of(),
                        true
                ),
                Arguments.of(
                        LocalDate.of(2026, 11, 8),
                        Set.of(),
                        false
                ),
                Arguments.of(
                        LocalDate.of(2026, 11, 2),
                        Set.of(LocalDate.of(2026, 11, 2)),
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("fornecerCenariosParaEhDiaUtil")
    void verificaSeEhDiaUtil(LocalDate data, Set<LocalDate> feriados, boolean esperado) {
        // Act
        boolean resultado = DiasUteis.ehDiaUtil(data, feriados);

        // Assert
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void retornaOSextoDiaDeNovembroQuandoNaoPossuiFeriados() {
        // Arrange
        YearMonth mes = YearMonth.of(2026, 11);
        Set<LocalDate> feriados = Set.of();
        LocalDate esperado = LocalDate.of(2026, 11, 6);

        // Act
        LocalDate resultado = DiasUteis.quintoDiaUtil(mes, feriados);

        // Assert
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void pulaOFeriadoAoCalcularOQuintoDiaUtil() {
        // Arrange
        YearMonth mes = YearMonth.of(2026, 11);
        Set<LocalDate> feriados = Set.of(LocalDate.of(2026, 11, 2));
        LocalDate esperado = LocalDate.of(2026, 11, 7);

        // Act
        LocalDate resultado = DiasUteis.quintoDiaUtil(mes, feriados);

        // Assert
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void retornaOSextoDiaDeNovembroQuandoUsaASobrecargaSemFeriados() {
        // Arrange
        LocalDate esperado = LocalDate.of(2026, 11, 6);

        // Act
        LocalDate resultado = DiasUteis.quintoDiaUtil(YearMonth.of(2026, 11));

        // Assert
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void lancaExcecaoQuandoFeriadoEhNulo() {
        YearMonth mes = YearMonth.of(2026, 11);

        assertThatThrownBy(() -> DiasUteis.quintoDiaUtil(mes, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Os feriados não podem ser nulos.");
    }

    @Test
    void contaVinteECincoDiasUteisSemFeriados() {
        // Arrange
        YearMonth mes = YearMonth.of(2026, 11);
        Set<LocalDate> feriados = Set.of();
        int esperado = 25;

        // Act
        int resultado = DiasUteis.contarDiasUteis(mes, feriados);

        // Assert
        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    void contaVinteEQuatroDiasUteisComFeriado() {
        // Arrange
        YearMonth mes = YearMonth.of(2026, 11);
        Set<LocalDate> feriados = Set.of(LocalDate.of(2026, 11, 2));
        int esperado = 24;

        // Act
        int resultado = DiasUteis.contarDiasUteis(mes, feriados);

        // Assert
        assertThat(resultado).isEqualTo(esperado);
    }
}
