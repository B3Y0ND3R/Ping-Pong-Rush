package gameV;

import java.awt.Color;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
	 int gameWidth = 1000; 
     int gameHeight = (int) (gameWidth * (0.5555)); 
    GamePanel panel;
    LoginSignUpPage login = new LoginSignUpPage(gameWidth,gameHeight);

    GameFrame(String userPlayerName, String aiPlayerName, Difficulty difficulty) {
        panel = new GamePanel(userPlayerName, aiPlayerName, difficulty);
        this.add(panel);
        this.setTitle("Pong Game");
        this.setResizable(true);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    private void playBackgroundMusic(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}