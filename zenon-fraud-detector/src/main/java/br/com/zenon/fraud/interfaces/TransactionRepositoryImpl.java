package br.com.zenon.fraud.interfaces;

import br.com.zenon.fraud.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final List<Transaction> transactions;

    public TransactionRepositoryImpl(List<Transaction> transactions) {
        this.transactions = transactions;
    }


    @Override
    public Optional<Transaction> findByNameOrig(String nameOrig) {
        if(Objects.isNull(nameOrig)) {
            throw new IllegalArgumentException("Nome informado é inválido");
        }
        return this.transactions.stream().filter(transaction -> transaction.getCustomer().getNameOrig().equalsIgnoreCase(nameOrig)).findFirst();
    }
}
