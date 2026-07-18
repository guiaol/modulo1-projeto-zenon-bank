package br.com.zenon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    private String fileName;

    public TransactionIngestor(String fileName) {
        this.fileName = fileName;
    }

    public List<Transaction>  ingestaoDados() throws IOException {
        List<Transaction> transactionList = new ArrayList<>();

        int data;
        try(FileReader fr = new FileReader(this.fileName)) {
            BufferedReader br = new BufferedReader(fr);
            String allLines = br.readAllAsString();

            String[] linesFile = allLines.split("\n");

            for (int i = 1; i <= 1000; i++) {
                String line = linesFile[i];
                String[] lineSplit = line.split(",");


                int step  = Integer.parseInt(lineSplit[0]);

                TransactionType transactionType = TransactionType.valueOf(lineSplit[1]);

                BigDecimal amount = new BigDecimal(lineSplit[2]);

                TransactionCustomer origin = new TransactionCustomer(lineSplit[3], new BigDecimal(lineSplit[4]),new BigDecimal(lineSplit[5]));

                TransactionCustomer recipient = new TransactionCustomer(lineSplit[6], new BigDecimal(lineSplit[7]),new BigDecimal(lineSplit[8]));

                boolean isFraud = lineSplit[9].equals("1");

                boolean isFlaggedFraud = lineSplit[10].equals("1");

                Transaction t = new Transaction(step, transactionType, amount, origin, recipient, isFraud, isFlaggedFraud);

                transactionList.add(t);
            }

        }

        return transactionList;
    }
}
