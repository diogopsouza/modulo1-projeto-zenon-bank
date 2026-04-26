package br.com.zenon.fraud.interfaces;

import br.com.zenon.fraud.Transaction;

import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> findByNameOrig(String nameOrig);
}
