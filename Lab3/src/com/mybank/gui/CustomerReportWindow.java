package com.mybank.gui;

import com.mybank.domain.Customer;
import com.mybank.domain.Account;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerReportWindow {

    public CustomerReportWindow(List<Customer> customers) {
        JFrame reportFrame = new JFrame("Customer Report");
        reportFrame.setLayout(new BorderLayout());

        StringBuilder sb = new StringBuilder("<html><body style='font-family: Arial;'>");
        sb.append("<h2 style='text-align:center;'>=== CUSTOMER REPORT ===</h2>");
        sb.append("<table border='1' cellpadding='5' cellspacing='0' width='100%'>");
        sb.append("<tr style='background-color:#f0f0f0;'>");
        sb.append("<th>ID</th><th>Last Name</th><th>First Name</th><th>Account Type</th><th>Balance</th>");
        sb.append("</tr>");

        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            for (int j = 0; j < c.getNumberOfAccounts(); j++) {
                Account acc = c.getAccount(j);
                String type = acc.getClass().getSimpleName();
                sb.append("<tr>");
                sb.append("<td>").append(i).append("</td>");
                sb.append("<td>").append(c.getLastName()).append("</td>");
                sb.append("<td>").append(c.getFirstName()).append("</td>");
                sb.append("<td>").append(type).append("</td>");
                sb.append(String.format("<td>$%.2f</td>", acc.getBalance()));
                sb.append("</tr>");
            }
        }
        sb.append("</table></body></html>");

        JEditorPane reportPane = new JEditorPane("text/html", sb.toString());
        reportPane.setEditable(false);
        reportPane.setPreferredSize(new Dimension(600, 300));

        reportFrame.add(new JScrollPane(reportPane), BorderLayout.CENTER);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> reportFrame.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeBtn);
        reportFrame.add(buttonPanel, BorderLayout.SOUTH);

        reportFrame.pack();
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setVisible(true);
    }
}