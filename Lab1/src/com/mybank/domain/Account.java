package com.mybank.domain;

public class Account {
    private double balance;
    private String accountType;

    public Account(double initBalance, String accountType) {
        this.balance = initBalance;
        this.accountType = accountType;
    }

    public double getBalance() { 
        return balance; 
    }
    
    public String getAccountType() { 
        return accountType; 
    }

    public void deposit(double amount) throws Exception {
        if (amount <= 0) throw new Exception("Сума має бути більшою за нуль");
        balance += amount;
    }

    public void withdraw(double amount) throws Exception {
        if (amount <= 0) throw new Exception("Сума має бути більшою за нуль");
        if (amount > balance) throw new Exception("Недостатньо коштів на рахунку!");
        balance -= amount;
    }
}