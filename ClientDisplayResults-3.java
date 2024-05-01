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
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JScrollPane;

public class ClientDisplayResults {

    private JFrame frmSqlClientApplication;  
    private JTextField userNameInput;
    private JTextField passwordInput;
    private JTextArea displayConnectionStatus;
    private JTextArea displaySQLresult;
    private Connection connection;
    private JButton disconnectDatabaseButton;
    private JComboBox<String> userPropertiesComboBox;
    // Define the properties file names
    String[] propertiesFiles = {"project3.properties", "bikedb.properties"};
    String[] userPropertiesFiles = {"root.properties", "client1.properties", "client2.properties"};

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientDisplayResults window = new ClientDisplayResults();
                    window.frmSqlClientApplication.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public ClientDisplayResults() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmSqlClientApplication = new JFrame();
        frmSqlClientApplication.setTitle("SQL Client Application - ( CNT 4714 - Spring 2024 - Project 3)");
        frmSqlClientApplication.setBounds(100, 100, 1023, 650);
        frmSqlClientApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmSqlClientApplication.getContentPane().setLayout(null);

        JLabel connectionLabel = new JLabel("Connection Details");
        connectionLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        connectionLabel.setBounds(10, 6, 123, 23);
        frmSqlClientApplication.getContentPane().add(connectionLabel);

        //DB props label
        JLabel lblDbUrlProperties = new JLabel("DB URL Properties");
        lblDbUrlProperties.setBounds(7, 40, 111, 23);
        frmSqlClientApplication.getContentPane().add(lblDbUrlProperties);
        
        //DB props drop down selector
        JComboBox<String> dbUrlPropertiesComboBox = new JComboBox<>(propertiesFiles);
        dbUrlPropertiesComboBox.setBounds(115, 39, 189, 21);
        frmSqlClientApplication.getContentPane().add(dbUrlPropertiesComboBox);

        
        //User props
        JLabel lblUserProperties = new JLabel("User Properties");
        lblUserProperties.setBounds(10, 69, 93, 23);
        frmSqlClientApplication.getContentPane().add(lblUserProperties);
        
        //User props drop down selector
        userPropertiesComboBox = new JComboBox<>(userPropertiesFiles);
        userPropertiesComboBox.setBounds(112, 72, 192, 21);
        frmSqlClientApplication.getContentPane().add(userPropertiesComboBox);
        
        
        
        
        JLabel userNameLabel = new JLabel("Username");
        userNameLabel.setBounds(10, 103, 80, 23);
        frmSqlClientApplication.getContentPane().add(userNameLabel);

