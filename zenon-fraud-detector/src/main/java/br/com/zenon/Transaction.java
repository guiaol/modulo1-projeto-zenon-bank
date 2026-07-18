package br.com.zenon;

import java.math.BigDecimal;

public record Transaction(int step,
                          TransactionType transactionType,
                          BigDecimal amount,
                          TransactionCustomer origin,
                          TransactionCustomer recipient,
                          boolean isFraud,
                          boolean isFlaggedFraud) {

}
