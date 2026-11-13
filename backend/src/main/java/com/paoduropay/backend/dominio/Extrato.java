package com.paoduropay.backend.dominio;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Extrato {
    // Calcula o saldo somente da carteira correspondente
    public static BigDecimal calcularSaldo(List<Lancamento> lancamentos, Carteira carteira) {
        return lancamentos.stream()
                .filter(lancamento -> lancamento.carteira() == carteira)
                .map(Lancamento::valorComSinal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcula o saldo individual de cada carteira
    public static Map<Carteira, BigDecimal> saldosPorCarteira(List<Lancamento> lancamentos) {
        Map<Carteira, BigDecimal> saldos = new HashMap<>();

        for (Carteira carteira : Carteira.values()) {
            BigDecimal saldoDaCarteira = calcularSaldo(lancamentos, carteira);

            saldos.put(carteira, saldoDaCarteira);
        }
        return saldos;
    }
}
