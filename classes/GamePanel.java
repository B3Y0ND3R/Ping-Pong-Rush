package gameV;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class GamePanel extends JPanel implements Runnable {
	//LoginSignUpPage login;
	private String userPlayerName;
    private String aiPlayerName;
    int gameWidth = 1000; 
    int gameHeight = (int) (gameWidth * (0.5555)); 
    
    private int winThreshold = 2; 
    private boolean gameEnded = false; 
    
    private boolean gameEndingWindowDisplayed = false;
    
    private boolean gamePaused = false;


    
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle playerPaddle;
    Paddle computerPaddle;
    Ball ball;
    Score score;
    private Difficulty difficulty; 
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ping";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    LoginSignUpPage login=new LoginSignUpPage(gameWidth,gameHeight);


    GamePanel(String userPlayerName, String aiPlayerName, Difficulty difficulty) {
        //this.userPlayerName = login.username;
        this.aiPlayerName = aiPlayerName;
        this.difficulty = difficulty; 
       // this.login=login;
        this.userPlayerName=userPlayerName;
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        int initialSpeed = 2 * difficulty.getSpeedMultiplier();
        int randomXDirection = (random.nextBoolean()) ? 1 : -1;
        int randomYDirection = (random.nextBoolean()) ? 1 : -1;
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER, difficulty);
        ball.xVelocity = randomXDirection * initialSpeed;

        int maxBallSpeed = difficulty.getMaxBallSpeed();
        if (Math.abs(ball.xVelocity) > maxBallSpeed) {
            ball.xVelocity = maxBallSpeed * (ball.xVelocity > 0 ? 1 : -1);
        }
        ball.yVelocity = randomYDirection * initialSpeed;
    }


    public void newPaddles() {
        playerPaddle = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        computerPaddle = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString(userPlayerName + ": " + score.player1, 20, 30);

        g.drawString(aiPlayerName + ": " + score.player2, GAME_WIDTH - 180, 30);
        playerPaddle.draw(g);
        computerPaddle.draw(g);
        ball.draw(g);
        score.draw(g);
        Toolkit.getDefaultToolkit().sync();
    }

    public void move() {
        playerPaddle.move();
        computerPaddle.moveAI(ball.y);
        ball.move();
    }

    public void checkCollision() {
    	if (!gamePaused) {
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }

        if (ball.intersects(playerPaddle)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; 
            if (ball.yVelocity > 0)
                ball.yVelocity++; 
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(computerPaddle)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++; 
            if (ball.yVelocity > 0)
                ball.yVelocity++; 
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if (playerPaddle.y <= 0)
            playerPaddle.y = 0;
        if (playerPaddle.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
            playerPaddle.y = GAME_HEIGHT - PADDLE_HEIGHT;
        if (computerPaddle.y <= 0)
            computerPaddle.y = 0;
        if (computerPaddle.y >= (GAME_HEIGHT - PADDLE_HEIGHT))
            computerPaddle.y = GAME_HEIGHT - PADDLE_HEIGHT;


        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2: " + score.player2);
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1: " + score.player1);
            //System.out.println(login.username);
        }


        if (!gameEnded) {
            if (score.player1 >= score.player2 + winThreshold) {
            	//updateScoreInDatabase(userPlayerName,score.player1);
                endGame(userPlayerName, score.player1);
            } else if (score.player2 >= score.player1 + winThreshold) {
            	//updateScoreInDatabase(login.username,score.player1);
                endGame(aiPlayerName, score.player2);
            }
        }
    } 
    }
    public void updateScoreInDatabase(int newScore) {
    	  try {
              Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
              String query = "UPDATE users SET score=? WHERE username=?";
              PreparedStatement preparedStatement = connection.prepareStatement(query);
              preparedStatement.setInt(1, newScore);
              preparedStatement.setString(2, userPlayerName);

              int rowsUpdated = preparedStatement.executeUpdate();

              int rowsAffected = preparedStatement.executeUpdate();
              if (rowsAffected > 0) {
                  System.out.println("User data updated successfully.");
              } else {
                  System.out.println("User data update failed, no " + userPlayerName);
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
    }
    
    public void deleteFromDatabase() {
  	  try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "DELETE FROM users WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userPlayerName);

            int rowsUpdated = preparedStatement.executeUpdate();

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User data deleted successfully.");
            } else {
                //System.out.println("User data delete failed, no " + userPlayerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
  }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }
    
    
    private void endGame(String winnerName, int winnerScore) {
        gameEnded = true;
        if (!gameEndingWindowDisplayed) {
            gamePaused = true; 

            Color panelBackgroundColor = new Color(0, 0, 0);
            Color panelTextColor = Color.WHITE; 

            JFrame endGameFrame = new JFrame("Game Over");
            JPanel endGamePanel = new JPanel(new GridBagLayout());
            endGamePanel.setBackground(panelBackgroundColor);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.CENTER;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.insets = new Insets(10, 10, 10, 10);

            JLabel winnerLabel = new JLabel("Winner: " + winnerName);
            winnerLabel.setForeground(panelTextColor);
            constraints.gridx = 0;
            constraints.gridy = 0;
            endGamePanel.add(winnerLabel, constraints);

            JLabel scoreLabel = new JLabel("Score: " + winnerScore);
            scoreLabel.setForeground(panelTextColor);
            constraints.gridy = 1;
            endGamePanel.add(scoreLabel, constraints);

            JButton playAgainButton = new JButton("Play Again");
            playAgainButton.setForeground(panelTextColor);
            playAgainButton.setBackground(panelBackgroundColor);
            playAgainButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    endGameFrame.dispose();
                    restartGame();
                }
            });
            constraints.gridy = 2;
            endGamePanel.add(playAgainButton, constraints);

            /*JButton exitButton = new JButton("Exit");
            exitButton.setForeground(panelTextColor);
            exitButton.setBackground(panelBackgroundColor);
            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            constraints.gridy = 3;
            endGamePanel.add(exitButton, constraints);*/
            
            JButton worldDivisionButton = new JButton("World Division");
            worldDivisionButton.setForeground(panelTextColor);
            worldDivisionButton.setBackground(panelBackgroundColor);
            worldDivisionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //openWorldDivisionPage();
                	new WorldDivisionPage();
                }
            });
            constraints.gridy = 3; 
            endGamePanel.add(worldDivisionButton, constraints);
            
            
            JButton deleteAccButton = new JButton("Delete Account");
            deleteAccButton.setForeground(panelTextColor);
            deleteAccButton.setBackground(panelBackgroundColor);
            deleteAccButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteFromDatabase();
                }
            });
            constraints.gridy = 4; 
            endGamePanel.add(deleteAccButton, constraints);


            endGameFrame.add(endGamePanel);
            endGameFrame.setSize(300, 250);
            endGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            endGameFrame.setVisible(true);
            endGameFrame.setLocationRelativeTo(null); 
            updateScoreInDatabase(score.player1);
            gameEndingWindowDisplayed = true; 
        }
    }
    
    /*private void openWorldDivisionPage() {
        try {
            // Replace "YOUR_JSON_URL" with the actual URL of your JSON data
            URL url = new URL("YOUR_JSON_URL");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Get response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON data
                Gson gson = new Gson();
                JsonObject jsonData = gson.fromJson(response.toString(), JsonObject.class);
                JsonArray data = jsonData.getAsJsonArray("data");

                // Create a new window or panel to display the information
                JFrame worldDivisionFrame = new JFrame("World Division");
                WorldDivisionPage worldDivisionPage = new WorldDivisionPage(data);
                worldDivisionFrame.getContentPane().add(worldDivisionPage);
                worldDivisionFrame.setSize(800, 600);  // Adjust the size as needed
                worldDivisionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close only this window
                worldDivisionFrame.setResizable(false);
                worldDivisionFrame.setVisible(true);
                worldDivisionFrame.setLocationRelativeTo(null);

            } else {
                System.out.println("Error: Unable to fetch JSON data. HTTP response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/


    private void restartGame() {
        gameEnded = false;
        gameEndingWindowDisplayed = false;
        gamePaused = false; 
        score.player1 = 0;
        score.player2 = 0;
        newPaddles();
        newBall();
    }


    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            playerPaddle.keyPressed(e);
            computerPaddle.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            playerPaddle.keyReleased(e);
            computerPaddle.keyReleased(e);
        }
    }
}