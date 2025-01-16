import java.awt.*; 
import java.awt.event.*;
import java.util.ArrayList; //stores all the pipes in our game
import java.util.Random; // for placing pipes at random places
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener{

    int boardWidth = 360;
    int boardHeight = 640;
    
    // IMAGES
    // The four following variables are going to store image objects
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //BIRD
    int birdX = boardWidth/8; // 1/8 from the left side of the screen
    int birdY = boardHeight/2; // half of the height
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird 
    {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img; 

        Bird(Image img)
        {
            this.img = img;
        }
    }

    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe 
    {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false; // to see whether flappybird passed the pipes

        Pipe(Image img)
        {
            this.img = img;
        }

    }



    //game logic

    Bird bird;
    int velocityY = 0; //for bird
    int gravity = 1; //for bird
    int velocitx = -4; // for the pipes

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop; 
    Timer placePipesTimer;
    
    boolean gameOver = false;

    double score = 0;
    int highScore = 0;

    FlappyBird()
    {
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        setFocusable(true);
        addKeyListener(this); 

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //place pipes timer : calls "placePipes" every 1500 milisecons
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                placePipes();
            }
        });
        placePipesTimer.start();

        //game timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start(); // starts the timer

    }

    //(0-1) * (pipeHeight/2) -> (0,256)
    //128
    //0-128-(0,256) --> pipeHeight/4 -> 3/4 of piprHeight

    public void placePipes()
    {
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); 
        draw(g);
    }

    public void draw(Graphics g)
    {
        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        //bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for (int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver)
        {
            g.drawString("Game Over: " + String.valueOf((int)score), 10, 35);
        }
        else
        {
            g.drawString(String.valueOf((int) score), 10, 35);
        }

        //High score
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("High Score: " + highScore, 10, 70);
    }
    
    
    public void move()
    {
        //bird                                                                        
        velocityY += gravity;     //update the velocity with the gravity// updates all the x and y position of the object
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipes
        for (int i = 0; i < pipes.size(); i++)
        {
            Pipe pipe = pipes.get(i);
            pipe.x += velocitx; // every frame pipes will move -4 to the left

            if (!pipe.passed && bird.x > pipe.x + pipe.width)  
            {
                pipe.passed = true;
                score += 0.5;

                if ((int) score > highScore) {
                    highScore = (int) score;
                }
            }

            if (collision(bird, pipe))
            {
                gameOver = true;
            };
        }

        if (bird.y > boardHeight)
        {
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b)
    {
        return a.x < b.x + b.width &&
        a.x + a.width > b.x &&
        a.y < b.y + b.height &&
        a.y + a.height >b.y;
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver)
        {
            if ((int) score > highScore) {
                highScore = (int) score; // Update the high score
            }
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            velocityY = -9;

            if (gameOver)
            {
                //restart the game by resetting the conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {} 
    @Override
    public void keyTyped(KeyEvent e) {}

}
