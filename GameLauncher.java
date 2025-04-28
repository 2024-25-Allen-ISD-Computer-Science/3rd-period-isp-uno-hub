import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class GameLauncher {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        JButton flappyBirdButton = new JButton("Flappy Bird!");
        JButton cloudGameButton = new JButton("Cloud Game!");
        JButton unoGameButton = new JButton("Uno!");

        flappyBirdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runFlappyBird();
            }
        });

        cloudGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runCloudGame();
            }
        });

        unoGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runUnoGame();
            }
        });

        frame.add(flappyBirdButton);
        frame.add(cloudGameButton);
        frame.add(unoGameButton);
        frame.setVisible(true);
    }

    private static void runFlappyBird() {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "FlappyBird/src", "App");
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start Flappy Bird!");
        }
    }

    private static void runCloudGame() {
        try {
            
            ProcessBuilder pb = new ProcessBuilder("python3", "CloudGame.py");
            pb.directory(new File("CloudGame"));
            pb.inheritIO();
            
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start Cloud Game!");
        }
    }

    private static void runUnoGame() {
        try {
            
            ProcessBuilder pb = new ProcessBuilder("python3", "main.py");
            pb.directory(new File("UnoGame"));
            pb.inheritIO();
            
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to start Uno!");
        }
    }
}