package br.com.zenon.fraud;

import java.util.Arrays;

public enum EnumTipoTransacao {
    CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER;

    public static EnumTipoTransacao getTipoTransacao(String tipo){
        return Arrays.stream(EnumTipoTransacao.values()).filter(f -> f.name().equals(tipo)).findFirst().orElse(EnumTipoTransacao.PAYMENT);
    }
}
