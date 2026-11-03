package com.mybank.gui;

import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.Account;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.SavingsAccount;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class MatisseDemo extends JFrame {

    private JComboBox<String> clientsCombo;
    private JComboBox<String> accountsCombo;
    private JTextArea outputArea;

    public MatisseDemo() {
        setTitle("Banking GUI Demo (Lab 4)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. Верхня панель (Списки)
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        topPanel.add(new JLabel("Select Customer:"));
        clientsCombo = new JComboBox<>();
        topPanel.add(clientsCombo);

        topPanel.add(new JLabel("Select Account:"));
        accountsCombo = new JComboBox<>();
        topPanel.add(accountsCombo);

        // 2. Центральна панель (Текст)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // 3. Нижня панель (Кнопки)
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton showButton = new JButton("Show");
        JButton reportButton = new JButton("Report");
        JButton aboutButton = new JButton("About");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");

        buttonPanel.add(showButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(aboutButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Логіка
        loadDataFromFile();
        populateClients();

        // Обробники подій
        clientsCombo.addActionListener(e -> updateAccountsCombo());
        showButton.addActionListener(e -> showCustomerDetails());
        reportButton.addActionListener(e -> generateReport());
        
        aboutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Banking GUI Demo\nLab 4 - Matisse", "About", JOptionPane.INFORMATION_MESSAGE);
        });

        depositButton.addActionListener(e -> {
            try {
                Account acc = getSelectedAccount();
                if (acc == null) throw new Exception("Оберіть рахунок!");
                String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
                if (amountStr != null && !amountStr.isEmpty()) {
                    acc.deposit(Double.parseDouble(amountStr));
                    showCustomerDetails();
                    updateAccountsCombo();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                Account acc = getSelectedAccount();
                if (acc == null) throw new Exception("Оберіть рахунок!");
                String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
                if (amountStr != null && !amountStr.isEmpty()) {
                    acc.withdraw(Double.parseDouble(amountStr));
                    showCustomerDetails();
                    updateAccountsCombo();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setSize(600, 450);
        setLocationRelativeTo(null);
    }

    private void loadDataFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("test.dat"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    Bank.addCustomer(parts[0], parts[1]);
                    double balance = Double.parseDouble(parts[2]);
                    Bank.getCustomer(Bank.getNumberOfCustomers() - 1).addAccount(new CheckingAccount(balance));
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load test.dat. Using default data.");
            Bank.addCustomer("John", "Doe");
            Bank.addCustomer("Fox", "Mulder");
            try {
                Bank.getCustomer(0).addAccount(new CheckingAccount(2000));
                Bank.getCustomer(1).addAccount(new SavingsAccount(1000, 3));
            } catch (Exception ex) {}
        }
    }

    private void populateClients() {
        clientsCombo.removeAllItems();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer c = Bank.getCustomer(i);
            clientsCombo.addItem(c.getFirstName() + " " + c.getLastName());
        }
        if (clientsCombo.getItemCount() > 0) {
            clientsCombo.setSelectedIndex(0);
            updateAccountsCombo();
        }
    }

    private void updateAccountsCombo() {
        accountsCombo.removeAllItems();
        Customer cust = getSelectedCustomer();
        if (cust != null) {
            for (int i = 0; i < cust.getNumberOfAccounts(); i++) {
                Account acc = cust.getAccount(i);
                String type = acc instanceof CheckingAccount ? "Checking" : "Savings";
                accountsCombo.addItem(String.format("Acc %d (%s): $%.2f", i, type, acc.getBalance()));
            }
        }
    }

    private Customer getSelectedCustomer() {
        int index = clientsCombo.getSelectedIndex();
        return index >= 0 ? Bank.getCustomer(index) : null;
    }

    private Account getSelectedAccount() {
        Customer cust = getSelectedCustomer();
        if (cust == null) return null;
        int index = accountsCombo.getSelectedIndex();
        return index >= 0 ? cust.getAccount(index) : null;
    }

    private void showCustomerDetails() {
        Customer cust = getSelectedCustomer();
        if (cust == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append("Customer: ").append(cust.getFirstName()).append(" ").append(cust.getLastName()).append("\n\n");
        for (int i = 0; i < cust.getNumberOfAccounts(); i++) {
            Account acc = cust.getAccount(i);
            String type = acc instanceof CheckingAccount ? "Checking" : "Savings";
            sb.append(String.format("Account %d (%s): $%.2f\n", i, type, acc.getBalance()));
        }
        outputArea.setText(sb.toString());
    }

    private void generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CUSTOMER REPORT ===\n\n");
        sb.append(String.format("%-15s %-15s %-10s\n", "Last Name", "First Name", "Total Balance"));
        sb.append("--------------------------------------------------\n");
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer c = Bank.getCustomer(i);
            double total = 0;
            for (int j = 0; j < c.getNumberOfAccounts(); j++) {
                total += c.getAccount(j).getBalance();
            }
            sb.append(String.format("%-15s %-15s $%.2f\n", c.getLastName(), c.getFirstName(), total));
        }
        outputArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MatisseDemo().setVisible(true));
    }
}