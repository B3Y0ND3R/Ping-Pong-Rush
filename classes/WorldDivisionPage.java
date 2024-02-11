package gameV;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class WorldDivisionPage extends JFrame {

    private int gameWidth = 500; 
    private int gameHeight = 500; 

    public WorldDivisionPage() {
        this.setTitle("World Division Page");
        this.setSize(gameWidth, gameHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

  
        WorldData worldData = parseJson("https://api.myjson.online/v1/records/4801b3b6-cb63-44f0-9412-dab1ae984796");

  
        displayPlayerList(panel, worldData);

        JScrollPane scrollPane = new JScrollPane(panel);
        this.add(scrollPane);
        this.setVisible(true);
    }

    private WorldData parseJson(String jsonUrl) {
        try {
            InputStreamReader reader = new InputStreamReader(new URL(jsonUrl).openStream());
            return new Gson().fromJson(reader, WorldData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void displayPlayerList(JPanel panel, WorldData worldData) {
        if (worldData != null) {
            for (PlayerData playerData : worldData.getData()) {
                JPanel playerPanel = new JPanel(new BorderLayout());
                playerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                playerPanel.setBackground(Color.BLACK);

                JLabel nameLabel = new JLabel("Name: " + playerData.getName());
                nameLabel.setForeground(Color.YELLOW);


                try {
                    ImageIcon imageIcon = new ImageIcon(new URL(playerData.getImg()));
                    Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon resizedImageIcon = new ImageIcon(image);
                    JLabel imageLabel = new JLabel(resizedImageIcon);
                    playerPanel.add(imageLabel, BorderLayout.WEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JButton detailsButton = new JButton("Details");
                detailsButton.setBackground(Color.BLACK);
                detailsButton.setForeground(Color.YELLOW);
                detailsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openPlayerDetailsPage(playerData);
                    }
                });

                playerPanel.add(nameLabel, BorderLayout.CENTER);
                playerPanel.add(detailsButton, BorderLayout.EAST);

                panel.add(playerPanel);
                panel.add(Box.createRigidArea(new Dimension(0, 10))); 
            }
        }
    }

    private void openPlayerDetailsPage(PlayerData playerData) {

        JFrame playerDetailsFrame = new JFrame("Player Details");
        playerDetailsFrame.setSize(400, 400);
        playerDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.BLACK);

        JLabel nameLabel = new JLabel("Name: " + playerData.getName());
        nameLabel.setForeground(Color.YELLOW);
        detailsPanel.add(nameLabel);

        JLabel rankLabel = new JLabel("Rank: " + playerData.getDetails().getRank());
        rankLabel.setForeground(Color.YELLOW);
        detailsPanel.add(rankLabel);

        JLabel divisionLabel = new JLabel("Division: " + playerData.getDetails().getDivision());
        divisionLabel.setForeground(Color.YELLOW);
        detailsPanel.add(divisionLabel);

        JLabel associationLabel = new JLabel("Association: " + playerData.getDetails().getAssociation());
        associationLabel.setForeground(Color.YELLOW);
        detailsPanel.add(associationLabel);


        try {
            ImageIcon imageIcon = new ImageIcon(new URL(playerData.getImg()));
            Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon resizedImageIcon = new ImageIcon(image);
            JLabel imageLabel = new JLabel(resizedImageIcon);
            detailsPanel.add(imageLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton returnButton = new JButton("Return to World Division");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDetailsFrame.dispose();
            }
        });
        detailsPanel.add(returnButton);

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        playerDetailsFrame.add(scrollPane);
        playerDetailsFrame.setVisible(true);
    }

}
