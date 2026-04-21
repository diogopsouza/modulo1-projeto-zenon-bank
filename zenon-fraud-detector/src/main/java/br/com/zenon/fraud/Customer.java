package br.com.zenon.fraud;

import java.math.BigDecimal;

public class Customer {
    private String nameOrig;
    private BigDecimal oldbalanceOrg;
    private BigDecimal newbalanceOrig;


    public Customer(String nameOrig, BigDecimal oldbalanceOrg, BigDecimal newbalanceOrig) {
        this.nameOrig = nameOrig;
        this.oldbalanceOrg = oldbalanceOrg;
        this.newbalanceOrig = newbalanceOrig;
    }


    public String getNameOrig() {
        return nameOrig;
    }

    public void setNameOrig(String nameOrig) {
        this.nameOrig = nameOrig;
    }

    public BigDecimal getOldbalanceOrg() {
        return oldbalanceOrg;
    }

    public void setOldbalanceOrg(BigDecimal oldbalanceOrg) {
        this.oldbalanceOrg = oldbalanceOrg;
    }

    public BigDecimal getNewbalanceOrig() {
        return newbalanceOrig;
    }

    public void setNewbalanceOrig(BigDecimal newbalanceOrig) {
        this.newbalanceOrig = newbalanceOrig;
    }

    @Override
    public String toString() {
        return "Customer{" + "nameOrig='" + nameOrig + '\'' + ", oldbalanceOrg=" + oldbalanceOrg + ", newbalanceOrig=" + newbalanceOrig + '}';
    }
}
