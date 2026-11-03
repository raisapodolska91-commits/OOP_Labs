package com.mybank.domain;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Customer> customers;

    public Bank() {
        customers = new ArrayList<>();
        // Додаємо тестових клієнтів, щоб було з чим працювати
        Customer c1 = new Customer(1, "John Doe");
        c1.addAccount(new Account(200.00, "Checking"));
        c1.addAccount(new Account(1500.00, "Savings"));
        
        Customer c2 = new Customer(2, "Jane Smith");
        c2.addAccount(new Account(350.50, "Checking"));

        customers.add(c1);
        customers.add(c2);
    }

    public List<Customer> getCustomers() { 
        return customers; 
    }

    public Customer getCustomer(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) return c;
        }
        return null; // Якщо клієнта з таким ID немає
    }
}