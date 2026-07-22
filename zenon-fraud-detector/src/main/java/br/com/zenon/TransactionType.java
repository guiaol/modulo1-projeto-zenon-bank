package br.com.zenon;

public enum TransactionType {
    CASH_IN, CASH_OUT, DEBIT, PAYMENT, TRANSFER;

    public static boolean hasTransactionType(TransactionType transactionType) {
        for(TransactionType tt : TransactionType.values()) {
            if (tt.name().equals(transactionType.name())) {
                return true;
            }
        }

        return false;
    }
}
