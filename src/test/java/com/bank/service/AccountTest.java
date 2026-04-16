package com.bank.service;

import com.bank.model.Account;
import com.bank.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest{
    private Account account;
    @BeforeEach
    void accountSample(){
        account = new SavingsAccount("Prosper",new BigDecimal("7000"),true, LocalDateTime.now());
    }
    @Test
    void depositShouldIncreaseBalance(){
        account.deposit(new BigDecimal("1000"));
        assertEquals(new BigDecimal("8000"),account.getBalance(),()->"Deposit on account should increase account balance");
    }
    @Test
    void withdrawShouldReduceBalance(){
        account.withdraw(new BigDecimal("1000"));
        assertEquals(new BigDecimal("6000"),account.getBalance(),()->"Withdrawal on account should reduce balance");
    }
    @Test
    void zeroDepositAmountShouldThrowError(){
        assertThrows(IllegalArgumentException.class,()->{
            account.deposit(new BigDecimal("0"));
        });
    }
    @Test
    void negativeDepositAmountShouldThrowError(){
        assertThrows(IllegalArgumentException.class,()->{
            account.deposit(new BigDecimal("-10"));
        });
    }
    @Test
    void ZeroWithdrawAmountShouldThrowError(){
        assertThrows(IllegalArgumentException.class,()->{
            account.withdraw(new BigDecimal("0"));
        });
    }
    @Test
    void negativeWithdrawAmountShouldThrowError(){
        assertThrows(IllegalArgumentException.class,()->{
            account.withdraw(new BigDecimal("-10"));
        });
    }
    @Test
    void withdrawAmountGreaterThanBalanceShouldThrowError(){
        assertThrows(IllegalArgumentException.class,()->{
            account.withdraw(new BigDecimal("10000"));
        });
    }
}