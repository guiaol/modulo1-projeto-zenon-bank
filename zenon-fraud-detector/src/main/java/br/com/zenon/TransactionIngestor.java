package br.com.zenon;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TransactionIngestor {

    private String fileName;
    public static final int MAX_SIZE_50k = 50_000;
    public static final int MAX_SIZE_100k = 100_000;

    public TransactionIngestor(String fileName) {
        this.fileName = fileName;
    }

    public TransactionIngestor() {
    }

    // -------------------------------- solucao professor java.nio2
    public List<Transaction> readNIO(String fileName) {
        Path path = Path.of(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
//                    .limit(MAX_SIZE_100k)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, ex);
        }
    }

    // -------------------------------- solucao professor java.io
    public List<Transaction> readIO(String fileName) {

        ArrayList<Transaction> transacations = new ArrayList<>();

        try(FileInputStream fis = new FileInputStream(fileName)) {
            Scanner scanner = new Scanner(fis);

            int lineCount = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineCount++;

                if (lineCount == 1) {
                    continue;
                }

                if (lineCount > 1001) {
                    break;
                }

                Transaction t = parseTransaction(line).get();
                transacations.add(t);
            }

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, ex);
        }

        return transacations;
    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] chuncks = line.split(",");
            int step = Integer.parseInt(chuncks[0]);
            TransactionType transactionType = TransactionType.valueOf(chuncks[1]);

            if (chuncks[2] == null || chuncks[2].trim().isEmpty()) throw new IllegalArgumentException("O valor de amount não pode ser nulo nem vazio.");
            BigDecimal amount = new BigDecimal(chuncks[2]);

            var origin = new TransactionCustomer(chuncks[3], new BigDecimal(chuncks[4]), new BigDecimal(chuncks[5]));
            var recipient = new TransactionCustomer(chuncks[6], new BigDecimal(chuncks[7]), new BigDecimal(chuncks[8]));
            boolean isFraud = chuncks[9].equals("1");
            boolean isFlaggedFraud = chuncks[10].equals("1");

            return Optional.of(new Transaction(step, transactionType, amount, origin, recipient, isFraud, isFlaggedFraud));
        } catch(Exception e) {
            System.out.println("Erro ao fazer parse " + line + " | " + e);
//            e.printStackTrace();
        }

        return Optional.empty();
    }

    // --------------------------------  solucao pessoal
    public List<Transaction> readDates() throws IOException {
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

//                TransactionType transactionType = TransactionType.valueOf(lineSplit[1]);

                BigDecimal amount = new BigDecimal(lineSplit[2]);

                TransactionCustomer origin = new TransactionCustomer(lineSplit[3], new BigDecimal(lineSplit[4]),new BigDecimal(lineSplit[5]));

                TransactionCustomer recipient = new TransactionCustomer(lineSplit[6], new BigDecimal(lineSplit[7]),new BigDecimal(lineSplit[8]));

                boolean isFraud = lineSplit[9].equals("1");

                boolean isFlaggedFraud = lineSplit[10].equals("1");

                try {
                    Transaction t = new Transaction(step, TransactionType.valueOf(lineSplit[1]), amount, origin, recipient, isFraud, isFlaggedFraud);

                    transactionList.add(t);
                } catch (Exception e) {
                    IO.println("Erro: " + line + " | " + e.getCause() + " : " +  e.getMessage());
                }
            }

        }

        return transactionList;
    }
}
