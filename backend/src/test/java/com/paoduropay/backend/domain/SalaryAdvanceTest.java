package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static com.paoduropay.backend.domain.SalaryAdvance.calculatePaymentDate;
import static org.assertj.core.api.Assertions.assertThat;

class SalaryAdvanceTest {

    @Test
    void keepsThe20thWhenItIsABusinessDay() {
        YearMonth referenceMonth = YearMonth.of(2027, 1);

        LocalDate result = calculatePaymentDate(referenceMonth);

        assertThat(result).isEqualTo(LocalDate.of(2027, 1, 20));
    }

    @Test
    void movesToFridayWhenThe20thIsSaturday() {
        YearMonth referenceMonth = YearMonth.of(2027, 2);

        LocalDate result = calculatePaymentDate(referenceMonth);

        assertThat(result).isEqualTo(LocalDate.of(2027, 2, 19));
    }

    @Test
    void movesToFridayWhenThe20thIsSunday() {
        YearMonth referenceMonth = YearMonth.of(2027, 6);

        LocalDate result = calculatePaymentDate(referenceMonth);

        assertThat(result).isEqualTo(LocalDate.of(2027, 6, 18));
    }

}
