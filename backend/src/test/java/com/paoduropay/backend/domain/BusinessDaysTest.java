package com.paoduropay.backend.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BusinessDaysTest {
    static Stream<Arguments> provideScenariosForIsBusinessDay() {
        // Arrange
        return Stream.of(
                Arguments.of(
                        LocalDate.of(2026, 11, 3),
                        Set.of(),
                        true
                ),
                Arguments.of(
                        LocalDate.of(2026, 11, 7),
                        Set.of(),
                        true
                ),
                Arguments.of(
                        LocalDate.of(2026, 11, 8),
                        Set.of(),
                        false
                ),
                Arguments.of(
                        LocalDate.of(2026, 11, 2),
                        Set.of(LocalDate.of(2026, 11, 2)),
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideScenariosForIsBusinessDay")
    void identifiesBusinessDays(LocalDate date, Set<LocalDate> holidays, boolean expected) {
        // Act
        boolean result = BusinessDays.isBusinessDay(date, holidays);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void returnsTheSixthDayOfNovemberWhenThereAreNoHolidays() {
        // Arrange
        YearMonth month = YearMonth.of(2026, 11);
        Set<LocalDate> holidays = Set.of();
        LocalDate expected = LocalDate.of(2026, 11, 6);

        // Act
        LocalDate result = BusinessDays.fifthBusinessDay(month, holidays);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void skipsHolidayWhenCalculatingTheFifthBusinessDay() {
        // Arrange
        YearMonth month = YearMonth.of(2026, 11);
        Set<LocalDate> holidays = Set.of(LocalDate.of(2026, 11, 2));
        LocalDate expected = LocalDate.of(2026, 11, 7);

        // Act
        LocalDate result = BusinessDays.fifthBusinessDay(month, holidays);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void returnsTheSixthDayOfNovemberWhenUsingTheNoHolidaysOverload() {
        // Arrange
        LocalDate expected = LocalDate.of(2026, 11, 6);

        // Act
        LocalDate result = BusinessDays.fifthBusinessDay(YearMonth.of(2026, 11));

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void throwsExceptionWhenHolidaysAreNull() {
        YearMonth month = YearMonth.of(2026, 11);

        assertThatThrownBy(() -> BusinessDays.fifthBusinessDay(month, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Os feriados não podem ser nulos.");
    }

    @Test
    void countsTwentyFiveBusinessDaysWithoutHolidays() {
        // Arrange
        YearMonth month = YearMonth.of(2026, 11);
        Set<LocalDate> holidays = Set.of();
        int expected = 25;

        // Act
        int result = BusinessDays.countBusinessDays(month, holidays);

        // Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void countsTwentyFourBusinessDaysWithOneHoliday() {
        // Arrange
        YearMonth month = YearMonth.of(2026, 11);
        Set<LocalDate> holidays = Set.of(LocalDate.of(2026, 11, 2));
        int expected = 24;

        // Act
        int result = BusinessDays.countBusinessDays(month, holidays);

        // Assert
        assertThat(result).isEqualTo(expected);
    }
}
