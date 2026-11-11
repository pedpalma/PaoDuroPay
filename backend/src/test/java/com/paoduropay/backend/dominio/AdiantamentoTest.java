package com.paoduropay.backend.dominio;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static com.paoduropay.backend.dominio.Adiantamento.calcularDataAdiantamento;
import static org.assertj.core.api.Assertions.assertThat;

class AdiantamentoTest {

    // Dia útil sendo dia 20
    @Test
    void dia20EDiaUtil() {
        YearMonth mesDeReferencia = YearMonth.of(2027, 1);

        LocalDate resultado = calcularDataAdiantamento(mesDeReferencia);

        assertThat(resultado).isEqualTo(LocalDate.of(2027,1,20));
    }

    // Dia 20 no sábado
    @Test
    void dia20ESabado() {
        YearMonth mesDeReferencia = YearMonth.of(2027, 2);

        LocalDate resultado = calcularDataAdiantamento(mesDeReferencia);

        assertThat(resultado).isEqualTo(LocalDate.of(2027,2,19));
    }

    // Dia 20 no domingo
    @Test
    void dia20EDomingo() {
        YearMonth mesDeReferencia = YearMonth.of(2027, 6);

        LocalDate resultado = calcularDataAdiantamento(mesDeReferencia);

        assertThat(resultado).isEqualTo(LocalDate.of(2027,6,18));
    }

}
