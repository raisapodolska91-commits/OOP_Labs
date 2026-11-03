package com.mybank.tui;

import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.Account;
import jexer.TAction;
import jexer.TApplication;
import jexer.TField;
import jexer.TText;
import jexer.TWindow;
import jexer.event.TMenuEvent;
import jexer.menu.TMenu;

public class TUIdemo extends TApplication {

    private static final int ABOUT_APP = 2000;
    private static final int CUST_INFO = 2010;
    private static final int CUST_REPORT = 2020;

    private Bank bank = new Bank();

    public static void main(String[] args) throws Exception {
        TUIdemo tdemo = new TUIdemo();
        (new Thread(tdemo)).start();
    }

    public TUIdemo() throws Exception {
        super(BackendType.SWING);

        addToolMenu();
        TMenu fileMenu = addMenu("&File");
        fileMenu.addItem(CUST_INFO, "&Customer Info");
        fileMenu.addItem(CUST_REPORT, "&Customer Report"); 
        fileMenu.addDefaultItem(TMenu.MID_SHELL);
        fileMenu.addSeparator();
        fileMenu.addDefaultItem(TMenu.MID_EXIT);

        addWindowMenu();

        TMenu helpMenu = addMenu("&Help");
        helpMenu.addItem(ABOUT_APP, "&About...");

        setFocusFollowsMouse(true);
        ShowCustomerDetails();
    }

    @Override
    protected boolean onMenu(TMenuEvent menu) {
        if (menu.getId() == ABOUT_APP) {
            // Текст як на оригінальному скріншоті
            messageBox("About", "\t\t\t\t\t   Just a simple Jexer demo.\n\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich").show();
            return true;
        }
        if (menu.getId() == CUST_INFO) {
            ShowCustomerDetails();
            return true;
        }
        if (menu.getId() == CUST_REPORT) {
            ShowCustomerReport(); 
            return true;
        }
        return super.onMenu(menu);
    }

    // ================= ЗАВДАННЯ 1: ЗВІТ =================
    private void ShowCustomerReport() {
        TWindow reportWin = addWindow("Customer Report", 2, 1, 70, 15, TWindow.NOZOOMBOX);
        reportWin.newStatusBar("All customers report. Press Refresh to update.");

        final TText reportText = reportWin.addText("", 2, 2, 65, 10);

        reportWin.addButton("&Refresh", 2, 13, new TAction() {
            @Override
            public void DO() {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%-5s %-15s %-12s %-10s\n", "ID", "Name", "Account", "Balance"));
                sb.append("------------------------------------------------------\n");
                for (Customer c : bank.getCustomers()) {
                    for (Account acc : c.getAccounts()) {
                        sb.append(String.format("%-5d %-15s %-12s $%.2f\n",
                                c.getId(), c.getName(), acc.getAccountType(), acc.getBalance()));
                    }
                }
                reportText.setText(sb.toString());
            }
        });
    }

    // ================= ЗАВДАННЯ 2 та 3: ОПЕРАЦІЇ =================
    private void ShowCustomerDetails() {
        // Вікно, яке виглядає майже як на оригінальному скріншоті
        final TWindow custWin = addWindow("Customer Window", 2, 1, 50, 16, TWindow.NOZOOMBOX);
        custWin.newStatusBar("Enter customer number, press Show, then use Account Index for operations.");

        custWin.addLabel("Enter customer number: ", 2, 2);
        final TField custNo = custWin.addField(24, 2, 3, false);
        
        // Поле для вибору рахунку (Замінює проблемний ComboBox)
        custWin.addLabel("Account Index (0/1): ", 2, 4);
        final TField accountIndexField = custWin.addField(24, 4, 3, false);

        // Вікно з деталями (як на скріншоті)
        final TText details = custWin.addText("Owner Name: \nAccount Type: \nAccount Balance: ", 2, 6, 45, 4);

        // Кнопка Show
        custWin.addButton("&Show", 30, 2, new TAction() {
            @Override
            public void DO() {
                try {
                    int custId = Integer.parseInt(custNo.getText());
                    Customer cust = bank.getCustomer(custId);
                    if (cust == null) throw new Exception("Клієнта не знайдено");

                    // Виводимо дані першого рахунку для початку
                    Account acc = cust.getAccounts().get(0);
                    details.setText(String.format("Owner Name: %s\nAccount Type: '%s'\nAccount Balance: $%.2f", 
                            cust.getName(), acc.getAccountType(), acc.getBalance()));
                    
                    // Автоматично ставимо 0 в поле індексу
                    accountIndexField.setText("0");
                } catch (Exception e) {
                    messageBox("Error", "You must provide a valid customer number!").show();
                }
            }
        });

        // Кнопка Deposit
        custWin.addButton("&Deposit", 2, 12, new TAction() {
            @Override
            public void DO() {
                processTransaction(true, custNo, accountIndexField, details);
            }
        });

        // Кнопка Withdraw
        custWin.addButton("&Withdraw", 15, 12, new TAction() {
            @Override
            public void DO() {
                processTransaction(false, custNo, accountIndexField, details);
            }
        });
    }

    private void processTransaction(boolean isDeposit, TField custNo, TField accountIndexField, TText details) {
        try {
            int custId = Integer.parseInt(custNo.getText());
            Customer cust = bank.getCustomer(custId);
            if (cust == null) throw new Exception("Клієнта не знайдено");

            String indexStr = accountIndexField.getText().trim();
            if (indexStr.isEmpty()) throw new Exception("Введіть індекс рахунку (0 або 1)!");
            
            int accIndex = Integer.parseInt(indexStr);
            if (accIndex < 0 || accIndex >= cust.getAccounts().size()) {
                throw new Exception("Невірний індекс! Введіть 0 або 1.");
            }
            
            Account acc = cust.getAccounts().get(accIndex);
            
            String actionName = isDeposit ? "Deposit" : "Withdraw";
            String amountStr = inputBox(actionName, "Введіть суму для " + actionName + ":").getText();
            
            if (amountStr == null || amountStr.trim().isEmpty()) return; 
            
            double amount = Double.parseDouble(amountStr.trim());
            
            if (isDeposit) {
                acc.deposit(amount);
                messageBox("Success", "Успішно внесено: $" + amount).show();
            } else {
                acc.withdraw(amount); 
                messageBox("Success", "Успішно знято: $" + amount).show();
            }
            
            // Оновлюємо текст на екрані після транзакції
            details.setText(String.format("Owner Name: %s\nAccount Type: '%s'\nAccount Balance: $%.2f", 
                cust.getName(), acc.getAccountType(), acc.getBalance()));
            
        } catch (NumberFormatException e) {
            messageBox("Error", "Введіть коректне число.").show();
        } catch (Exception e) {
            messageBox("Error", e.getMessage()).show();
        }
    }
}