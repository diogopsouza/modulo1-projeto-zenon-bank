package br.com.zenon.fraud;

import java.math.BigDecimal;

public class Transaction {
    private Integer step;
    private EnumTipoTransacao type;
    private BigDecimal amount;
    private Customer customer;
//    String nameOrig;
//        BigDecimal oldbalanceOrg;
//        BigDecimal newbalanceOrig;

    //        String nameDest;
//        BigDecimal oldbalanceDest;
//        BigDecimal newbalanceDest;
    private ClienteDestino destino;
    private boolean isFraud;
    private boolean isFlaggedFraud;

    public Transaction(Integer step, EnumTipoTransacao type, BigDecimal amount, Customer customer, ClienteDestino destino, boolean isFraud, boolean isFlaggedFraud) {
        this.step = step;
        this.type = type;
        this.amount = amount;
        this.customer = customer;
        this.destino = destino;
        this.isFraud = isFraud;
        this.isFlaggedFraud = isFlaggedFraud;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public EnumTipoTransacao getType() {
        return type;
    }

    public void setType(EnumTipoTransacao type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ClienteDestino getDestino() {
        return destino;
    }

    public void setDestino(ClienteDestino destino) {
        this.destino = destino;
    }

    public boolean isFraud() {
        return isFraud;
    }

    public void setFraud(boolean fraud) {
        isFraud = fraud;
    }

    public boolean isFlaggedFraud() {
        return isFlaggedFraud;
    }

    public void setFlaggedFraud(boolean flaggedFraud) {
        isFlaggedFraud = flaggedFraud;
    }

    @Override
    public String toString() {
        return "Transaction{" + "step=" + step + ", type=" + type + ", amount=" + amount + ", customer=" + customer + ", destino=" + destino + ", isFraud=" + isFraud + ", isFlaggedFraud=" + isFlaggedFraud + '}';
    }
}
