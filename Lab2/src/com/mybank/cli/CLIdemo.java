package com.mybank.tui;

import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.SavingsAccount;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import org.jline.reader.*;
import org.jline.reader.impl.completer.*;
import org.jline.utils.*;
import org.fusesource.jansi.AnsiConsole;

public class CLIdemo {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private String[] commandsList;

    public void init() {
        commandsList = new String[]{
            "help", "customers", "customer", "account", 
            "deposit", "withdraw", "report", "save", "exit"
        };
    }

    public void run() {
        AnsiConsole.systemInstall(); // Вмикаємо кольори
        printWelcomeMessage();

        LineReaderBuilder readerBuilder = LineReaderBuilder.builder();
        List<Completer> completors = new LinkedList<>();
        completors.add(new StringsCompleter(commandsList));
        readerBuilder.completer(new ArgumentCompleter(completors));
        LineReader reader = readerBuilder.build();

        String line;
        while ((line = readLine(reader, "")) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 0 || parts[0].isEmpty()) continue;
            
            String command = parts[0].toLowerCase();

            try {
                if ("help".equals(command)) {
                    printHelp();
                } else if ("customers".equals(command)) {
                    printCustomers();
                } else if ("customer".equals(command)) {
                    if (parts.length > 1) printCustomerDetails(Integer.parseInt(parts[1]));
                    else System.out.println(ANSI_RED + "Usage: customer <index>" + ANSI_RESET);
                } else if ("account".equals(command)) {
                    if (parts.length > 2) showAccount(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    else System.out.println(ANSI_RED + "Usage: account <customer_id> <account_index>" + ANSI_RESET);
                } else if ("deposit".equals(command)) {
                    if (parts.length > 3) deposit(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                    else System.out.println(ANSI_RED + "Usage: deposit <customer_id> <account_index> <amount>" + ANSI_RESET);
                } else if ("withdraw".equals(command)) {
                    if (parts.length > 3) withdraw(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                    else System.out.println(ANSI_RED + "Usage: withdraw <customer_id> <account_index> <amount>" + ANSI_RESET);
                } else if ("report".equals(command)) {
                    printReport();
                } else if ("save".equals(command)) {
                    saveDataToFile();
                } else if ("exit".equals(command)) {
                    System.out.println(ANSI_CYAN + "Exiting application..." + ANSI_RESET);
                    AnsiConsole.systemUninstall();
                    return;
                } else {
                    System.out.println(ANSI_RED + "Invalid command, For assistance press TAB or type \"help\" then hit ENTER." + ANSI_RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(ANSI_RED + "Error: Please enter valid numbers!" + ANSI_RESET);
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET);
            }
        }
        AnsiConsole.systemUninstall();
    }

    private void printWelcomeMessage() {
        System.out.println("\nWelcome to " + ANSI_GREEN + "MyBank Console Client App" + ANSI_RESET + "!");
        System.out.println("For assistance press TAB or type \"help\" then hit ENTER.");
    }

    private void printHelp() {
        System.out.println(ANSI_CYAN + "Available commands:" + ANSI_RESET);
        System.out.println("help\t\t\t- Show help");
        System.out.println("customers\t\t- Show list of customers");
        System.out.println("customer <index>\t- Show customer details");
        System.out.println("account <id> <acc_idx>\t- Show specific account balance");
        System.out.println("deposit <id> <acc> <sum>- Deposit money");
        System.out.println("withdraw <id> <acc> <sum>- Withdraw money");
        System.out.println("report\t\t\t- Show full customers report (Grade 5)");
        System.out.println("save\t\t\t- Save data to test.dat");
        System.out.println("exit\t\t\t- Exit the app");
    }

    private void printCustomers() {
        System.out.println("\nThis is all of your " + ANSI_RED + "customers" + ANSI_RESET + ":");
        if (Bank.getNumberOfCustomers() > 0) {
            System.out.printf("%-5s %-15s %-15s %-10s\n", "ID", "Last Name", "First Name", "Balance");
            System.out.println("-------------------------------------------------------");
            for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
                Customer c = Bank.getCustomer(i);
                System.out.printf("%-5d %-15s %-15s $%.2f\n", i, c.getLastName(), c.getFirstName(), c.getAccount(0).getBalance());
            }
        } else {
            System.out.println(ANSI_RED + "Your bank has no customers!" + ANSI_RESET);
        }
    }

    private void printCustomerDetails(int custNo) {
        if (custNo < 0 || custNo >= Bank.getNumberOfCustomers()) {
            System.out.println(ANSI_RED + "ERROR! Wrong customer number!" + ANSI_RESET);
            return;
        }
        Customer cust = Bank.getCustomer(custNo);
        System.out.println("\nDetailed info about customer #" + ANSI_RED + custNo + ANSI_RESET + ":");
        System.out.printf("%-15s %-15s %-15s %-10s\n", "Last Name", "First Name", "Account Type", "Balance");
        System.out.println("-------------------------------------------------------");
        for (int i = 0; i < cust.getNumberOfAccounts(); i++) {
            String type = cust.getAccount(i) instanceof CheckingAccount ? "Checking" : "Savings";
            System.out.printf("%-15s %-15s %-15s $%.2f\n", cust.getLastName(), cust.getFirstName(), type, cust.getAccount(i).getBalance());
        }
    }

    private void showAccount(int custId, int accIndex) {
        Customer cust = Bank.getCustomer(custId);
        double balance = cust.getAccount(accIndex).getBalance();
        System.out.println(ANSI_GREEN + "Balance on account " + accIndex + " for customer " + custId + ": $" + balance + ANSI_RESET);
    }

    private void deposit(int custId, int accIndex, double amount) throws Exception {
        Customer cust = Bank.getCustomer(custId);
        cust.getAccount(accIndex).deposit(amount);
        System.out.println(ANSI_GREEN + "Deposited $" + amount + " successfully." + ANSI_RESET);
    }

    private void withdraw(int custId, int accIndex, double amount) throws Exception {
        Customer cust = Bank.getCustomer(custId);
        cust.getAccount(accIndex).withdraw(amount);
        System.out.println(ANSI_GREEN + "Withdrew $" + amount + " successfully." + ANSI_RESET);
    }

    // ================= ЗВІТ (Завдання на "5") =================
    private void printReport() {
        System.out.println("\n" + ANSI_BLUE + "================ CUSTOMER REPORT ================" + ANSI_RESET);
        System.out.printf("%-5s %-15s %-15s %-10s\n", "ID", "Last Name", "First Name", "Total Balance");
        System.out.println("-------------------------------------------------");
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer c = Bank.getCustomer(i);
            double total = 0;
            for (int j = 0; j < c.getNumberOfAccounts(); j++) {
                total += c.getAccount(j).getBalance();
            }
            System.out.printf("%-5d %-15s %-15s $%.2f\n", i, c.getLastName(), c.getFirstName(), total);
        }
        System.out.println("=================================================\n");
    }

    // ================= ЗБЕРЕЖЕННЯ =================
    private void saveDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("test.dat"))) {
            writer.println(Bank.getNumberOfCustomers());
            for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
                Customer c = Bank.getCustomer(i);
                writer.println(c.getFirstName() + ";" + c.getLastName() + ";" + c.getAccount(0).getBalance());
            }
            System.out.println(ANSI_GREEN + "Data saved to test.dat successfully." + ANSI_RESET);
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Error saving file: " + e.getMessage() + ANSI_RESET);
        }
    }

    // ================= ЗЧИТУВАННЯ =================
    private void loadDataFromFile() {
        File file = new File("test.dat");
        if (!file.exists()) {
            System.out.println(ANSI_YELLOW + "File test.dat not found. Creating default test data..." + ANSI_RESET);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null) {
                int count = Integer.parseInt(line.trim());
                for (int i = 0; i < count; i++) {
                    String[] parts = br.readLine().split(";");
                    Bank.addCustomer(parts[0], parts[1]);
                    Bank.getCustomer(i).addAccount(new CheckingAccount(Double.parseDouble(parts[2])));
                }
            }
            System.out.println(ANSI_GREEN + "Data loaded from test.dat." + ANSI_RESET);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error loading file: " + e.getMessage() + ANSI_RESET);
        }
    }

    private String readLine(LineReader reader, String promptMessage) {
        try {
            return reader.readLine(promptMessage + ANSI_YELLOW + "\nbank> " + ANSI_RESET).trim();
        } catch (UserInterruptException | EndOfFileException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        CLIdemo shell = new CLIdemo();
        shell.init();
        
        shell.loadDataFromFile();

        // Якщо файлу не було і банк порожній, створюємо тестових клієнтів
        if (Bank.getNumberOfCustomers() == 0) {
            Bank.addCustomer("John", "Doe");
            Bank.addCustomer("Fox", "Mulder");
            Bank.getCustomer(0).addAccount(new CheckingAccount(2000));
            Bank.getCustomer(0).addAccount(new SavingsAccount(1000, 3));
            Bank.getCustomer(1).addAccount(new CheckingAccount(500));
        }

        shell.run();
    }
}