package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(
        Integer step, //
        EnumTipoTransacao type, //
        BigDecimal amount, //
        String nameOrig, //
        BigDecimal oldbalanceOrg,//
        BigDecimal newbalanceOrig, //
        String nameDest, //
        BigDecimal oldbalanceDest, //
        BigDecimal newbalanceDest, //
        boolean isFraud,//
        boolean isFlaggedFraud) {
}
