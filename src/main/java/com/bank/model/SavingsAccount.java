package com.bank.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SavingsAccount extends Account{
    private final BigDecimal interestRate;
    public SavingsAccount(String holderName, BigDecimal balance, boolean isActive, LocalDateTime createdAt){
        super(holderName,balance,isActive,createdAt,"001");
        this.interestRate = new BigDecimal("0.04");
    }
    public SavingsAccount(String accountNumber, String holderName, BigDecimal balance,
                          boolean isActive, LocalDateTime createdAt) {
        super(accountNumber, holderName, balance, isActive, createdAt);
        this.interestRate = new BigDecimal("0.04");
    }
    @Override
    public String getType(){
        return "SAVINGS";
    }
    @Override
    public BigDecimal calculateInterest(){
        BigDecimal currentBalance = getBalance();
        BigDecimal interest = currentBalance.multiply(interestRate);
        addToBalance(interest);
        return interest;
    }
}
