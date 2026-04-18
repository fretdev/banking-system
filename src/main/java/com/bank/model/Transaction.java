package com.bank.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    public enum TransactionType{
        DEPOSIT,
        WITHDRAWAL,
        INTEREST,
        ACCOUNT_CREATION
    }
    private final String transactionId;
    private final String accountNumber;
    private final TransactionType type;
    private final  BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final LocalDateTime timeStamp;

    /**
     * Creates a new transaction record.
     * @param accountNumber the account this transaction belongs to
     * @param type the type of transaction (DEPOSIT, WITHDRAWAL, INTEREST, ACCOUNT_CREATION)
     * @param amount the amount of money involved
     * @param balanceAfter the account balance after this transaction
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Transaction(String accountNumber,TransactionType type,BigDecimal amount,BigDecimal balanceAfter){
        if(accountNumber == null || accountNumber.trim().isEmpty()){
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if(type == null){
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if(amount == null){
            throw new IllegalArgumentException("Amount cannot be empty");
        }
        if(balanceAfter == null){
            throw new IllegalArgumentException("Balance after cannot be null");
        }
        if(type != TransactionType.INTEREST && amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Amount must be positive for non-interest transactions");
        }
        this.transactionId = generateTransactionId();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timeStamp = LocalDateTime.now();
    }
    public Transaction (String transactionId,String accountNumber,Transaction.TransactionType type,BigDecimal amount,BigDecimal balanceAfter,LocalDateTime timeStamp){
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timeStamp = timeStamp;
    }
    private String generateTransactionId(){
        return "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0,4).toUpperCase();
    }
    @Override
    public String toString(){
        return String.format("[%s] %s: $%.2f | Balance after: $%.2f | ID: %s",
                timeStamp, type, amount, balanceAfter, transactionId);
    }


    public String getTransactionId(){
        return transactionId;
    }
    public String getAccountNumber(){
        return accountNumber;
    }
    public TransactionType getType(){
        return type;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public BigDecimal getBalanceAfter(){
        return balanceAfter;
    }
    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }
}
