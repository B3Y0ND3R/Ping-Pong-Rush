package gameV;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginSignUpPage extends JPanel {
    private int gameWidth;
    private int gameHeight;
    private JTextField usernameField;
    private JPasswordField passwordField;
    //GamePanel gPanel;
    
    public String username;

    public LoginSignUpPage(int gameWidth, int gameHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 1));
        loginPanel.setBackground(Color.BLACK);

        JLabel title = new JLabel("Game Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.YELLOW);
        loginPanel.add(title);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBackground(Color.BLACK);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        usernameField.setBackground(Color.BLACK);
        usernameField.setForeground(Color.YELLOW);
        passwordField.setBackground(Color.BLACK);
        passwordField.setForeground(Color.YELLOW);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.YELLOW); 
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.YELLOW); 

        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        loginPanel.add(inputPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        JButton loginButton = new JButton("Login");
        loginButton.setForeground(Color.DARK_GRAY);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setForeground(Color.DARK_GRAY);
        loginButton.setBackground(Color.ORANGE);
        signUpButton.setBackground(Color.ORANGE);
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        username = usernameField.getText();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String passwordString = new String(password);

                if (performLogin(username, passwordString)) {
               // gPanel.setName(username);
                    showStartMenu();
                } else {
                    JOptionPane.showMessageDialog(LoginSignUpPage.this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String passwordString = new String(password);

                if (performSignUp(username, passwordString)) {
                	//gPanel.setName(username);
                    showStartMenu();
                } else {
                    JOptionPane.showMessageDialog(LoginSignUpPage.this, "Error while signing up", "Sign-Up Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginPanel.add(buttonPanel);
        add(loginPanel, BorderLayout.CENTER);
    }

    private boolean performLogin(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/ping", "root", "");

            String query = "SELECT * FROM users WHERE BINARY username=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                connection.close();
                return true;
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

  /*  private boolean performSignUp(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/ping", "root", "");

            String query = "INSERT INTO users (username, password, score) VALUES (?, ?, 0)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int result = preparedStatement.executeUpdate();

            connection.close();

            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }*/
    
    private boolean performSignUp(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/ping", "root", "");


            if (userExists(connection, username)) {
                System.out.println("User with the same username already exists. Please choose a different username.");
                connection.close();
                return false;
            }

            String query = "INSERT INTO users (username, password, score) VALUES (?, ?, 0)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int result = preparedStatement.executeUpdate();

            connection.close();

            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean userExists(Connection connection, String username) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
        checkStatement.setString(1, username);

        ResultSet resultSet = checkStatement.executeQuery();
        resultSet.next();
        
        int count = resultSet.getInt(1);

        return count > 0;
    }


    private void showStartMenu() {
        JFrame startFrame = new JFrame("Start Menu");
        StartMenu startMenu = new StartMenu(gameWidth, gameHeight,usernameField.getText());
        startFrame.getContentPane().add(startMenu);
        startFrame.setSize(gameWidth, gameHeight);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setResizable(true);
        startFrame.setVisible(true);
        startFrame.setLocationRelativeTo(null);
    }
}