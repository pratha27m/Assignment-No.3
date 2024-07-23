/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pratham.expensetracker;

/**
 *
 * @author prath
 */


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class Expense {
    private Date date;
    private String category;
    private double amount;

    public Expense(Date date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}

class FileHandler {
    private static final String FILE_PATH = "expenses.txt";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public void saveExpenses(String username, List<Expense> expenses) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Expense expense : expenses) {
                writer.write(username + "," + sdf.format(expense.getDate()) + "," + expense.getCategory() + "," + expense.getAmount());
                writer.newLine();
            }
        }
    }

    public List<Expense> loadExpenses(String username) throws IOException {
        List<Expense> expenses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    try {
                        Date date = sdf.parse(parts[1]);
                        String category = parts[2];
                        double amount = Double.parseDouble(parts[3]);
                        expenses.add(new Expense(date, category, amount));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return expenses;
    }
}

public class ExpenseTracker {
    private Map<String, User> users;
    private Map<String, List<Expense>> userExpenses;
    private FileHandler fileHandler;

    public ExpenseTracker() {
        users = new HashMap<>();
        userExpenses = new HashMap<>();
        fileHandler = new FileHandler();
    }

    public void registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            User user = new User(username, password);
            users.put(username, user);
            userExpenses.put(username, new ArrayList<>());
        }
    }

    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public void addExpense(String username, Expense expense) {
        List<Expense> expenses = userExpenses.get(username);
        if (expenses != null) {
            expenses.add(expense);
        }
    }

    public List<Expense> getExpenses(String username) {
        return userExpenses.get(username);
    }

    public static void main(String[] args) {
        new ExpenseTrackerGUI();
    }
}

class ExpenseTrackerGUI {
    private ExpenseTracker tracker;
    private FileHandler fileHandler;

    public ExpenseTrackerGUI() {
        tracker = new ExpenseTracker();
        fileHandler = new FileHandler();

        JFrame frame = new JFrame("Expense Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(180, 80, 80, 25);
        panel.add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                if (tracker.loginUser(username, password)) {
                    JOptionPane.showMessageDialog(panel, "Login successful");
                    showExpensePanel(panel, username);
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid credentials");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                tracker.registerUser(username, password);
                JOptionPane.showMessageDialog(panel, "User registered successfully");
            }
        });
    }

    private void showExpensePanel(JPanel panel, String username) {
        panel.removeAll();
        panel.repaint();

        JLabel dateLabel = new JLabel("Date (dd/MM/yyyy)");
        dateLabel.setBounds(10, 20, 120, 25);
        panel.add(dateLabel);

        JTextField dateText = new JTextField(20);
        dateText.setBounds(150, 20, 165, 25);
        panel.add(dateText);

        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setBounds(10, 50, 80, 25);
        panel.add(categoryLabel);

        JTextField categoryText = new JTextField(20);
        categoryText.setBounds(150, 50, 165, 25);
        panel.add(categoryText);

        JLabel amountLabel = new JLabel("Amount");
        amountLabel.setBounds(10, 80, 80, 25);
        panel.add(amountLabel);

        JTextField amountText = new JTextField(20);
        amountText.setBounds(150, 80, 165, 25);
        panel.add(amountText);

        JButton addButton = new JButton("Add Expense");
        addButton.setBounds(10, 110, 150, 25);
        panel.add(addButton);

        JButton viewButton = new JButton("View Expenses");
        viewButton.setBounds(10, 140, 150, 25);
        panel.add(viewButton);

        JButton saveButton = new JButton("Save and Logout");
        saveButton.setBounds(10, 170, 150, 25);
        panel.add(saveButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String dateStr = dateText.getText();
                    Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
                    String category = categoryText.getText();
                    double amount = Double.parseDouble(amountText.getText());
                    tracker.addExpense(username, new Expense(date, category, amount));
                    JOptionPane.showMessageDialog(panel, "Expense added successfully");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error adding expense: " + ex.getMessage());
                }
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder expensesList = new StringBuilder();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (Expense expense : tracker.getExpenses(username)) {
                    expensesList.append(sdf.format(expense.getDate()))
                                .append(" - ")
                                .append(expense.getCategory())
                                .append(": ")
                                .append(expense.getAmount())
                                .append("\n");
                }
                JOptionPane.showMessageDialog(panel, expensesList.toString());
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileHandler.saveExpenses(username, tracker.getExpenses(username));
                    JOptionPane.showMessageDialog(panel, "Data saved successfully. Logging out...");
                    System.exit(0);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error saving data: " + ex.getMessage());
                }
            }
        });

        panel.revalidate();
        panel.repaint();
    }
}

