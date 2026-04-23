package br.com.zenon.fraud;

import java.util.Arrays;

public enum EnumTipoTransacao {
    CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER;

    public static EnumTipoTransacao getTipoTransacao(String tipo){
        try {
            for(EnumTipoTransacao tipoTransacao : EnumTipoTransacao.values()){
                if(tipoTransacao.name().equalsIgnoreCase(tipo)) return tipoTransacao;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //return Arrays.stream(EnumTipoTransacao.values()).filter(f -> f.name().equals(tipo)).findFirst().orElseThrow(() ->  new IllegalArgumentException("O tipo de transação informado é inválido"));
        IO.println(tipo);
        throw new IllegalArgumentException("O tipo de transação informado é inválido");
    }
}
