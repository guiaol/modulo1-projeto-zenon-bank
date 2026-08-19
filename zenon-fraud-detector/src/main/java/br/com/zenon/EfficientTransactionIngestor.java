package br.com.zenon;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class EfficientTransactionIngestor {

    private String fileName;
    public static final int MAX_SIZE_50k = 50_000;
    public static final int MAX_SIZE_100k = 100_000;
    public static final int MAX_SIZE_10k = 10_000;
    public static final int LINE_BATCH_SIZE = 2_500;

    public EfficientTransactionIngestor(String fileName) {
        this.fileName = fileName;
    }

    public EfficientTransactionIngestor() {
    }

    public void readNIOAsBatch(String fileName, Consumer<List<Transaction>> batchConsumer) {
        Path path = Path.of(fileName);

        try (ExecutorService executor = Executors.newFixedThreadPool(10);
             Stream<String> lines = Files
                     .lines(path)
                     .skip(1)
//                     .limit(MAX_SIZE_10k)
        ){
            var iterator = lines.iterator();

            List<String> lineBatch = new ArrayList<>(LINE_BATCH_SIZE);
            while(iterator.hasNext()) {

                String line = iterator.next();
                lineBatch.add(line);

                if (lineBatch.size() >= LINE_BATCH_SIZE) {
                    IO.println("Executando batch ingestor...");
                    List<String> currentLineBatch = List.copyOf(lineBatch); // faz uma copia para uma variavel imutavel
                    executor.submit(() -> executeBatch(currentLineBatch, batchConsumer));
                    lineBatch.clear();
                }
            }

            if (!lineBatch.isEmpty()) {
                IO.println("Executando batch final ingestor...");
                List<String> currentLineBatch = List.copyOf(lineBatch); // faz uma copia para uma variavel imutavel
                executor.submit(() -> executeBatch(currentLineBatch, batchConsumer));
            }

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, ex);
        }
    }

    private void executeBatch(List<String> lineBatch, Consumer<List<Transaction>> batchConsumer) {
        List<Transaction> transactionBatch = lineBatch
                .stream()
                .map(this::parseTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        batchConsumer.accept(transactionBatch);
    }

    // -------------------------------- solucao professor java.nio2
    public void readNIOAsStream(String fileName, Consumer<Transaction> consumer) {
        Path path = Path.of(fileName);

        try (Stream<String> lines = Files.lines(path)){
            lines
                .skip(1)
                .limit(MAX_SIZE_10k)
                .map(this::parseTransaction)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(consumer);

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
