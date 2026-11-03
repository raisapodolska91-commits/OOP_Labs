package com.mybank.domain;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String firstName;
    private String lastName;
    private List<Account> accounts;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void addAccount(Account acc) {
        accounts.add(acc);
    }

    public Account getAccount(int index) {
        return accounts.get(index);
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }
}