package com.paoduropay.backend.dominio;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;
import java.util.Set;

public class DiasUteis {
    private static final int QUINTO_DIA_UTIL = 5;

    // Conta sábado como dia útil
    public static boolean ehDiaUtil(LocalDate data, Set<LocalDate> feriados) {
        Objects.requireNonNull(data, "A data não pode ser nula.");
        Objects.requireNonNull(feriados, "Os feriados não podem ser nulos.");

        DayOfWeek dia = data.getDayOfWeek();

        return (dia != DayOfWeek.SUNDAY) && !feriados.contains(data);
    }

    // Encontra e retorna o quinto dia útil do mês
    public static LocalDate quintoDiaUtil(YearMonth mes, Set<LocalDate> feriados) {
        Objects.requireNonNull(mes, "O mes não pode ser nulo.");
        Objects.requireNonNull(feriados, "Os feriados não podem ser nulos.");

        LocalDate diaDoMes = mes.atDay(1);
        LocalDate ultimoDiaDoMes = mes.atEndOfMonth();
        int contadorDeDiasUteis = 0;

        while (!diaDoMes.isAfter(ultimoDiaDoMes)) {
            if (ehDiaUtil(diaDoMes, feriados)) {
                contadorDeDiasUteis++;
                if (contadorDeDiasUteis == QUINTO_DIA_UTIL) {
                    return diaDoMes;
                }
            }
            diaDoMes = diaDoMes.plusDays(1);
        }
        throw new IllegalArgumentException("Não foi possível encontrar o quinto dia útil.");
    }

    // Sobrecarga de quintoDiaUtil
    public static LocalDate quintoDiaUtil(YearMonth mes) {
        Set<LocalDate> feriados = Set.of();
        return quintoDiaUtil(mes, feriados);
    }

    // Sábado é considerado dia útil; apenas domingos e feriados são excluídos.
    public static int contarDiasUteis(YearMonth mes, Set<LocalDate> feriados) {
        Objects.requireNonNull(mes, "O mes não pode ser nulo.");
        Objects.requireNonNull(feriados, "Os feriados não podem ser nulos.");

        return (int) mes.atDay(1)
                .datesUntil(mes.atEndOfMonth().plusDays(1))
                .filter(data -> ehDiaUtil(data, feriados))
                .count();
    }
}
