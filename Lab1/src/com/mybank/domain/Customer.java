package com.mybank.domain;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private int id;
    private List<Account> accounts;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
        this.accounts = new ArrayList<>();
    }

    public int getId() { 
        return id; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public List<Account> getAccounts() { 
        return accounts; 
    }
    
    public void addAccount(Account acc) {
        accounts.add(acc);
    }
}