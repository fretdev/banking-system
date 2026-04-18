package com.bank.service;

import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class  CurrentAccountTest {
    private CurrentAccount account;
    @BeforeEach
    void sampleAccount(){
        account = new CurrentAccount("Prosper",new BigDecimal("7000"),true, LocalDateTime.now());
    }
    @Test
    void depositShouldIncreaseBalance(){
        account.deposit(new BigDecimal("1000"));
        assertEquals(new BigDecimal("8000"),account.getBalance());
    }
    @Test
    void getOverdraftLimitShouldReturnLimit(){
        assertEquals(new BigDecimal("1000"),account.getOverdraftLimit());
    }
    @Test
    void withdrawShouldReduceBalance(){
        account.withdraw(new BigDecimal("2000"));
        assertEquals(new BigDecimal("5000"),account.getBalance());
    }
    @Test
    void withdrawAtOverdraftLimitShouldWork(){
        account.withdraw(new BigDecimal("8000"));
        assertEquals(new BigDecimal("-1000"),account.getBalance());
    }
    @Test
    void withdrawGreaterThanOverdraftLimitShouldThrowError(){
        assertThrows(IllegalArgumentException.class,()->{
            account.withdraw(new BigDecimal("9000"));
        });
    }
    @Test
    void calculateInterest(){
        BigDecimal interest = account.calculateInterest();
        assertEquals(BigDecimal.ZERO,interest);
        assertEquals(new BigDecimal("7000"),account.getBalance());
    }
    @Test
    void getTypeShouldReturnCurrent(){
        assertEquals("CURRENT",account.getType());
    }
}
