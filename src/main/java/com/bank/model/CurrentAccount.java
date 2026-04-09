package com.bank.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CurrentAccount extends Account{
    private final BigDecimal interestRate;
    private final BigDecimal overdraftLimit;

    public CurrentAccount(String holderName, BigDecimal balance, boolean isActive, LocalDateTime createdAt){
        super(holderName,balance,isActive,createdAt,"002");
        this.interestRate = new BigDecimal("0.01");
        this.overdraftLimit =new BigDecimal("1000");
    }
    public CurrentAccount(String accountNumber, String holderName, BigDecimal balance,
                          boolean isActive, LocalDateTime createdAt, BigDecimal overdraftLimit) {
        super(accountNumber, holderName, balance, isActive, createdAt);
        this.interestRate = new BigDecimal("0.01");
        this.overdraftLimit = overdraftLimit;
    }
    public BigDecimal getOverdraftLimit(){
        return overdraftLimit;
    }
    @Override
    public String getType(){
        return "CURRENT";
    }
    @Override
    public void withdraw(BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
           throw new IllegalArgumentException("Withdrawal must be positive");
        }
        BigDecimal totalWithdrawableAmount = getBalance().add(overdraftLimit);
        if(amount.compareTo(totalWithdrawableAmount) > 0){
            throw new IllegalArgumentException("Amount exceeds withdrawal limit.");
        }
        subtractFromBalance(amount);
    }

    @Override
    public BigDecimal calculateInterest() {
        BigDecimal currentBalance = getBalance();
        BigDecimal interest = currentBalance.multiply(interestRate);
        addToBalance(interest);
        return interest;
    }
}
