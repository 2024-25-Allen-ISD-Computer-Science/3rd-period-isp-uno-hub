import java.awt.*; 
import java.awt.event.*;
import java.util.ArrayList; //stores all the pipes in the game
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
    Image logoImg;
    Image gameOverImg;
    Image groundImg;

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
    boolean showStartScreen = true; // Flag for the start screen

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
        logoImg = new ImageIcon(getClass().getResource("./flappy-bird-logo-black-and-white-transformed.png")).getImage();
        gameOverImg = new ImageIcon(getClass().getResource("./gameover.png")).getImage();
        groundImg = new ImageIcon(getClass().getResource("./ground.png")).getImage();

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
        int openingSpace = boardHeight / 4;
        
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
        if (showStartScreen) {
            drawStartScreen(g);
        } else {
            draw(g);
        }
    }

    public void drawStartScreen(Graphics g) {
        // Draw background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // Draw logo
        int logoWidth = 350;
        int logoHeight = 93;
        int logoX = (boardWidth - logoWidth) / 2;
        int logoY = boardHeight / 4;
        g.drawImage(logoImg, logoX, logoY, logoWidth, logoHeight, null);

        // Draw "Press SPACE to start" text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Press SPACE to start", boardWidth / 6, boardHeight / 2);
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

        g.drawImage(groundImg, 0, boardHeight - 64, boardWidth, 64, null);

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver)
        {
        // Game Over image
        g.drawImage(gameOverImg, 5, boardHeight / 4, 350, 92, null);

        // Display the current score
        g.drawString("Score: " + (int)score, boardWidth / 2 - 70, boardHeight / 2 + 50);

        // Display the highest score
        g.drawString("High Score: " + highScore, boardWidth / 2 - 100, boardHeight / 2 + 100);
        }
        else
        {
            // Show the highest score at the top while the game is ongoing
            g.drawString("High Score: " + highScore, 10, 35);

            // Show the current score while the game is ongoing
            g.drawString(String.valueOf((int)score), 10, 75);
        }
    }
    
    
    public void move()
    {
        //bird                                                                        
        velocityY += gravity;    
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

        if (bird.y + bird.height >= boardHeight - 64) {
            bird.y = boardHeight - 64 - bird.height;
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
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            velocityY = -9;
            //playSound("sfx_swooshing.wav"); //plays sound 

            if (showStartScreen) {
                // Start the game
                showStartScreen = false;
                gameLoop.start();
                placePipesTimer.start();
            } else if (gameOver)
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
