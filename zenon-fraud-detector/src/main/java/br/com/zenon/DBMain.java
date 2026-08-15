package br.com.zenon;

import java.math.BigDecimal;
import java.util.List;

public class DBMain {

    void main() {
//        ConnectionFactory.getConnection();
//        IO.println("Conexão com o BD criada! :)");

        var repositoy = new TransactionSQLRepositoy();
//        repositoy.findByOriginName("C1000001")
//                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: C1000001"));
//
//        repositoy.findByOriginName("C12345")
//                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: C12345"));

        var transactionIngester = new TransactionIngestor();

        long startTimeSql = System.nanoTime();
        List<Transaction> transactions = transactionIngester.readNIO("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions.size());

        IO.println("Iniciando adição das transações no BD ...");
        transactions.stream().forEach(repositoy::save);

        long endTimeSql = System.nanoTime();
        IO.println("Tempo de inserção (ms) " + ((endTimeSql - startTimeSql)/1000000.0) + " s");

        repositoy.findByOriginName("C1231006815")
                .ifPresentOrElse(IO::println, () -> IO.println("Transação não encontrada para: C1000001"));

//        Transaction t1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9838.64"),
//                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
//                new TransactionCustomer("M1979787155", new BigDecimal(160296.36), new BigDecimal(0.0)),
//                false,
//                false
//        );
//        repositoy.save(t1);
    }
}
