package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.paoduropay.backend.domain.MealVoucher.calculateTopUpAmount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MealVoucherTest {

    @Test
    void monthWith22BusinessDays() {

        BigDecimal result = calculateTopUpAmount(22);

        assertThat(result).isEqualByComparingTo("462.00");

    }

    @Test
    void monthWith0BusinessDays() {

        BigDecimal result = calculateTopUpAmount(0);

        assertThat(result).isEqualByComparingTo("0.00");

    }

    @Test
    void throwsExceptionForNegativeBusinessDays() {

        assertThatThrownBy(() -> calculateTopUpAmount(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dias úteis não podem ser negativos.");

    }
}
