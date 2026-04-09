package com.bank.service;

import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.SavingsAccount;
import com.bank.model.Transaction;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Bank {
    private final Map<String,Account> accounts;
    private final List<Transaction> transactions;

    public Bank(){
        this.accounts = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public Account createSavingsAccount(String holderName, BigDecimal initialDeposit){
        Account newAccount = new SavingsAccount(holderName,initialDeposit,true, LocalDateTime.now());

        accounts.put(newAccount.getAccountNumber(),newAccount);

        Transaction creationTransaction = new Transaction(newAccount.getAccountNumber(),Transaction.TransactionType.ACCOUNT_CREATION,initialDeposit,initialDeposit);

        transactions.add(creationTransaction);
        saveToFile("accounts.csv","transactions.csv");
        return newAccount;
    }
    public Account createCurrentAccount(String holderName,BigDecimal initialDeposit){
        Account newAccount = new CurrentAccount(holderName,initialDeposit,true,LocalDateTime.now());
        accounts.put(newAccount.getAccountNumber(),newAccount);
        Transaction creationTransaction = new Transaction(newAccount.getAccountNumber(),Transaction.TransactionType.ACCOUNT_CREATION,initialDeposit,initialDeposit);
        transactions.add(creationTransaction);
        saveToFile("accounts.csv","transactions.csv");
        return newAccount;
    }
    public Account findAccount(String accountNumber){
        nullChecker(accountNumber);
        Account found = accounts.get(accountNumber);
        if(found == null){
            throw new IllegalArgumentException("Account not found: "+accountNumber);
        }
        return found;
    }
    public void deposit(String accountNumber,BigDecimal amount){
        Account account = findAccount(accountNumber);
        account.deposit(amount);

        Transaction depositTransaction = new Transaction(accountNumber,Transaction.TransactionType.DEPOSIT,amount,account.getBalance());
        transactions.add(depositTransaction);
        saveToFile("accounts.csv","transactions.csv");
    }
    public void withdraw(String accountNumber,BigDecimal amount){
        Account account = findAccount(accountNumber);
        account.withdraw(amount);

        Transaction withdrawTransaction = new Transaction(accountNumber,Transaction.TransactionType.WITHDRAWAL,amount,account.getBalance());
        transactions.add(withdrawTransaction);
        saveToFile("accounts.csv","transactions.csv");
    }
    public void applyInterest(String accountNumber){
        Account account = findAccount(accountNumber);
        BigDecimal interest = account.calculateInterest();

        Transaction transaction = new Transaction(accountNumber, Transaction.TransactionType.INTEREST,interest,account.getBalance());
        transactions.add(transaction);
        saveToFile("accounts.csv","transactions.csv");
    }
    public List<Account> getAllAccounts(){
        return new ArrayList<>(accounts.values());
    }
    public List<Transaction> getTransactions(String accountNumber){
        nullChecker(accountNumber);
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }
    public BigDecimal getTotalBalance(){
        return accounts.values().stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void nullChecker(String accountNumber){
        if(accountNumber == null || accountNumber.trim().isEmpty()){
            throw new IllegalArgumentException("Account number cannot be empty");
        }
    }

    public void saveToFile(String accountsFile,String transactionFile){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(accountsFile))){
            writer.write("accountNumber,holderName,balance,isActive,createdAt,type,overdraftLimit");
            writer.newLine();
            for(Account acc : accounts.values()){
                String line = acc.getAccountNumber()+","+acc.getHolderName()+","+acc.getBalance()+","+acc.isActive()+","+acc.getCreatedAt().format(formatter)+","+acc.getType()+",";
                if(acc instanceof CurrentAccount ca){
                    line += ca.getOverdraftLimit();
                }
                writer.write(line);
                writer.newLine();
            }
        }
        catch (IOException e){
            System.out.println("Error saving accounts: "+ e.getMessage());
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFile))){
            writer.write("transactionId,accountNumber,type,amount,balanceAfter,timestamp");
            writer.newLine();
            for(Transaction transaction : transactions){
                String line = transaction.getTransactionId()+","+transaction.getAccountNumber()+","+transaction.getType()+","+transaction.getAmount()+","+transaction.getBalanceAfter()+","+transaction.getTimeStamp().format(formatter);
                writer.write(line);
                writer.newLine();
            }
        }
        catch(IOException e){
            System.out.println("Error saving transactions "+e.getMessage());
        }
    }
    public void loadFromFile(String accountFile,String transactionFile){
        accounts.clear();
        transactions.clear();
        System.out.println("Attempting to read: "+ accountFile);

        try(BufferedReader reader = new BufferedReader(new FileReader(accountFile))){
            String header = reader.readLine();
            System.out.println("Header: "+header);
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                String accountNumber = parts[0];
                String holderName = parts[1];
                BigDecimal balance = new BigDecimal(parts[2]);
                boolean isActive = Boolean.parseBoolean(parts[3]);
                LocalDateTime createdAt = LocalDateTime.parse(parts[4],DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String type = parts[5];
                String overdraftLimitStr = parts.length > 6 ? parts[6]:"";
                Account account;
                if(type.equals("SAVINGS")){
                    account= new SavingsAccount(accountNumber,holderName,balance,isActive,createdAt);
                    System.out.println("loaded Savings Account: "+accountNumber);
                    accounts.put(account.getAccountNumber(),account);
                }
                else{
                    BigDecimal overdraftLimit = overdraftLimitStr.isEmpty()?new BigDecimal("1000"):new BigDecimal(overdraftLimitStr);
                    account = new CurrentAccount(accountNumber,holderName,balance,isActive,createdAt,overdraftLimit);
                    System.out.println("Loaded Current account: "+accountNumber);
                    accounts.put(account.getAccountNumber(),account);
                }
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        catch (IOException e){
            System.out.println("Error reading: "+e.getMessage());
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(transactionFile))){
            String header = reader.readLine();
            String line;
            while((line = reader.readLine())!= null){
                String[] part = line.split(",");
                String transactionId = part[0];
                String accountNumber = part[1];
                Transaction.TransactionType type = Transaction.TransactionType.valueOf(part[2]);
                BigDecimal amount = new BigDecimal(part[3]);
                BigDecimal balanceAfter = new BigDecimal(part[4]);
                LocalDateTime timeStamp = LocalDateTime.parse(part[5],DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                Transaction transaction = new Transaction(transactionId,accountNumber,type,amount,balanceAfter,timeStamp);
                transactions.add(transaction);
            }
            System.out.println("Loaded "+transactions.size()+" transactions");
        }
        catch(FileNotFoundException e){
            System.out.println("Error reading: "+e.getMessage());
        }
        catch(IOException e){
            System.out.println("File not found");
        }
    }
}
