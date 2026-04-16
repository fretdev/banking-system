package com.bank.service;

import com.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {
    private Bank bank;
    private Account account;
    @BeforeEach
    void setup(){
        bank = new Bank();
        account = bank.createSavingsAccount("Prosper",new BigDecimal("7000"));
    }
    @Test
    void createSavingsAccountShouldAddAccount(){
        Account savingsAccount = bank.createSavingsAccount("Prosper",new BigDecimal("7000"));
        assertEquals(1,bank.getAllAccounts().size());
        assertNotNull(savingsAccount.getAccountNumber());
    }
    @Test
    void createCurrentAccountShouldAddAccount(){
        Account currentAccount = bank.createCurrentAccount("Prosper",new BigDecimal("7000"));
        assertEquals(1,bank.getAllAccounts().size());
        assertNotNull(currentAccount.getAccountNumber());
    }
    @Test
    void findAccountShouldReturnAccountThatExists(){
        Account found = bank.findAccount(account.getAccountNumber());
        assertEquals(account.getAccountNumber(),found.getAccountNumber());
    }
    @Test
    void findAccountShouldThrowErrorWhenNotFound(){
        assertThrows(IllegalArgumentException.class,()->{
            bank.findAccount("null");
        });
    }
    @Test
    void depositShouldIncreaseBalance(){
        bank.deposit(account.getAccountNumber(),new BigDecimal("1000"));
        assertEquals(new BigDecimal("8000"),account.getBalance());
    }
    @Test
    void withdrawShouldReduceBalance(){
        bank.withdraw(account.getAccountNumber(),new BigDecimal("1000"));
        assertEquals(new BigDecimal("6000"),account.getBalance());
    }
    @Test
    void getTransactionsShouldReturnTransactions(){
        bank.deposit(account.getAccountNumber(),new BigDecimal("1000"));
        assertEquals(2,bank.getTransactions(account.getAccountNumber()).size());
    }
    @Test
    void getTotalBalanceShouldReturnTotalAccountsBalance(){
        bank.createCurrentAccount("Mariam",new BigDecimal("7000"));
        assertEquals(new BigDecimal("14000"),bank.getTotalBalance());
    }
    @Test
    void applyInterestToAllSavingsShouldAddInterestToAllSavingsAccount(){
        bank.applyInterest(account.getAccountNumber(),true);
        assertEquals(new BigDecimal("7280.00"),account.getBalance());
    }
}
