package br.com.zenon.fraud;

import java.math.BigDecimal;

public class Main {
    static void main() {
        IO.println("TESTE");

        Transaction transacao01 = new Transaction(1,
                EnumTipoTransacao.PAYMENT,
                BigDecimal.valueOf(9839.64),
                "C1231006815",
                BigDecimal.valueOf(170136.0),
                BigDecimal.valueOf(160296.36),
                "M1979787155",
                BigDecimal.valueOf(0.0),
                BigDecimal.valueOf(0.0),
                false,//
                false);

        Transaction transacao02 = new Transaction(2,
                EnumTipoTransacao.CASH_OUT,
                BigDecimal.valueOf(850002.52),
                "C1280323807",
                BigDecimal.valueOf(850002.52),
                BigDecimal.valueOf(0.0),
                "C873221189",
                BigDecimal.valueOf(6510099.11),
                BigDecimal.valueOf(7360101.63),
                true,//
                false);

        IO.println("TRANSACAO 01");
        IO.println(transacao01.toString());

        IO.println("TRANSACAO 02");
        IO.println(transacao02.toString());
    }



}

