package com.bank.service;

import com.bank.model.Account;
import com.bank.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {
    Bank bank;
    Account account;
    String accountNumber;
    @BeforeEach
    void setUp(){
        bank = new Bank();
        account = bank.createSavingsAccount("Prosper",new BigDecimal("8000"));
        accountNumber = account.getAccountNumber();
    }
    @Test
    void constructorShouldCreateValidDepositTransaction(){
        BigDecimal depositAmount = new BigDecimal("1000");
        BigDecimal balanceAfter = new BigDecimal("9000");

        Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.DEPOSIT,depositAmount,balanceAfter);
        assertNotNull(transaction.getTransactionId());
        assertNotNull(transaction.getTimeStamp());

        assertEquals(accountNumber,transaction.getAccountNumber());
        assertEquals(Transaction.TransactionType.DEPOSIT,transaction.getType());
        assertEquals(depositAmount,transaction.getAmount());
        assertEquals(balanceAfter,transaction.getBalanceAfter());
    }
    @Test
    void constructorShouldCreateValidWithdrawTransaction(){
        BigDecimal withdrawAmount = new BigDecimal("1000");
        BigDecimal balanceAfter = new BigDecimal("7000");

        Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.WITHDRAWAL,withdrawAmount,balanceAfter);
        assertNotNull(transaction.getTransactionId());
        assertNotNull(transaction.getTimeStamp());
        assertEquals(accountNumber,transaction.getAccountNumber());
        assertEquals(Transaction.TransactionType.WITHDRAWAL,transaction.getType());
        assertEquals(withdrawAmount,transaction.getAmount());
        assertEquals(balanceAfter,transaction.getBalanceAfter());
    }
    @Test
    void constructorWithEmptyAccountNumberShouldThrowException(){
        assertThrows(IllegalArgumentException.class,()->{
            new Transaction("", Transaction.TransactionType.WITHDRAWAL,new BigDecimal("1000"),new BigDecimal("7000"));
        });
    }
    @Test
    void constructorWithNullAccountNumberShouldThrowException(){
        assertThrows(IllegalArgumentException.class,()->{
            new Transaction(null, Transaction.TransactionType.DEPOSIT,new BigDecimal("1000"),new BigDecimal("9000"));
        });
    }
    @Test
    void constructorWithNullTypeShouldThrowException(){
        assertThrows(IllegalArgumentException.class,()->{
            new Transaction(accountNumber,null,new BigDecimal("1000"),new BigDecimal("9000"));
        });
    }
    @Test
    void constructorWithNullAmountShouldThrowException(){
        assertThrows(IllegalArgumentException.class,()->{
           new Transaction(accountNumber, Transaction.TransactionType.DEPOSIT,null,new BigDecimal("9000"));
        });
    }
    @Test
    void constructorWithNullBalanceAfterShouldThrowException(){
        assertThrows(IllegalArgumentException.class,()->{
            new Transaction(accountNumber, Transaction.TransactionType.DEPOSIT,new BigDecimal("1000"),null);
        });
    }
    @Test
    void constructorWithZeroAmountForDepositShouldThrowException(){
        assertThrows(IllegalArgumentException.class,()->{
            new Transaction(accountNumber, Transaction.TransactionType.DEPOSIT,BigDecimal.ZERO,new BigDecimal("8000"));
        });
    }
    @Test
    void constructorWithZeroAmountForWithdrawShouldThrowException(){
        assertThrows(IllegalArgumentException.class,()->{
            new Transaction(accountNumber, Transaction.TransactionType.WITHDRAWAL,BigDecimal.ZERO,new BigDecimal("8000"));
        });
    }
    @Test
    void constructorWithZeroAmountForInterestShouldWork(){
        Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.INTEREST,BigDecimal.ZERO,new BigDecimal("8000"));
        assertEquals(BigDecimal.ZERO,transaction.getAmount());
    }
    @Test
    void toStringNotBeNull(){
        Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.ACCOUNT_CREATION,new BigDecimal("4000"),new BigDecimal("4000"));
        assertNotNull(transaction.toString());
        assertTrue(transaction.toString().contains(transaction.getTransactionId()));
    }
}
