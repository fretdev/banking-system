package com.bank.ui;

import com.bank.model.Account;
import com.bank.service.Bank;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main{
    static final BigDecimal MIN_AMOUNT = new BigDecimal("100");
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args){

        Bank bank = new Bank();
        bank.loadFromFile("accounts.csv","transactions.csv");
        System.out.println("Status: Synchronizing account interests.....");
        bank.applyInterestToAllSavings();
        String holderName;
        BigDecimal amount;
        boolean isRunning = true;
        int choice;
        while(isRunning) {
            System.out.println("****Banking System****");
            System.out.println("1.Create Savings Account");
            System.out.println("2.Create current Account");
            System.out.println("3.Deposit");
            System.out.println("4.Withdraw");
            System.out.println("5.View Account Details");
            System.out.println("6.View all Accounts");
            System.out.println("7.View Transaction History");
            System.out.println("8.Exit");
            while (true) {
                System.out.print("Enter choice: ");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if (choice >= 1 && choice <= 8) {
                        break;
                    } else {
                        System.out.println("Invalid choice Choose between 1 and 8");
                    }
                } else {
                    System.out.println("Invalid Enter a number");
                    scanner.next();
                }
            }
            switch (choice) {
                case 1 -> {
                    holderName = getValidName("Enter name (letters only): ");
                    amount = getValidAmount("Enter amount: ",MIN_AMOUNT);
                    try {
                        Account account = bank.createSavingsAccount(holderName, amount);
                        System.out.println("***************************************");
                        System.out.println("Savings account created successfully");
                        System.out.println("Account number: " + account.getAccountNumber());
                        System.out.printf("Initial balance: $%.2f%n",account.getBalance());
                        System.out.println("***************************************");
                    } catch (IllegalArgumentException e) {
                        System.out.println("***************************************");
                        System.out.println("Error " + e.getMessage());
                        System.out.println("***************************************");
                    }
                }
                case 2 -> {
                    holderName = getValidName("Enter name (letters only): ");
                    amount = getValidAmount("Enter amount: ",MIN_AMOUNT);
                    try {
                        Account account = bank.createCurrentAccount(holderName, amount);
                        System.out.println("***************************************");
                        System.out.println("Current account created successfully");
                        System.out.println("Account number: " + account.getAccountNumber());
                        System.out.printf("Initial balance: %.2f%n",account.getBalance());
                        System.out.println("***************************************");
                    } catch (IllegalArgumentException e) {
                        System.out.println("***************************************");
                        System.out.println("Error " + e.getMessage());
                        System.out.println("***************************************");
                    }
                }
                case 3 -> {
                    Account acc = getValidAccount(bank);
                    amount = getValidAmount("Enter deposit amount: ",MIN_AMOUNT);
                    try {
                        bank.deposit(acc.getAccountNumber(), amount);
                        System.out.println("***************************************");
                        System.out.println("Deposit successful");
                        System.out.println("New balance " + acc.getBalance());
                        System.out.println("***************************************");
                    } catch (IllegalArgumentException e) {
                        System.out.println("***************************************");
                        System.out.println("Error " + e.getMessage());
                        System.out.println("***************************************");
                    }
                }
                case 4 -> {
                    Account acc = getValidAccount(bank);
                    amount =getValidAmount("Enter amount to withdraw: ",MIN_AMOUNT);
                    try {
                        bank.withdraw(acc.getAccountNumber(), amount);
                        System.out.println("*************************************************");
                        System.out.println("You have withdrawn " + amount + " successfully");
                        System.out.println("New balance: " + acc.getBalance());
                        System.out.println("*************************************************");
                    } catch (IllegalArgumentException e) {
                        System.out.println("***************************************");
                        System.out.println("Error " + e.getMessage());
                        System.out.println("***************************************");
                    }
                }
                case 5 -> {
                    Account acc = getValidAccount(bank);
                    try {
                        System.out.println("***************************************");
                        System.out.println("Bank Details");
                        System.out.println("Account name: " + acc.getHolderName());
                        System.out.println("Account number: " + acc.getAccountNumber());
                        System.out.println("Account balance: " + acc.getBalance());
                        System.out.println("***************************************");
                    } catch (IllegalArgumentException e) {
                        System.out.println("***************************************");
                        System.out.println("Error " + e.getMessage());
                        System.out.println("***************************************");
                    }
                }
                case 6 -> {
                    try {
                        if(bank.getAllAccounts().isEmpty()){
                            System.out.println("***************************************");
                            System.out.println("No accounts");
                            System.out.println("***************************************");
                        }
                        else{
                            for(Account account : bank.getAllAccounts()){
                                System.out.println("*********************************************");
                                System.out.println("Account name: "+account.getHolderName());
                                System.out.println("Account number: "+account.getAccountNumber());
                                System.out.println("Account balance: "+account.getBalance());
                                System.out.println("Created at: "+account.getCreatedAt());
                                System.out.println("**********************************************");
                            }
                            System.out.println("*************************************************");
                            System.out.println("Total bank balance: "+bank.getTotalBalance());
                            System.out.println("*************************************************");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("***************************************");
                        System.out.println("Error: " + e.getMessage());
                        System.out.println("***************************************");
                    }
                }
                case 7 -> {
                   Account acc = getValidAccount(bank);
                    if(bank.getTransactions(acc.getAccountNumber()).isEmpty()){
                        System.out.println("***************************************");
                        System.out.println("No transactions");
                        System.out.println("***************************************");
                    }
                    else{
                        bank.getTransactions(acc.getAccountNumber()).forEach(System.out::println);
                    }
                }
                case 8 -> {
                    while (true) {
                        System.out.print("Do you want to exit the bank(Yes/No): ");
                        String response = scanner.nextLine().trim();
                        if (response.equalsIgnoreCase("yes")) {
                            System.out.println("***************************************");
                            System.out.println("Exiting.......");
                            System.out.println("Goodbye!!!!!!!");
                            System.out.println("***************************************");
                            isRunning = false;
                            break;
                        } else if (response.equalsIgnoreCase("no")) {
                            break;
                        } else {
                            System.out.println("***************************************");
                            System.out.println("Please enter 'yes' or 'no'.");
                            System.out.println("***************************************");
                        }
                    }
                }
            }
        }


        scanner.close();
    }
    public static Account getValidAccount(Bank bank){
        while (true) {
            System.out.print("Enter account number: ");
            String accountNumber = scanner.nextLine().trim();
            if (accountNumber.isEmpty()) {
                System.out.println("Account number cannot be empty");
            } else if (!accountNumber.matches("\\d+")) {
                System.out.println("Account number must contain only digits");
            } else if (accountNumber.length() != 10) {
                System.out.println("Account number must be 10 digits");
            } else {
                try {
                   return bank.findAccount(accountNumber).orElseThrow(()->new IllegalArgumentException("Account not found"));
                } catch (IllegalArgumentException e) {
                    System.out.println("Error " + e.getMessage());
                }
            }
        }
    }
    public static BigDecimal getValidAmount(String message,BigDecimal min){
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(min) >= 0) {
                    return amount;
                } else {
                    System.out.println("Amount must be at least "+min);
                }
            } catch (NumberFormatException e) {
                System.out.println("Enter a numeric value");
            }
        }
    }
    public static String getValidName(String message){
        while (true) {
            System.out.print(message);
            String holderName = scanner.nextLine().trim();
            if (!holderName.isEmpty() && holderName.matches("[a-zA-Z\\s\\-']+")) {
                return holderName;
            } else {
                System.out.println("Invalid name. Use only letters and spaces (e.g, Ahmed Prosper");
            }
        }
    }
}