package com.paoduropay.backend.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public class SalaryAdvance {

    public static LocalDate calculatePaymentDate(YearMonth referenceMonth) {
        LocalDate paymentDate = referenceMonth.atDay(20);

        // A data de adiantamento sempre antecipa em caso de fim de semana.
        if (paymentDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            paymentDate = paymentDate.minusDays(1);
        } else if (paymentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            paymentDate = paymentDate.minusDays(2);
        }

        return paymentDate;
    }

}
