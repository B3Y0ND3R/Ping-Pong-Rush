package gameV;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StartMenu extends JPanel {

    private String userPlayerName ;
    private String aiPlayerName = "Player2";

    JButton startButton;
    JButton exitButton;
    JButton muteButton;
    boolean isMuted = false;
    int gameWidth;
    int gameHeight;
    Clip backgroundMusicClip;

    StartMenu(int gameWidth, int gameHeight,String userPlayerName) {
    	// showSplashScreen(); 
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.userPlayerName=userPlayerName;

        this.setLayout(null);

        startButton = new JButton("Start Game");
        startButton.setForeground(Color.DARK_GRAY);
        startButton.setBackground(new Color(0, 255, 255));
        startButton.setBounds(400, 300, 200, 50);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDifficultyChoice();
            }
        });
        this.add(startButton);

        exitButton = new JButton("Exit");
        exitButton.setForeground(Color.DARK_GRAY);
        exitButton.setBackground(Color.RED);
        exitButton.setBounds(400, 400, 200, 50);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.add(exitButton);

        muteButton = new JButton("Mute");
        muteButton.setForeground(Color.DARK_GRAY);
        muteButton.setBackground(Color.ORANGE);
        muteButton.setBounds(700, 20, 80, 30);
        muteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isMuted) {
                    backgroundMusicClip.start();
                } else {
                    backgroundMusicClip.stop();
                }
                isMuted = !isMuted;
            }
        });
        this.add(muteButton);

        try {
            File audioFile = new File("C:\\Users\\HASIB\\OneDrive\\Desktop\\all\\music.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioStream);
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusicClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   /* public void showSplashScreen() {
        JDialog splashDialog = new JDialog();
        JLabel splashLabel = new JLabel(new ImageIcon("C:/Users/HASIB/Videos/Aylin Mirza - GIFs Collection _ 2D Loops.gif"));
        splashDialog.add(splashLabel);
        splashDialog.setUndecorated(true);
        splashDialog.pack();
        splashDialog.setLocationRelativeTo(null);
        splashDialog.setVisible(true);

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splashDialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }*/



    private void showDifficultyChoice() {
        JFrame frame = new JFrame("Select Difficulty");
        DifficultyChoice difficultyChoice = new DifficultyChoice(gameWidth, gameHeight, this);
        frame.getContentPane().add(difficultyChoice);
        frame.setSize(gameWidth, gameHeight);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void startGame(Difficulty difficulty) {
        /*if (difficulty == Difficulty.EASY) {
            userPlayerName = "Player1 (You)";
        } else if (difficulty == Difficulty.MEDIUM) {
            userPlayerName = "Player1 (You)";
        } else if (difficulty == Difficulty.HARD) {
            userPlayerName = "Player1 (You)";
        }*/
        aiPlayerName = "Player2";
        GameFrame frame = new GameFrame(userPlayerName, aiPlayerName, difficulty);
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, gameWidth, gameHeight);
        g.setColor(Color.yellow);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawString("Ping Pong Rush", 250, 200);
    }
}