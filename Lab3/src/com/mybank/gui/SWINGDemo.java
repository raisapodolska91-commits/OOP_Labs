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

public class SWINGDemo {

    private final JEditorPane log;
    private final JButton show;
    private final JButton reportBtn;
    private final JButton depositBtn;
    private final JButton withdrawBtn;
    private final JComboBox<String> clients;
    private final JComboBox<String> accountsCombo;
    private JFrame frame;

    public SWINGDemo() {
        frame = new JFrame("Banking GUI Demo");
        frame.setLayout(new BorderLayout(10, 10));

        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        controlPanel.add(new JLabel("Select Customer:"));
        clients = new JComboBox<>();
        
        // --- ДОДАНО: Заповнюємо список клієнтами з банку ---
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer c = Bank.getCustomer(i);
            clients.addItem(c.getFirstName() + " " + c.getLastName());
        }
        // ---------------------------------------------------
        
        controlPanel.add(clients);

        controlPanel.add(new JLabel("Select Account:"));
        accountsCombo = new JComboBox<>();
        controlPanel.add(accountsCombo);

        show = new JButton("Show");
        reportBtn = new JButton("Report (Grade 5)");
        controlPanel.add(show);
        controlPanel.add(reportBtn);

        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");
        controlPanel.add(depositBtn);
        controlPanel.add(withdrawBtn);

        log = new JEditorPane("text/html", "");
        log.setEditable(false);
        log.setPreferredSize(new Dimension(400, 250));
        log.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(log), BorderLayout.CENTER);

        // Обробники подій
        show.addActionListener(e -> showCustomerDetails());
        reportBtn.addActionListener(e -> new CustomerReportWindow(Bank.getCustomers()));
        
        depositBtn.addActionListener(e -> {
            try {
                Customer cust = getSelectedCustomer();
                Account acc = getSelectedAccount(cust);
                String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
                if (amountStr != null && !amountStr.isEmpty()) {
                    acc.deposit(Double.parseDouble(amountStr));
                    showCustomerDetails();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        withdrawBtn.addActionListener(e -> {
            try {
                Customer cust = getSelectedCustomer();
                Account acc = getSelectedAccount(cust);
                String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
                if (amountStr != null && !amountStr.isEmpty()) {
                    acc.withdraw(Double.parseDouble(amountStr));
                    showCustomerDetails();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        clients.addActionListener(e -> updateAccountsCombo());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // --- ДОДАНО: Ініціалізуємо рахунки для першого клієнта в списку ---
        if (clients.getItemCount() > 0) {
            clients.setSelectedIndex(0);
            updateAccountsCombo();
        }
        // ------------------------------------------------------------------
    }

    private void updateAccountsCombo() {
        accountsCombo.removeAllItems();
        Customer cust = getSelectedCustomer();
        if (cust != null) {
            for (int i = 0; i < cust.getNumberOfAccounts(); i++) {
                accountsCombo.addItem("Account " + i + " (" + cust.getAccount(i).getClass().getSimpleName() + ")");
            }
        }
    }

    private Customer getSelectedCustomer() {
        int index = clients.getSelectedIndex();
        return index >= 0 ? Bank.getCustomer(index) : null;
    }

    private Account getSelectedAccount(Customer cust) {
        int index = accountsCombo.getSelectedIndex();
        return (cust != null && index >= 0) ? cust.getAccount(index) : null;
    }

    private void showCustomerDetails() {
        Customer cust = getSelectedCustomer();
        if (cust == null) return;
        
        StringBuilder sb = new StringBuilder("<html><body style='font-family: Arial;'>");
        sb.append("<h3>Customer: ").append(cust.getFirstName()).append(" ").append(cust.getLastName()).append("</h3>");
        sb.append("<b>Accounts:</b><br>");
        
        for (int i = 0; i < cust.getNumberOfAccounts(); i++) {
            Account acc = cust.getAccount(i);
            String type = acc instanceof CheckingAccount ? "Checking" : "Savings";
            sb.append(String.format("• Account %d: %s, Balance: $%.2f<br>", i, type, acc.getBalance()));
        }
        sb.append("</body></html>");
        log.setText(sb.toString());
    }

    private static void loadDataFromFile() {
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
            System.out.println("Data loaded successfully from test.dat");
        } catch (Exception e) {
            System.out.println("Could not load test.dat. Using default data.");
        }
    }

    public static void main(String[] args) {
        loadDataFromFile();

        if (Bank.getNumberOfCustomers() == 0) {
            Bank.addCustomer("John", "Doe");
            Bank.addCustomer("Fox", "Mulder");
            Bank.addCustomer("Dana", "Scully");
            try {
                Bank.getCustomer(0).addAccount(new CheckingAccount(2000));
                Bank.getCustomer(1).addAccount(new SavingsAccount(1000, 3));
                Bank.getCustomer(2).addAccount(new CheckingAccount(1000));
            } catch (Exception e) {}
        }

        SwingUtilities.invokeLater(SWINGDemo::new);
    }
}