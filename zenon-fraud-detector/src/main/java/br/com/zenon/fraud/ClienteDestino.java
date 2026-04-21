package br.com.zenon.fraud;

import java.math.BigDecimal;

public class ClienteDestino {

    private String nameDest;
    private BigDecimal oldbalanceDest;
    private BigDecimal newbalanceDest;

    public ClienteDestino(String nameDest, BigDecimal oldbalanceDest, BigDecimal newbalanceDest) {
        this.nameDest = nameDest;
        this.oldbalanceDest = oldbalanceDest;
        this.newbalanceDest = newbalanceDest;
    }

    public String getNameDest() {
        return nameDest;
    }

    public void setNameDest(String nameDest) {
        this.nameDest = nameDest;
    }

    public BigDecimal getOldbalanceDest() {
        return oldbalanceDest;
    }

    public void setOldbalanceDest(BigDecimal oldbalanceDest) {
        this.oldbalanceDest = oldbalanceDest;
    }

    public BigDecimal getNewbalanceDest() {
        return newbalanceDest;
    }

    public void setNewbalanceDest(BigDecimal newbalanceDest) {
        this.newbalanceDest = newbalanceDest;
    }

    @Override
    public String toString() {
        return "ClienteDestino{" +
                "nameDest='" + nameDest + '\'' +
                ", oldbalanceDest=" + oldbalanceDest +
                ", newbalanceDest=" + newbalanceDest +
                '}';
    }
}
