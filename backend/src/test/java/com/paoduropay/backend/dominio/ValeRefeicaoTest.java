package com.paoduropay.backend.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.paoduropay.backend.dominio.ValeRefeicao.calcularValorDaRecarga;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValeRefeicaoTest {

    // 22 dias úteis
    @Test
    void mesCom22DiasUteis() {

        BigDecimal resultado = calcularValorDaRecarga(22);

        assertThat(resultado).isEqualByComparingTo("462.00");

    }

    // 0 dias úteis
    @Test
    void mesCom0DiasUteis() {

        BigDecimal resultado = calcularValorDaRecarga(0);

        assertThat(resultado).isEqualByComparingTo("0.00");

    }

    // Dias úteis negativos
    @Test
    void mesComDiasUteisNegativosDeveLancarExcecao() {

        assertThatThrownBy(() -> calcularValorDaRecarga(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dias úteis não podem ser negativos.");

    }
}
