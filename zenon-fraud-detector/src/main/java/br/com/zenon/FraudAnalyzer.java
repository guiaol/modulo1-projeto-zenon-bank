package br.com.zenon;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    public long countFrauds() {
        return fraudStream()
                .count();
    }

    public List<BigDecimal> findHighestValueFraudAmounts(int limit) {
        return highValueFraudStream()
                .map(Transaction::amount)
                .limit(limit)
                .toList();
    }


    public List<String> findTopSuspiciousClients(int limit) {
        return highValueFraudStream()
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(limit)
                .toList();
    }

    public BigDecimal calculateTotalFraudLoss() {
        return fraudStream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public Map<TransactionType, Long> countFraudsByTypeTransaction() {
        return fraudStream()
                .collect(Collectors.groupingBy(Transaction::transactionType, Collectors.counting()));
    }

    @NotNull
    private Stream<Transaction> fraudStream() {
        return transactions
                .stream()
                .filter(Transaction::isFraud);
    }

    @NotNull
    private Stream<Transaction> highValueFraudStream() {
        return fraudStream()
                .sorted(Comparator.comparing(Transaction::amount).reversed());
    }
}
