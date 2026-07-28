package br.com.zenon;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository {

    // tarefa 06 professor --------------------------------------------------------------------------------
    private Map<String, Transaction> transactionByOriginName = new HashMap<>(); //

    public TransactionMapRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);

        this.transactionByOriginName = transactions
                .stream()
                .collect(
                        Collectors.toMap(
                                // key
                                transaction -> transaction.origin().name(),
                                // value
                                Function.identity(), // mesma coisa que transaction -> transaction, pega uma coisa e retorna a mesma coisa.
                                (existente, novo) -> novo
                        )
                );
    }

    @Override
    public Optional<Transaction> findByOriginName(String name) {
        return Optional.ofNullable(transactionByOriginName.get(name));
    }


    // tarefa 06 pessoal --------------------------------------------------------------------------------
//    Map<Long, Transaction> transactions = new HashMap<>();
//
//    public TransactionMapRepository(List<Transaction> transactionsList) {
//        Objects.requireNonNull(transactionsList);
//
//        long i = 1;
//        for (Transaction t : transactionsList) {
//            transactions.put(i, t);
//            i++;
//        }
//    }
//
//    @Override
//    public Optional<Transaction> findByOriginName(String name) {
//
//        AtomicReference<Transaction> t1 = new AtomicReference<>();
//        transactions.forEach((key, value) -> {
//            if (name.equals(value)) {
//                t1.set(transactions.get(key));
//            }
//        });
//
//        if (t1.get() != null) {
//            return Optional.of(t1.get());
//        }
//
//        return Optional.empty();
//    }

}
