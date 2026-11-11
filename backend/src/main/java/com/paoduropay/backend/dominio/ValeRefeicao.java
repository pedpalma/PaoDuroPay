package com.paoduropay.backend.dominio;

import java.math.BigDecimal;

public class ValeRefeicao {

    public static final BigDecimal VALOR_DO_DIA_TRABALHADO = new BigDecimal("21.00");

    public static BigDecimal calcularValorDaRecarga(int diasUteis) {

        if (diasUteis < 0) {
            throw new IllegalArgumentException("Dias úteis não podem ser negativos.");
        }
        return VALOR_DO_DIA_TRABALHADO.multiply(BigDecimal.valueOf(diasUteis));

    }

}
