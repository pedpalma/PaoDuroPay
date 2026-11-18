package com.paoduropay.backend.domain;

import java.math.BigDecimal;

public class MealVoucher {

    public static final BigDecimal DAILY_AMOUNT = new BigDecimal("21.00");

    public static BigDecimal calculateTopUpAmount(int businessDays) {

        if (businessDays < 0) {
            throw new IllegalArgumentException("Dias úteis não podem ser negativos.");
        }
        return DAILY_AMOUNT.multiply(BigDecimal.valueOf(businessDays));

    }

}
