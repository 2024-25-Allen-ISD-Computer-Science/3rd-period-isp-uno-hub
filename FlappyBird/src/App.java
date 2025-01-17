import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        
        // game window
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");

        frame.setSize(boardWidth, boardHeight); //set the size

        frame.setLocationRelativeTo(null); //places the window at the center of the screen
        frame.setResizable(false); // so that players cannot resize the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when player clicks x button on the window it will terminate the program

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird); 
        frame.pack(); // so that the bar on top does not count when measuring the size
        flappyBird.requestFocus(); //flappybird receives inputs from user
        frame.setVisible(true); 
    }
}