        userNameInput = new JTextField();
        userNameInput.setBounds(94, 104, 198, 20);
        userNameInput.setColumns(10);
        frmSqlClientApplication.getContentPane().add(userNameInput);
        
        
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 140, 80, 23);
        frmSqlClientApplication.getContentPane().add(passwordLabel);

        passwordInput = new JTextField();
        passwordInput.setColumns(10);
        passwordInput.setBounds(94, 141, 198, 20);
        frmSqlClientApplication.getContentPane().add(passwordInput);

        
        
        
        JLabel sqlCommandLabel = new JLabel("Enter An SQL Command");
        sqlCommandLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        sqlCommandLabel.setBounds(314, 11, 153, 30);
        frmSqlClientApplication.getContentPane().add(sqlCommandLabel);

        
        JTextArea SQLcommand = new JTextArea();
        SQLcommand.setLineWrap(true); // Enable line wrapping
        SQLcommand.setWrapStyleWord(true); // Wrap at word boundaries
        SQLcommand.setBounds(314, 40, 530, 157);
        frmSqlClientApplication.getContentPane().add(SQLcommand);
       


        
        
        
        
        JButton connectDatabaseButton = new JButton("Connect to Database");
        connectDatabaseButton.setBackground(Color.BLUE);
        connectDatabaseButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        connectDatabaseButton.setBounds(10, 208, 165, 23);
        connectDatabaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectToDatabase(dbUrlPropertiesComboBox.getSelectedItem().toString());
            }
        });
        frmSqlClientApplication.getContentPane().add(connectDatabaseButton);

        
        
        
        //Disconnect button
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
        frmSqlClientApplication.getContentPane().add(disconnectDatabaseButton);


        
        
        //Clear SQL command Button
        JButton clearCommandButton = new JButton("Clear SQL Command");
        clearCommandButton.setBackground(Color.YELLOW);
        clearCommandButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        clearCommandButton.setBounds(440, 208, 165, 23);
        clearCommandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SQLcommand.setText("");
            }
        });
        
        frmSqlClientApplication.getContentPane().add(clearCommandButton);

        
        
        
        JButton executeCommandButton = new JButton("Execute SQL Command");
        executeCommandButton.setForeground(Color.BLACK);
        executeCommandButton.setBackground(Color.GREEN);
        executeCommandButton.setFont(new Font("Tahoma", Font.BOLD, 9));
        executeCommandButton.setBounds(648, 208, 179, 23);
        executeCommandButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedPropertyFile = dbUrlPropertiesComboBox.getSelectedItem().toString();
                executeSQLCommand(SQLcommand.getText(), selectedPropertyFile);
            }
        });
        frmSqlClientApplication.getContentPane().add(executeCommandButton); 



        
        
        
        
        displayConnectionStatus = new JTextArea();
        displayConnectionStatus.setText("No Connection Established");
        displayConnectionStatus.setBounds(10, 253, 583, 23);
        frmSqlClientApplication.getContentPane().add(displayConnectionStatus);

        
        
      //SQL Result Label
        JLabel sqlResultLabel = new JLabel("SQL Execution Result Window");
        sqlResultLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        sqlResultLabel.setBounds(10, 287, 198, 14);
        frmSqlClientApplication.getContentPane().add(sqlResultLabel);
       
        
        
        
        //SQL Result text area
        displaySQLresult = new JTextArea();
        displaySQLresult.setBounds(10, 312, 583, 96);
        frmSqlClientApplication.getContentPane().add(displaySQLresult);

        
        //SQL Result Scroll wheel
        JScrollPane scrollPane = new JScrollPane(displaySQLresult);
        scrollPane.setBounds(35, 310, 870, 215);
        frmSqlClientApplication.getContentPane().add(scrollPane);
        
        
        
        
        
        
        //SQL Result clear button
        JButton clearResultsButton = new JButton("Clear Result Window");
        clearResultsButton.setBackground(Color.YELLOW);
        clearResultsButton.setBounds(22, 556, 153, 23);
        clearResultsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displaySQLresult.setText("");
            }
        });
        frmSqlClientApplication.getContentPane().add(clearResultsButton);
        
    }
        
        
        
        
 //-------------------------------------------------------------------------------------------------------------------------------------------------------    
    

    /*
     Connects to the database using the user selected properties file.
     Updates the text area with the database URL if a valid properties file is selected.
       param: selectedPropertyFile The name of the selected properties file.
     */
    private void connectToDatabase(String selectedPropertyFile) {
        try {
            String username = userNameInput.getText();
            String password = passwordInput.getText();
            
            // Load user properties dynamically based on selection
            Properties userProps = new Properties();
            InputStream userInputStream = getClass().getClassLoader().getResourceAsStream(userPropertiesComboBox.getSelectedItem().toString());
            if (userInputStream != null) {
                userProps.load(userInputStream);
                String propUsername = userProps.getProperty("db.username");
                String propPassword = userProps.getProperty("db.password");
                userInputStream.close();
                
                if (propUsername.equals(username) && propPassword.equals(password)) {
                    // Load DB URL properties
                    Properties dbProps = new Properties();
                    InputStream dbInputStream = getClass().getClassLoader().getResourceAsStream(selectedPropertyFile);
                    if (dbInputStream != null) {
                        dbProps.load(dbInputStream);
                        String dbUrl = dbProps.getProperty("db.url");
                        dbInputStream.close();
                        
                        if (dbUrl != null) {
                            displayConnectionStatus.setText("CONNECTED TO: " + dbUrl);
                            disconnectDatabaseButton.setEnabled(true);
                            connection = DriverManager.getConnection(dbUrl, username, password);
                            
                            // Check user privileges
                            checkUserPrivileges(connection, username);
                        } else {
                            displayConnectionStatus.setText("Database URL not found in properties file: " + selectedPropertyFile);
                        }
                    } else {
                        displayConnectionStatus.setText("DB URL properties file not found: " + selectedPropertyFile);
                    }
                } else {
                    displayConnectionStatus.setText("No connection established. Invalid username or password.");
                }
            } else {
                displayConnectionStatus.setText("User properties file not found: " + userPropertiesComboBox.getSelectedItem().toString());
            }
        } catch (IOException | SQLException e) {
            displayConnectionStatus.setText("Error loading properties file or connecting to the database: " + e.getMessage());
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
    /*
     Checks the privileges of the connected user.
     param: connection The database connection.
     param: username The username of the connected user.
     */
    private void checkUserPrivileges(Connection connection, String username) {
        try {
            Statement statement = connection.createStatement();
            
            // Execute a query to check user privileges
            ResultSet resultSet = statement.executeQuery("SHOW GRANTS FOR '" + username + "'");
            
            // Process the result set
            boolean hasSelectPrivilege = false;
            boolean hasUpdatePrivilege = false;
            while (resultSet.next()) {
                String grant = resultSet.getString(1);
                if (grant.contains("SELECT")) {
                    hasSelectPrivilege = true;
                }
                if (grant.contains("UPDATE")) {
                    hasUpdatePrivilege = true;
                }
            }
            
            // Update UI based on user privileges
            if (hasSelectPrivilege) {
                displaySQLresult.setText("User has SELECT privilege.");
            } else {
                displaySQLresult.setText("User does not have SELECT privilege.");
            }
            if (hasUpdatePrivilege) {
                displaySQLresult.append("\nUser has UPDATE privilege.");
            } else {
                displaySQLresult.append("\nUser does not have UPDATE privilege.");
            }
            
            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            
        }
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

    
    private void executeSQLCommand(String sqlCommand, String selectedPropertyFile) { 
        try {
            // Retrieve the database URL, username, and password from the properties file
            Properties props = loadProperties(selectedPropertyFile);
            if (props != null) {
                String dbUrl = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                // Establish the database connection
                connection = DriverManager.getConnection(dbUrl, username, password);

                // Create a statement object
                Statement statement = connection.createStatement();

                // Check if the SQL command is an UPDATE or SELECT query
                if (sqlCommand.trim().toUpperCase().startsWith("UPDATE") || sqlCommand.trim().toUpperCase().startsWith("DELETE") || sqlCommand.trim().toUpperCase().startsWith("INSERT")) {
                    // Execute the UPDATE, DELETE, or INSERT query
                    int rowsAffected = statement.executeUpdate(sqlCommand);
                    displaySQLresult.setText("Number of rows affected: " + rowsAffected);
                } else if (sqlCommand.trim().toUpperCase().startsWith("SELECT")) {
                    // Execute the SELECT query
                    ResultSet resultSet = statement.executeQuery(sqlCommand);
                    displayResultSet(resultSet);
                } else {
                    // Handle unsupported SQL commands
                    displaySQLresult.setText("Unsupported SQL command: " + sqlCommand);
                }

                // Close resources
                statement.close();
            }
        } catch (SQLException e) {
            displaySQLresult.setText("Error executing SQL command: " + e.getMessage());
        }
    }
      
    //SQL OUTPUT WINDOW
    private void displayResultSet(ResultSet resultSet) throws SQLException {
        // Get metadata
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Build metadata string
        StringBuilder metaDataString = new StringBuilder();
        for (int i = 1; i <= columnCount; i++) {
            metaDataString.append(metaData.getColumnName(i)).append("\t");
        }
        metaDataString.append("\n\n");

        // Build result text
        StringBuilder resultText = new StringBuilder();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                resultText.append(resultSet.getString(i)).append("\t");
            }
            resultText.append("\n\n");
        }

        // Display the results in textArea_1
        displaySQLresult.setText("Metadata:\n" + metaDataString.toString() + "\nData:\n" + resultText.toString());

        // Close the result set
        resultSet.close();
    }
}
