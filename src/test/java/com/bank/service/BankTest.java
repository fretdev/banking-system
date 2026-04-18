package com.bank.service;

import com.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

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
        int sizeBefore = bank.getAllAccounts().size();
        Account savingsAccount = bank.createSavingsAccount("Prosper",new BigDecimal("7000"));
        assertEquals(sizeBefore + 1 ,bank.getAllAccounts().size());
        assertNotNull(savingsAccount.getAccountNumber());
    }
    @Test
    void createCurrentAccountShouldAddAccount(){
        int sizeBefore = bank.getAllAccounts().size();
        Account currentAccount = bank.createCurrentAccount("Prosper",new BigDecimal("7000"));
        assertEquals(sizeBefore + 1,bank.getAllAccounts().size());
        assertNotNull(currentAccount.getAccountNumber());
    }
    @Test
    void findAccountShouldReturnAccountThatExists(){
        Optional<Account> found = bank.findAccount(account.getAccountNumber());
        assertTrue(found.isPresent());
        assertEquals(account.getAccountNumber(),found.get().getAccountNumber());
    }
    @Test
    void findAccountShouldReturnEmptyWhenAccountNotFound(){
        Optional<Account> found = bank.findAccount("Non Existent");
        assertFalse(found.isPresent());
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
