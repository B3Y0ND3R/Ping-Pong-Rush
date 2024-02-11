package gameV;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    public DatabaseManager(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public int loadUserScore(String username) {
        int score = 0;
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            String query = "SELECT score FROM users WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                score = resultSet.getInt("score");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return score;
    }

    public void updateUserScore(String username, int score) {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            String query = "UPDATE users SET score=? WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, score);
            preparedStatement.setString(2, username);

            int rowsUpdated = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

