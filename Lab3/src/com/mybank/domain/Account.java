package com.mybank.domain;

public class Account {
    protected double balance;

    public Account(double initBalance) {
        this.balance = initBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Сума має бути більшою за нуль!");
        }
        balance += amount;
    }

    public void withdraw(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Сума має бути більшою за нуль!");
        }
        if (amount > balance) {
            throw new Exception("Недостатньо коштів на рахунку!");
        }
        balance -= amount;
    }
}