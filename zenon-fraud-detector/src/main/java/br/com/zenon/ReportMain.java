package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportMain {

    static void main(String[] args) {

        String language = (args.length > 0) ? args[0] : "pt";
        var locale = Locale.of(language);

        var integerFormatter = NumberFormat.getIntegerInstance(locale);
        var currencyFormatter = DecimalFormat.getCurrencyInstance(locale);
        currencyFormatter.setCurrency(Currency.getInstance("USD"));

        ResourceBundle resourceBundle = ResourceBundle.getBundle("report", locale);

        var tr = new TransactionReport();
        String nameFile = "data/PS_20174392719_1491204439457_log.csv";
        Statistics statistics = tr.generateReport(nameFile);

        String fmtTotalTransactions = integerFormatter.format(statistics.totalTransactions());
        String fmtTotalFrauds = integerFormatter.format(statistics.totalFrauds());
        String fmtTotalAmount = currencyFormatter.format(statistics.totalAmount());

        String msgTotalTransactions = resourceBundle.getString("label.total.transactions");
        String msgTotalFrauds = resourceBundle.getString("label.total.frauds");
        String msgTotalAmounts = resourceBundle.getString("label.total.amount");

        IO.println("""
                %s : %s
                %s: %s
                %s: %s
                """.formatted(msgTotalTransactions, fmtTotalTransactions,
                        msgTotalFrauds, fmtTotalFrauds,
                        msgTotalAmounts, fmtTotalAmount
                ));
    }
}
