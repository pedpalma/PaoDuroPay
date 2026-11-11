package com.paoduropay.backend.dominio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public class Adiantamento {

    public static LocalDate calcularDataAdiantamento(YearMonth mesDeReferencia) {
        LocalDate dataDoAdiantamento = mesDeReferencia.atDay(20);

        // A data de adiantamento sempre antecipa em caso de feriado ou fim de semana.
        if (dataDoAdiantamento.getDayOfWeek() == DayOfWeek.SATURDAY) {
            dataDoAdiantamento = dataDoAdiantamento.minusDays(1);
        }
        else if (dataDoAdiantamento.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dataDoAdiantamento = dataDoAdiantamento.minusDays(2);
        }

        return dataDoAdiantamento;
    }

}
