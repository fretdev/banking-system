package com.bank.service;

import com.bank.model.Account;
import com.bank.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavingsAccountTest {
    private Account account;
    @BeforeEach
    void accountSample(){
        account = new SavingsAccount("Prosper",new BigDecimal("7000"),true, LocalDateTime.now());
    }
    @Test
    void calculateInterestShouldAddInterestToBalance(){
        BigDecimal interest = account.calculateInterest();
        assertEquals(new BigDecimal("280.00"),interest);
        assertEquals(new BigDecimal("7280.00"),account.getBalance(),()->"Calculate interest should add interest to balance");
    }
    @Test
    void getTypeShouldReturnType(){
        assertEquals("SAVINGS",account.getType(),()->"The getType method must return account type");
    }
}
