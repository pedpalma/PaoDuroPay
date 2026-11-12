package com.paoduropay.backend.dominio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Compra(String descricao, BigDecimal valorTotal, LocalDate dataCompra, int quantidadeParcelas) {

    public static final int DIA_DE_FECHAMENTO = 5;

    public Compra {
        Objects.requireNonNull(descricao, "A descrição não pode ser nula.");
        Objects.requireNonNull(valorTotal, "O valor total não pode ser nulo.");
        Objects.requireNonNull(dataCompra, "A data da compra não pode ser nula.");

        if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }
        if (quantidadeParcelas <= 0) {
            throw new IllegalArgumentException("Deve ter pelo menos uma parcela.");
        }
    }

    // Garante que uma compra após o dia 5 será passada para a fatura do mês seguinte
    public YearMonth mesDaFatura() {
        YearMonth mesAnoCompra = YearMonth.from(dataCompra);
        int diaCompra = dataCompra.getDayOfMonth();

        if (diaCompra > DIA_DE_FECHAMENTO) {
            mesAnoCompra = mesAnoCompra.plusMonths(1);
        }
        return mesAnoCompra;
    }

    public List<Parcela> gerarParcelas() {
        BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(quantidadeParcelas), 2, RoundingMode.DOWN);

        // Calcula o resto para adicionar na primeira fatura
        BigDecimal totalDasParcelas = valorParcela.multiply(BigDecimal.valueOf(quantidadeParcelas));
        BigDecimal resto = valorTotal.subtract(totalDasParcelas);

        List<Parcela> parcelas = new ArrayList<>();
        YearMonth primeiraFatura = mesDaFatura();

        for (int i = 0; i < quantidadeParcelas; i++) {
            BigDecimal valorAtual = (i == 0) ? valorParcela.add(resto) : valorParcela;

            YearMonth vencimento = primeiraFatura.plusMonths(i);

            Parcela novaParcela = new Parcela(i + 1, valorAtual, vencimento);
            parcelas.add(novaParcela);
        }
        return parcelas;
    }
}
