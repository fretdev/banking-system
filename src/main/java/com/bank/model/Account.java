package com.bank.model;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Account {
    private final String accountNumber;
    private final String holderName;
    private BigDecimal balance;
    private final boolean isActive;
    private final LocalDateTime createdAt;

    public Account(String holderName,BigDecimal balance,boolean isActive,LocalDateTime createdAt,String prefix){
        if(holderName == null || holderName.trim().isEmpty()){
            throw new IllegalArgumentException("Holder name cannot be empty");
        }
        if(balance == null || balance.compareTo(BigDecimal.ZERO)<= 0){
            throw new IllegalArgumentException("Initial balance must be positive");
        }
        this.accountNumber = generateAccountNumber(prefix);
        this.holderName = holderName;
        this.balance = balance;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    protected Account(String accountNumber, String holderName, BigDecimal balance,
                      boolean isActive, LocalDateTime createdAt) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(accountNumber);
    }

    private String generateAccountNumber(String prefix){
        SecureRandom random = new SecureRandom();
        int suffix = random.nextInt(10000000);
        return prefix + String.format("%07d",suffix);
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public String getHolderName(){
        return holderName;
    }
    public BigDecimal getBalance(){
        return balance;
    }
    protected void  addToBalance(BigDecimal amount){
        this.balance = this.balance.add(amount);
    }
    protected void subtractFromBalance(BigDecimal amount){
        this.balance = this.balance.subtract(amount);
    }
    public boolean isActive() {
        return isActive;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }


    public void deposit(BigDecimal amount){
       if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
           throw new IllegalArgumentException("Deposit amount must be positive");
       }
        addToBalance(amount);
    }
    public void withdraw(BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if(amount.compareTo(this.balance) > 0){
            throw new IllegalArgumentException("Insufficient balance");
        }
        subtractFromBalance(amount);
    }
    public abstract BigDecimal calculateInterest();
    public abstract String getType();
}
