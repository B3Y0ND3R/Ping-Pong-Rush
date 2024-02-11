package gameV;

import javax.swing.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PONG {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showSplashScreen();
        });
    }

    public static void showSplashScreen() {
        int gameWidth = 1000; 
        int gameHeight = (int) (gameWidth * (0.5555)); 

        JDialog splashDialog = new JDialog();
        JLabel splashLabel = new JLabel(new ImageIcon("C:/Users/HASIB/Videos/Aylin Mirza - GIFs Collection _ 2D Loops.gif"));
        splashDialog.add(splashLabel);
        splashDialog.setUndecorated(true);
        splashDialog.pack();
        splashDialog.setLocationRelativeTo(null);
        splashDialog.setVisible(true);

        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                splashDialog.dispose();
                showLoginSignUpPage(gameWidth, gameHeight);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void showLoginSignUpPage(int gameWidth, int gameHeight) {
        JFrame loginSignUpFrame = new JFrame("Login or Sign Up");
        LoginSignUpPage loginSignUpPage = new LoginSignUpPage(gameWidth, gameHeight);
        loginSignUpFrame.add(loginSignUpPage);
        loginSignUpFrame.setSize(gameWidth, gameHeight);
        loginSignUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginSignUpFrame.setResizable(true);
        loginSignUpFrame.setVisible(true);
        loginSignUpFrame.setLocationRelativeTo(null);
    }
}