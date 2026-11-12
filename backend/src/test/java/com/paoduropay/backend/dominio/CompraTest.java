package com.paoduropay.backend.dominio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CompraTest {

    @ParameterizedTest
    @CsvSource({
            "2026-11-05, 2026-11",
            "2026-11-06, 2026-12",
            "2026-11-01, 2026-11",
            "2026-12-31, 2027-01"
    })
    void calcularDataDaFaturaCorretamente(LocalDate dataCompra, YearMonth faturaEsperada) {
        // Arrange
        Compra compra = new Compra(
                "Compra de teste",
                new BigDecimal("100.00"),
                dataCompra,
                1
        );

        // Act
        YearMonth resultado = compra.mesDaFatura();

        // Assert
        assertThat(resultado).isEqualTo(faturaEsperada);
    }

    @Test
    void geraUmaFaturaParaCompraAVista() {
        // Arrange
        Compra compra = new Compra(
                "Compra de teste",
                new BigDecimal("50.00"),
                LocalDate.of(2026, 11, 5),
                1
        );
        // Act
        List<Parcela> parcelas = compra.gerarParcelas();

        // Assert
        assertThat(parcelas).hasSize(1);
        assertThat(parcelas.getFirst().numeroDaParcela()).isEqualTo(1);

        assertThat(parcelas.getFirst().valor()).isEqualByComparingTo("50.00");
        assertThat(parcelas.getFirst().fatura()).isEqualTo(YearMonth.of(2026, 11));
    }

    @Test
    void distribuiOValorCorretamenteEntreTresFaturas() {
        // Arrange
        Compra compra = new Compra(
                "Compra de teste",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 10, 20),
                3
        );

        // Act
        List<Parcela> parcelas = compra.gerarParcelas();

        // Assert
        assertThat(parcelas).hasSize(3);

        assertThat(parcelas)
                .extracting(Parcela::valor)
                .usingElementComparator(BigDecimal::compareTo)
                .containsExactly(
                        new BigDecimal("33.34"),
                        new BigDecimal("33.33"),
                        new BigDecimal("33.33")
                );

        assertThat(parcelas)
                .extracting(Parcela::numeroDaParcela)
                .containsExactly(1, 2, 3);

        assertThat(parcelas)
                .extracting(Parcela::fatura)
                .containsExactly(
                        YearMonth.of(2026, 11),
                        YearMonth.of(2026, 12),
                        YearMonth.of(2027, 1)
                );


    }

    @Test
    void avancaCorretamenteAsFaturasEntreAnos() {
        // Arrange
        Compra compra = new Compra(
                "Compra de teste",
                new BigDecimal("300.00"),
                LocalDate.of(2026, 11, 20),
                3
        );

        // Act
        List<Parcela> parcelas = compra.gerarParcelas();

        // Assert
        assertThat(parcelas).hasSize(3);

        assertThat(parcelas)
                .extracting(Parcela::fatura)
                .containsExactly(
                        YearMonth.of(2026, 12),
                        YearMonth.of(2027, 1),
                        YearMonth.of(2027, 2)
                );
    }

    @Test
    void rejeitaValorZero() {
        assertThatThrownBy(() -> new Compra(
                "Compra inválida",
                BigDecimal.ZERO,
                LocalDate.of(2026, 11, 5),
                1
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void rejeitaValorNegativo() {
        assertThatThrownBy(() -> new Compra(
                "Compra inválida",
                new BigDecimal("-10.00"),
                LocalDate.of(2026, 11, 5),
                1
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Valor deve ser positivo.");
    }

    @Test
    void rejeitaQuantidadeDeParcelasIgualAZero() {
        assertThatThrownBy(() -> new Compra(
                "Compra inválida",
                new BigDecimal("100.00"),
                LocalDate.of(2026, 11, 5),
                0
        )).isInstanceOf(IllegalArgumentException.class).hasMessage("Deve ter pelo menos uma parcela.");
    }

    @Test
    void preservaOValorTotalAoGerarSeteParcelas() {
        // Arrange
        BigDecimal valorTotal = new BigDecimal("99.99");


        Compra compra = new Compra(
                "Compra de teste",
                valorTotal,
                LocalDate.of(2026, 11, 5),
                7
        );

        // Act
        List<Parcela> parcelas = compra.gerarParcelas();

        BigDecimal somaParcelas = parcelas.stream()
                .map(Parcela::valor).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Assert
        assertThat(parcelas).hasSize(7);
        assertThat(somaParcelas).isEqualByComparingTo(valorTotal);
    }
}
