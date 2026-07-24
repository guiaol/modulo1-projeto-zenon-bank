package br.com.zenon;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(int step,
                          TransactionType transactionType,
                          BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer recipient,
                          boolean isFraud,
                          boolean isFlaggedFraud) {

    // construtor compacto
    public Transaction {

        Objects.requireNonNull(transactionType);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(recipient);

        if (step <= 0) throw new IllegalArgumentException("O valor de step deve ser positivo: " + step);
        if (amount.signum() < 0) throw new IllegalArgumentException("O valor de amount deve ser positivo ou zero: " + amount);
    }

//    public Transaction(int step,
//                       TransactionType transactionType,
//                       BigDecimal amount,
//                       TransactionCustomer origin,
//                       TransactionCustomer recipient,
//                       boolean isFraud,
//                       boolean isFlaggedFraud) {
//
//            if (step < 1) {
//                throw new IllegalArgumentException("step should be positive: " + step);
//            }
//            if (amount.compareTo(BigDecimal.ZERO) < 0) {
//                throw new IllegalArgumentException("amount should be positive: " + amount);
//            }
//            if(transactionType == null
//                    || amount == null
//                    || origin == null
//                    || recipient == null) {
//                throw new IllegalArgumentException();
//            }
//            if (origin.name() == null || origin.name().isEmpty()) {
//                throw new IllegalArgumentException("name should not be empty");
//            }
//            if (origin.newBalance().compareTo(BigDecimal.ZERO) < 0) {
//                throw new IllegalArgumentException("newBalance should be positive: " + origin.newBalance());
//            }
//            if (origin.oldBalance().compareTo(BigDecimal.ZERO) < 0) {
//                throw new IllegalArgumentException("oldBalance should be positive: " + origin.oldBalance());
//            }
//            if (recipient.name() == null || recipient.name().isEmpty()) {
//                throw new IllegalArgumentException("name should not be empty");
//            }
//            if (recipient.newBalance().compareTo(BigDecimal.ZERO) < 0) {
//                throw new IllegalArgumentException("newBalance should be positive: " + recipient.newBalance());
//            }
//            if (recipient.oldBalance().compareTo(BigDecimal.ZERO) < 0) {
//                throw new IllegalArgumentException("oldBalance should be positive: " + recipient.oldBalance());
//            }
//            if (!TransactionType.hasTransactionType(transactionType)) {
//                throw new IllegalArgumentException("No enum constant " + transactionType);
//            }
//            // Arrays.stream(TransactionType.values()).anyMatch(tt -> tt.name().equals(transactionType.name()));
//            this.step = step;
//            this.transactionType = transactionType;
//            this.amount = amount;
//            this.origin = origin;
//            this.recipient = recipient;
//            this.isFraud = isFraud;
//            this.isFlaggedFraud = isFlaggedFraud;
//    }
}
