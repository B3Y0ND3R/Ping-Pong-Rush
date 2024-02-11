package gameV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultyChoice extends JPanel {

    JButton easyButton;
    JButton mediumButton;
    JButton hardButton;
    StartMenu startMenu;
    private String userPlayerName;
    private String aiPlayerName;

    DifficultyChoice(int gameWidth, int gameHeight, StartMenu startMenu) {
        this.startMenu = startMenu;

        this.setLayout(null);

        
        hardButton = new JButton("Easy");
        hardButton.setBackground(Color.ORANGE);
        hardButton.setBounds(400, 200, 200, 50);
        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startMenu.startGame(Difficulty.HARD);
            }
        });
        this.add(hardButton);
        
        
        mediumButton = new JButton("Medium");
        mediumButton.setBackground(Color.ORANGE);
        mediumButton.setBounds(400, 300, 200, 50);
        mediumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startMenu.startGame(Difficulty.MEDIUM);
            }
        });
        this.add(mediumButton);
        
        
        easyButton = new JButton("Hard");
        easyButton.setBackground(Color.ORANGE);
        easyButton.setBounds(400, 400, 200, 50);
        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startMenu.startGame(Difficulty.EASY);
            }
        });
        this.add(easyButton);


  
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 60));
        g.drawString("Select Difficulty", 250, 100);
    }
}
