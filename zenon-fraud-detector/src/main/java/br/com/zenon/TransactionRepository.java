package br.com.zenon;

import java.util.Optional;

public interface TransactionRepository {

    // tarefa 06 pessoal/professor --------------------------------------------------------------------------------
    Optional<Transaction> findByOriginName(String name);

    void save(Transaction transaction);

}
