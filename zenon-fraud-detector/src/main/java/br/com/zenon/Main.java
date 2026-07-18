package br.com.zenon;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

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
    }
}
