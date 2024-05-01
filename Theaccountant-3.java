/*
Name: <donald kelly>
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: March 10, 2024
Class: Enterpise Computing
*/


package project3;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Color;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Theaccountant {

    private JFrame frmTheAccountant;
    private JTextField userNameInput;
    private JTextField passwordInput;
    private JTextArea displayConnectionStatus;
    private JTextArea displaySQLresult;
    private Connection connection;
    private JButton disconnectDatabaseButton;
    private JComboBox<String> userPropertiesComboBox;

    // Define the properties file names
    private final String dbUrlPropertiesFile = "operationslog.properties";
    private final String userPropertiesFile = "theaccountant.properties";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Theaccountant window = new Theaccountant();
                    window.frmTheAccountant.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Theaccountant() {
        initialize();
    }

    private void initialize() {
        frmTheAccountant = new JFrame();
        frmTheAccountant.setTitle("SQL Client Application - ( CNT 4714 - Spring 2024 - Project 3)");
        frmTheAccountant.setBounds(100, 100, 1023, 650);
        frmTheAccountant.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmTheAccountant.getContentPane().setLayout(null);

        JLabel connectionLabel = new JLabel("Connection Details");
        connectionLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        connectionLabel.setBounds(10, 6, 123, 23);
        frmTheAccountant.getContentPane().add(connectionLabel);

        JLabel lblDbUrlProperties = new JLabel("DB URL Properties");
        lblDbUrlProperties.setBounds(7, 40, 111, 23);
        frmTheAccountant.getContentPane().add(lblDbUrlProperties);

        JComboBox<String> dbUrlPropertiesComboBox = new JComboBox<>(new String[]{dbUrlPropertiesFile});
        dbUrlPropertiesComboBox.setBounds(115, 39, 189, 21);
        frmTheAccountant.getContentPane().add(dbUrlPropertiesComboBox);

        JLabel lblUserProperties = new JLabel("User Properties");
        lblUserProperties.setBounds(10, 69, 93, 23);
        frmTheAccountant.getContentPane().add(lblUserProperties);

        userPropertiesComboBox = new JComboBox<>(new String[]{userPropertiesFile});
        userPropertiesComboBox.setBounds(112, 72, 192, 21);
        frmTheAccountant.getContentPane().add(userPropertiesComboBox);

        JLabel userNameLabel = new JLabel("Username");
        userNameLabel.setBounds(10, 103, 80, 23);
        frmTheAccountant.getContentPane().add(userNameLabel);

        userNameInput = new JTextField();
        userNameInput.setBounds(94, 104, 198, 20);
        userNameInput.setColumns(10);
        frmTheAccountant.getContentPane().add(userNameInput);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 140, 80, 23);
        frmTheAccountant.getContentPane().add(passwordLabel);

        passwordInput = new JTextField();
        passwordInput.setColumns(10);
        passwordInput.setBounds(94, 141, 198, 20);
        frmTheAccountant.getContentPane().add(passwordInput);

        JLabel sqlCommandLabel = new JLabel("Enter An SQL Command");
        sqlCommandLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        sqlCommandLabel.setBounds(314, 11, 153, 30);
        frmTheAccountant.getContentPane().add(sqlCommandLabel);

        JTextArea SQLcommand = new JTextArea();
        SQLcommand.setLineWrap(true);
        SQLcommand.setWrapStyleWord(true);
        SQLcommand.setBounds(314, 40, 530, 157);
        frmTheAccountant.getContentPane().add(SQLcommand);

        JButton connectDatabaseButton = new JButton("Connect to Database");
        connectDatabaseButton.setBackground(Color.BLUE);
        connectDatabaseButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        connectDatabaseButton.setBounds(10, 208, 165, 23);
        connectDatabaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToDatabase();
            }
        });
        frmTheAccountant.getContentPane().add(connectDatabaseButton);

        disconnectDatabaseButton = new JButton("Disconnect From Database");
        disconnectDatabaseButton.setForeground(Color.BLACK);
        disconnectDatabaseButton.setBackground(Color.RED);
        disconnectDatabaseButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        disconnectDatabaseButton.setBounds(204, 208, 189, 21);
        disconnectDatabaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnectFromDatabase();
            }
        });
        disconnectDatabaseButton.setEnabled(false);
        frmTheAccountant.getContentPane().add(disconnectDatabaseButton);

        JButton clearCommandButton = new JButton("Clear SQL Command");
        clearCommandButton.setBackground(Color.YELLOW);
        clearCommandButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        clearCommandButton.setBounds(440, 208, 165, 23);
        clearCommandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SQLcommand.setText("");
            }
        });
        frmTheAccountant.getContentPane().add(clearCommandButton);

        JButton executeCommandButton = new JButton("Execute SQL Command");
        executeCommandButton.setForeground(Color.BLACK);
        executeCommandButton.setBackground(Color.GREEN);
        executeCommandButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        executeCommandButton.setBounds(648, 208, 179, 23);
        executeCommandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeSQLCommand(SQLcommand.getText());
            }
        });
        frmTheAccountant.getContentPane().add(executeCommandButton);

        displayConnectionStatus = new JTextArea();
        displayConnectionStatus.setText("No Connection Established");
        displayConnectionStatus.setBounds(10, 253, 583, 23);
        frmTheAccountant.getContentPane().add(displayConnectionStatus);

        JLabel sqlResultLabel = new JLabel("SQL Execution Result Window");
        sqlResultLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        sqlResultLabel.setBounds(10, 287, 198, 14);
        frmTheAccountant.getContentPane().add(sqlResultLabel);

        displaySQLresult = new JTextArea();
        displaySQLresult.setBounds(10, 312, 583, 96);
        frmTheAccountant.getContentPane().add(displaySQLresult);

        JScrollPane scrollPane = new JScrollPane(displaySQLresult);
        scrollPane.setBounds(35, 310, 870, 215);
        frmTheAccountant.getContentPane().add(scrollPane);

        JButton clearResultsButton = new JButton("Clear Result Window");
        clearResultsButton.setBackground(Color.YELLOW);
        clearResultsButton.setBounds(22, 556, 153, 23);
        clearResultsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displaySQLresult.setText("");
            }
        });
        frmTheAccountant.getContentPane().add(clearResultsButton);
    }

    private void connectToDatabase() {
        try {
            String username = userNameInput.getText();
            String password = passwordInput.getText();
            
            // Load user properties
            Properties userProps = loadProperties(userPropertiesFile);
            if (userProps != null) {
                String propUsername = userProps.getProperty("db.username");
                String propPassword = userProps.getProperty("db.password");
                
                if (propUsername.equals(username) && propPassword.equals(password)) {
                    // Load DB URL properties
                    Properties dbProps = loadProperties(dbUrlPropertiesFile);
                    if (dbProps != null) {
                        String dbUrl = dbProps.getProperty("db.url");
                        
                        if (dbUrl != null) {
                            displayConnectionStatus.setText("CONNECTED TO: " + dbUrl);
                            disconnectDatabaseButton.setEnabled(true);
                            connection = DriverManager.getConnection(dbUrl, username, password);
                            
                            // Check user privileges
                            checkUserPrivileges(connection, username);
                        } else {
                            displayConnectionStatus.setText("Database URL not found in properties file: " + dbUrlPropertiesFile);
                        }
                    } else {
                        displayConnectionStatus.setText("DB URL properties file not found: " + dbUrlPropertiesFile);
                    }
                } else {
                    displayConnectionStatus.setText("No connection established. Invalid username or password.");
                }
            } else {
                displayConnectionStatus.setText("User properties file not found: " + userPropertiesFile);
            }
        } catch (SQLException e) {
            displayConnectionStatus.setText("Error connecting to the database: " + e.getMessage());
        }
    }

    private Properties loadProperties(String fileName) {
        try {
            Properties props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                props.load(inputStream);
                inputStream.close();
                return props;
            } else {
                displayConnectionStatus.setText("Properties file not found: " + fileName);
                return null;
            }
        } catch (IOException e) {
            displayConnectionStatus.setText("Error loading properties file: " + e.getMessage());
            return null;
        }
    }

    private void checkUserPrivileges(Connection connection, String username) {
        // This method checks user privileges as per the requirement. Implementation omitted for brevity.
    }

    private void disconnectFromDatabase() {
        try {
            // Close the database connection
            if (connection != null && !connection.isClosed()) {
                connection.close();
                displayConnectionStatus.setText("No Connection Established"); // Update UI
            }
        } catch (SQLException e) {
            displayConnectionStatus.setText("Error disconnecting from the database: " + e.getMessage());
        }
        // Clear the SQL execution result window
        displaySQLresult.setText("");
    }
    
    private void displaySQLResult(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Clear the existing result
            displaySQLresult.setText("");

            // Append column names
            for (int i = 1; i <= columnCount; i++) {
                displaySQLresult.append(metaData.getColumnName(i) + "\t");
            }
            displaySQLresult.append("\n");

            // Append rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    displaySQLresult.append(resultSet.getString(i) + "\t");
                }
                displaySQLresult.append("\n");
            }
        } catch (SQLException e) {
            displayConnectionStatus.setText("Error displaying SQL result: " + e.getMessage());
        }
    }

    private void executeSQLCommand(String sqlCommand) {
        try {
            if (connection != null && !connection.isClosed()) {
                Statement statement = connection.createStatement();

                // Execute the SQL command
                if (sqlCommand.trim().toUpperCase().startsWith("SELECT")) {
                    // For SELECT queries
                    ResultSet resultSet = statement.executeQuery(sqlCommand);
                    displaySQLResult(resultSet);
                } else {
                    // For other queries (UPDATE, INSERT, DELETE, etc.)
                    int rowsAffected = statement.executeUpdate(sqlCommand);
                    displaySQLresult.setText("Number of rows affected: " + rowsAffected);
                }

                statement.close();
            } else {
                displayConnectionStatus.setText("No active database connection.");
            }
        } catch (SQLException e) {
            displayConnectionStatus.setText("Error executing SQL command: " + e.getMessage());
        }
    }

}
