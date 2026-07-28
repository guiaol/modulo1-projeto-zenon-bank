package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    private record ReportTransaction(BigDecimal amount, boolean isFraud) {
    }

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {

        private final static Statistics ZERO = new Statistics(0, 0, BigDecimal.ZERO);

        private Statistics addReportTransaction(ReportTransaction rt) {
            return new Statistics(
                    totalTransactions + 1,
                    totalFrauds + (rt.isFraud ? 1 : 0),
                    totalAmount.add(rt.amount));
        }

        // adiciona uma estatistica em outra, ideal para processamento em paralelo
        private Statistics add(Statistics other) {
            return new Statistics(
                    totalTransactions + other.totalTransactions,
                    totalFrauds + other.totalFrauds,
                    totalAmount.add(other.totalAmount));
        }
    }

    // Solucao 07 professor --------------------------------------------------------------------------------);
    public Statistics generateReport(String fileName) {
        Path path = Path.of(fileName);
        // le linha a linha, ideal para arquivos grandes
        try(Stream<String> lines = Files.lines(path)) {
            return lines
                    .skip(1)
                    .map(this::parseReportTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            Statistics.ZERO,
                            Statistics::addReportTransaction,
                            Statistics::add
                    );
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, ex);
        }
    }

    private Optional<ReportTransaction> parseReportTransaction(String line) {
        try {
            String[] chuncks = line.split(",");

            if (chuncks[2] == null || chuncks[2].trim().isEmpty()) throw new IllegalArgumentException("O valor de amount não pode ser nulo nem vazio.");
            BigDecimal amount = new BigDecimal(chuncks[2]);

            boolean isFraud = chuncks[9].equals("1");
            return Optional.of(new ReportTransaction(amount, isFraud));
        } catch(Exception e) {
            System.out.println("Erro ao fazer parse " + line + " | " + e);
        }

        return Optional.empty();
    }
}





















    // "Solucao 07 pessoal --------------------------------------------------------------------------------");
//    private Stream<String> lines;
//    private String nameFile;
//
//    public TransactionReport(String nameFile) {
//        Objects.requireNonNull(nameFile);
//        this.nameFile = nameFile;
//    }
//
//
//    public void obterTotalLinas() {
//        Stream<String> lines = lerArquivo();
//        Objects.requireNonNull(lines);
//        IO.println("Total de linhas: " + (lines.toList().size()));
//    }
//
//    public void obterTotalFraudes() {
//        Stream<String> lines = lerArquivo();
//        Objects.requireNonNull(lines);
//
//        long count = lines
//                .map(this::parseTransaction)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .filter(Transaction::isFraud)
//                .count();
//
//        IO.println("Total de fraudes: " + count);
//    }
//
//    public void obterTotalTransacionado() {
//        Stream<String> lines = lerArquivo();
//        Objects.requireNonNull(lines);
//
//        BigDecimal amount = lines
//                .map(this::parseTransaction)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .map(Transaction::amount)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        IO.println("Total transacionado " + amount);
//    }
//
//    private Stream<String> lerArquivo() {
//        Path path = Path.of(this.nameFile);
//        Stream<String> lines = null;
//        try {
//            //  lê o arquivo de forma "preguiçosa" (Lazy), linha a linha, sem carregar tudo na RAM.
//            lines = Files.lines(path)
//                    .skip(1);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return lines;
//    }
//
//    private Optional<Transaction> parseTransaction(String line) {
//        try {
//            String[] chuncks = line.split(",");
//            int step = Integer.parseInt(chuncks[0]);
//            TransactionType transactionType = TransactionType.valueOf(chuncks[1]);
//
//            if (chuncks[2] == null || chuncks[2].trim().isEmpty()) throw new IllegalArgumentException("O valor de amount não pode ser nulo nem vazio.");
//            BigDecimal amount = new BigDecimal(chuncks[2]);
//
//            var origin = new TransactionCustomer(chuncks[3], new BigDecimal(chuncks[4]), new BigDecimal(chuncks[5]));
//            var recipient = new TransactionCustomer(chuncks[6], new BigDecimal(chuncks[7]), new BigDecimal(chuncks[8]));
//            boolean isFraud = chuncks[9].equals("1");
//            boolean isFlaggedFraud = chuncks[10].equals("1");
//
//            return Optional.of(new Transaction(step, transactionType, amount, origin, recipient, isFraud, isFlaggedFraud));
//        } catch(Exception e) {
//            System.out.println("Erro ao fazer parse " + line + " | " + e);
//            e.printStackTrace();
//        }
//
//        return Optional.empty();
//    }
//}
