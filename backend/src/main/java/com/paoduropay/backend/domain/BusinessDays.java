package com.paoduropay.backend.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;
import java.util.Set;

public class BusinessDays {
    private static final int FIFTH_BUSINESS_DAY = 5;

    // Conta sábado como dia útil, excluindo domingos e feriados
    public static boolean isBusinessDay(LocalDate date, Set<LocalDate> holidays) {
        Objects.requireNonNull(date, "A data não pode ser nula.");
        Objects.requireNonNull(holidays, "Os feriados não podem ser nulos.");

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return (dayOfWeek != DayOfWeek.SUNDAY) && !holidays.contains(date);
    }

    // Encontra e retorna o quinto dia útil do mês
    public static LocalDate fifthBusinessDay(YearMonth month, Set<LocalDate> holidays) {
        Objects.requireNonNull(month, "O mês não pode ser nulo.");
        Objects.requireNonNull(holidays, "Os feriados não podem ser nulos.");

        LocalDate currentDate = month.atDay(1);
        LocalDate lastDayOfMonth = month.atEndOfMonth();
        int businessDayCount = 0;

        while (!currentDate.isAfter(lastDayOfMonth)) {
            if (isBusinessDay(currentDate, holidays)) {
                businessDayCount++;
                if (businessDayCount == FIFTH_BUSINESS_DAY) {
                    return currentDate;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        throw new IllegalArgumentException("Não foi possível encontrar o quinto dia útil.");
    }

    // Calcula sem considerar feriados
    public static LocalDate fifthBusinessDay(YearMonth month) {
        Set<LocalDate> holidays = Set.of();
        return fifthBusinessDay(month, holidays);
    }

    // Conta os dias úteis do mês
    public static int countBusinessDays(YearMonth month, Set<LocalDate> holidays) {
        Objects.requireNonNull(month, "O mês não pode ser nulo.");
        Objects.requireNonNull(holidays, "Os feriados não podem ser nulos.");

        return (int) month.atDay(1)
                .datesUntil(month.atEndOfMonth().plusDays(1))
                .filter(date -> isBusinessDay(date, holidays))
                .count();
    }
}
