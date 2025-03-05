/* Question No.3 (b):
 * Game Initialization

A 2D array (board) represents the game grid.
The game starts with an empty board and a queue of upcoming blocks.
A Timer triggers the automatic downward movement of the current block every 500 milliseconds.
Block Generation and Movement

New blocks are randomly selected and placed at the top of the board using generateNewBlock().
Blocks move down automatically with actionPerformed().
The player can move blocks left or right (moveBlock()) and rotate them (rotateBlock()).
isValidMove() ensures blocks stay within boundaries and don’t overlap.
Placing Blocks and Clearing Rows

When a block can’t move down, it is placed on the board using placeBlock().
clearRows() checks if any row is fully filled and removes it, shifting the above rows down and increasing the score.
Game Over Condition

If a new block cannot be placed at the top, the game ends (gameOver = true).
The screen displays “Game Over” along with the final score.
Rendering the Game

paintComponent() updates the board visually, drawing placed blocks in blue and the falling block in red.
The user interacts using the keyboard (KeyAdapter), controlling block movement and rotation.
Game Loop Execution

The main method initializes the game window, starts the timer, and keeps the game running until the player loses.
 */

// Tetris Game Implementation in Java
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import javax.swing.*;
public class TetrisGame extends JPanel implements ActionListener {
    private static final int ROWS = 20;          // Number of rows in the game board
    private static final int COLS = 10;          // Number of columns in the game board
    private static final int BLOCK_SIZE = 30;    // Size of each block
    private static final int TIMER_DELAY = 500;  // Delay between block drops (in milliseconds)
    
    private Timer timer;  // Timer to control the game loop
    private int[][] board = new int[ROWS][COLS]; // 2D array representing the game board
    private Queue<int[][]> blockQueue = new LinkedList<>(); // Queue to store falling blocks
    private int[][] currentBlock;  // 2D array representing the current falling block
    private int blockRow, blockCol; // Position of the current block
    private int score = 0; // Player's score
    private boolean gameOver = false;  // Flag to check if the game is over
    private boolean showGameOverScreen = false; // Flag to show the game over screen
    private boolean specialPowerActive = false; // Power-up flag
    
    // Constructor to initialize the game
    public TetrisGame() {
        setPreferredSize(new Dimension(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TetrisKeyAdapter());
        generateNewBlock();  // Generate the first block
        timer = new Timer(TIMER_DELAY, this); // Initialize the game timer
        timer.start();  // Start the game loop
    }

    // Generates a new block and enqueues it
    private void generateNewBlock() {
        int[][][] possibleBlocks = {
            {{1, 1, 1, 1}}, // I Block
            {{1, 1}, {1, 1}}, // O Block
            {{1, 1, 0}, {0, 1, 1}}, // S Block
            {{0, 1, 1}, {1, 1, 0}}, // Z Block
            {{1, 1, 1}, {0, 1, 0}}, // T Block
        };
        Random random = new Random();
        if (blockQueue.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                blockQueue.add(possibleBlocks[random.nextInt(possibleBlocks.length)]);
            }
        }
        currentBlock = blockQueue.poll();  // Get the next block
        blockQueue.add(possibleBlocks[random.nextInt(possibleBlocks.length)]);  // Add a new block to the queue
        blockRow = 0;  // Start from the top row
        blockCol = COLS / 2 - currentBlock[0].length / 2;  // Start from the middle column
        if (!isValidMove(blockRow, blockCol)) {  // If the block can't fit, the game is over
            gameOver = true;
            showGameOverScreen = true;
        }
    }

    // Checks if a move is valid (collision detection)
    private boolean isValidMove(int newRow, int newCol) {
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[i].length; j++) {
                if (currentBlock[i][j] == 1) {
                    int r = newRow + i, c = newCol + j;
                    if (r >= ROWS || c < 0 || c >= COLS || (r >= 0 && board[r][c] == 1)) {
                        return false;  // Collision detected
                    }
                }
            }
        }
        return true;  // No collision, valid move
    }

    // Places the block in the stack (game board state)
    private void placeBlock() {
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[i].length; j++) {
                if (currentBlock[i][j] == 1) {
                    board[blockRow + i][blockCol + j] = 1;
                }
            }
        }
        clearRows();  // Check for completed rows
        generateNewBlock();  // Generate a new block
    }

    // Clears completed rows and updates the score
    private void clearRows() {
        for (int i = 0; i < ROWS; i++) {
            boolean fullRow = true;
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                score += 100;  // Increase score for clearing a row
                for (int k = i; k > 0; k--) {
                    board[k] = board[k - 1].clone();
                }
                board[0] = new int[COLS];  // Add a new empty row at the top
            }
        }
    }

    // Moves the block left or right
    private void moveBlock(int dx) {
        if (isValidMove(blockRow, blockCol + dx)) {
            blockCol += dx;
        }
    }

    // Moves the block down automatically
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (isValidMove(blockRow + 1, blockCol)) {
                blockRow++;  // Move block down
            } else {
                placeBlock();  // Place the block on the board
            }
            repaint();  // Redraw the game state
        }
    }

    // Draw the game components on the panel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the game board
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == 1) {
                    g.setColor(Color.BLUE);  // Block color
                    g.fillRect(j * BLOCK_SIZE, i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // Draw the current falling block
        g.setColor(Color.RED);  // Falling block color
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[i].length; j++) {
                if (currentBlock[i][j] == 1) {
                    g.fillRect((blockCol + j) * BLOCK_SIZE, (blockRow + i) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        // If game over, display a message and final score
        if (showGameOverScreen) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER!", 80, 150);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Final Score: " + score, 100, 200);
        }
    }

    // Handles user input for controlling the game
    private class TetrisKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (gameOver) return;
            if (e.getKeyCode() == KeyEvent.VK_LEFT) moveBlock(-1);  // Move left
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveBlock(1);   // Move right
            if (e.getKeyCode() == KeyEvent.VK_DOWN) actionPerformed(null);  // Move down
            if (e.getKeyCode() == KeyEvent.VK_SPACE) rotateBlock();  // Rotate block
            repaint();  // Redraw the game
        }
    }

    // Rotates the current block if possible
    private void rotateBlock() {
        int[][] rotatedBlock = new int[currentBlock[0].length][currentBlock.length];
        for (int i = 0; i < currentBlock.length; i++) {
            for (int j = 0; j < currentBlock[i].length; j++) {
                rotatedBlock[j][currentBlock.length - 1 - i] = currentBlock[i][j];
            }
        }
        if (isValidMove(blockRow, blockCol)) {
            currentBlock = rotatedBlock;  // Apply the rotation
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        TetrisGame game = new TetrisGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}