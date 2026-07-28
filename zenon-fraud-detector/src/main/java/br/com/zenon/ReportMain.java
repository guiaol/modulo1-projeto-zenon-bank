package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

public class ReportMain {

    static void main() {
        TransactionReport tr = new TransactionReport();
        String nameFile = "data/PS_20174392719_1491204439457_log.csv";
        Statistics statistics = tr.generateReport(nameFile);
        IO.println("Total de linhas %d".formatted(statistics.totalTransactions()));
        IO.println("Total de fraudes %d".formatted(statistics.totalFrauds()));
        IO.println("Total do valor transacionado %.2f".formatted(statistics.totalAmount()));
    }

}
