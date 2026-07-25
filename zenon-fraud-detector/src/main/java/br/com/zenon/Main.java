package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    void main() throws IOException {

        Transaction t1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9838.64"),
                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new TransactionCustomer("M1979787155", new BigDecimal(160296.36), new BigDecimal(0.0)),
                false,
                false
        );

        Transaction t2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal(850002.52),
                new TransactionCustomer("C1280323807", new BigDecimal(850002.52), new BigDecimal(0.0)),
                new TransactionCustomer("C873221189",  new BigDecimal(6510099.11), new BigDecimal(7360101.63)),
                true,
                false);

        IO.println(t1);
        IO.println(t2);

        IO.println("professor versao java.nio --------------------------------------------------------------------------------");

        var transactionIngesterNIO = new TransactionIngestor();
        List<Transaction> transactionsNIO = transactionIngesterNIO.readNIO("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactionsNIO.size());

        transactionsNIO.stream().limit(10).forEach(IO::println);

        IO.println("professor versao java.io --------------------------------------------------------------------------------");

        var transactionIngesterIO = new TransactionIngestor();
        List<Transaction> transactionsIO = transactionIngesterIO.readIO("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactionsIO.size());

        transactionsIO.stream().limit(10).forEach(IO::println);

        IO.println("pessoal versao java.io --------------------------------------------------------------------------------");

        TransactionIngestor ti = new TransactionIngestor("data/PS_20174392719_1491204439457_log.csv");
        List<Transaction> tl = ti.readDates();
        IO.println(tl.size());
        tl.stream().limit(10).forEach(IO::println);

        IO.println("tarefa 04 pessoal --------------------------------------------------------------------------------");
//        TransactionIngestor ti2 = new TransactionIngestor("data/paysim_with_bad_data.csv");
//        List<Transaction> tl2 = ti2.readDates();
//
//        tl2.stream().limit(10).forEach(IO::println);

        IO.println("tarefa 04 professor --------------------------------------------------------------------------------");
        List<Transaction> transactionsBadData = transactionIngesterNIO.readNIO("data/paysim_with_bad_data.csv");
        IO.println(transactionsBadData.size());

        transactionsBadData.forEach(IO::println);

        IO.println("tarefa 05 professor --------------------------------------------------------------------------------");
        List<Transaction> transactions05 = transactionIngesterNIO.readNIO("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions05.size());

        var fraudAnalyzer = new FraudAnalyzer(transactions05);

        // Apenas transações onde isFraud == true, imprima o tamanho da lista.
        long fraudCount = fraudAnalyzer.countFrauds();
        IO.println("Total de fraudes: " + fraudCount);

        // Imprima as 3 fraudes de maior valor (amount).
        List<BigDecimal> highestFraudAmounts = fraudAnalyzer.findHighestValueFraudAmounts(3);
        IO.println("Top 3 fraudes de maior valor:");
        highestFraudAmounts.forEach(amount -> IO.println("- %.2f".formatted(amount)));

        /*
         * Obter apenas os nomes dos clientes de origem (nameOrig) dessas fraudes
         * e depois gere uma lista sem repetições (Set ou distinct) com os 5 maiores clientes suspeitos.
         */
        IO.println("Top 5 clientes suspeitos:");
        List<String> suspiciousClients = fraudAnalyzer.findTopSuspiciousClients(5);
        suspiciousClients.forEach(IO::println);

        // Calcule o prejuízo total causado pelas fraudes (soma dos amount).
        BigDecimal totalFraudLoss = fraudAnalyzer.calculateTotalFraudLoss();
        IO.println("Prejuízo total: " + totalFraudLoss);

        // Conte quantas fraudes ocorreram por tipo de transação (CASH_OUT, TRANSFER, etc...).
        Map<TransactionType, Long> fraudCountByTypeTransaction = fraudAnalyzer.countFraudsByTypeTransaction();
        IO.println("Fraudes por tipo: ");
        fraudCountByTypeTransaction.forEach((type, count) -> IO.println("- %s: %d".formatted(type, count)));

        IO.println("tarefa 06 pessoal --------------------------------------------------------------------------------");
//        List<Transaction> transactions = transactionIngesterNIO.readNIO("data/PS_20174392719_1491204439457_log.csv");
//        IO.println("tarefa 06 transaction " + transactions.size());

//        TransactionRepository tlr = new TransactionListRepository(transactions);
//        String name = "C1868032458";
//
//        long start = System.nanoTime();
//        Optional<Transaction> trancation = tlr.findByOriginName(name);
//        long finished = System.nanoTime();
//        IO.println("Tempo para List " + ((finished - start)/1_000_000.0) + " s");
//
//        TransactionRepository tmr = new TransactionMapRepository(transactions);
//        name = "C1868032458";
//        start = System.nanoTime();
//        trancation = tlr.findByOriginName(name);
//        finished = System.nanoTime();
//        IO.println("Tempo para Map " + ((finished - start)/1_000_000.0) + " s");
//
//        if(trancation.isPresent()) {
//           IO.println(trancation.get());
//        } else {
//            IO.println("Transação não encontrada para o cliente " + name);
//        }

        IO.println("tarefa 06 professor --------------------------------------------------------------------------------");
        List<Transaction> transactions = transactionIngesterNIO.readNIO("data/PS_20174392719_1491204439457_log.csv");
//        IO.println("tarefa 06 transaction " + transactions.size());
        TransactionRepository tlrP = new TransactionListRepository(transactions);
        String nameP = "C1868032458";

        long startP = System.nanoTime();
        tlrP
            .findByOriginName(nameP)
            .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente" + nameP));
        long finishedP = System.nanoTime();
        IO.println("Tempo de busca para List (ms) " + ((finishedP - startP)/1000000.0) + " s");

        tlrP = new TransactionMapRepository(transactions);
        String namePMap = "C1868032458";

        startP = System.nanoTime();
        tlrP
                .findByOriginName(nameP)
                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para o cliente" + namePMap));
        finishedP = System.nanoTime();
        IO.println("Tempo de busca para Map (ms) " + ((finishedP - startP)/1000000.0) + " s");
    }
}
