package br.com.zenon;

public class IngestonMain {

    void main() {

        var repositoy = new TransactionSQLRepositoy();
        var transactionIngester = new EfficientTransactionIngestor();

        long startTimeSql = System.nanoTime();
        transactionIngester.readNIOAsBatch("data/PS_20174392719_1491204439457_log.csv",
                repositoy::saveAll);

        long endTimeSql = System.nanoTime();
        IO.println("Tempo de ingestão no BD (ms) " + ((endTimeSql - startTimeSql)/1000000.0));
    }
}
