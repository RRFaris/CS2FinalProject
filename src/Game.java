import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Game implements MouseListener {
    private Tile[][] board;
    private final int TOTAL_MINES = 40;

    public static final int BOARD_WIDTH = 18;
    public static final int BOARD_HEIGHT = 14;

    // Tile images
    private Image emptyTileImage;
    private Image landTileImage;

    // Game states
    private int state;
    public final int WELCOME = 0;
    public final int PLAYING = 1;
    public final int LOST = 2;

    GameViewer window;

    // Mouse stuff
    int mouseX;
    int mouseY;
    int mouseButton;

    // Use to set up the mines after user's first click
    int numMouseClicks;

    public Game() {
        window = new GameViewer(this);

        // Initialize game state
        state = WELCOME;

        // Initialize board
        board = new Tile[BOARD_WIDTH][BOARD_HEIGHT];

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                // Make color alternate between tiles
                if ((i + j) % 2 == 0) {
                    emptyTileImage = new ImageIcon("Resources/emptyLightTile.png").getImage();
                    landTileImage = new ImageIcon("Resources/landLightTile.png").getImage();
                }
                else {
                    emptyTileImage = new ImageIcon("Resources/emptyDarkTile.png").getImage();
                    landTileImage = new ImageIcon("Resources/landDarkTile.png").getImage();
                }
                board[i][j] = new Tile(window.BOARD_X + (i*Tile.TILE_WIDTH), window.BOARD_Y + (j*Tile.TILE_WIDTH), i, j, emptyTileImage, landTileImage);
            }
        }

        // Initialize MouseListener
        window.addMouseListener(this);
    }

    public Tile[][] getBoard() {
        return board;
    }

    public int getState() {
        return state;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Necessary for code to run
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseButton = e.getButton();
        numMouseClicks++;

        if (state == WELCOME) {
            numMouseClicks = 0;
            state = PLAYING;
        }
        window.repaint();

        if (state == PLAYING) {
            // All the setup stuff that happens right after the user's first click
            if (numMouseClicks == 1) {
                ArrayList<Tile> startingLand = new ArrayList<>();

                // Checks which tile has been clicked and sets it to land
                for (int i = 0; i < BOARD_WIDTH; i++) {
                    for (int j = 0; j < BOARD_HEIGHT; j++) {
                        if (board[i][j].isClicked(mouseX, mouseY)) {
                            startingLand = generateLand(i, j);
                        }
                    }
                }

                for (Tile tile : startingLand) {
                    tile.setState(Tile.LAND);
                }

                distributeMines();
                for (int i = 0; i < BOARD_WIDTH; i++) {
                    for (int j = 0; j < BOARD_HEIGHT; j++) {
                        // Sets each tile's
                        board[i][j].countNumMines(board);
                    }
                }
            }

            // Main game "loop"
            else {
                // Checks which tile has been clicked and sets it to land
                for (int i = 0; i < BOARD_WIDTH; i++) {
                    for (int j = 0; j < BOARD_HEIGHT; j++) {
                        if (board[i][j].isClicked(mouseX, mouseY)) {
                            // Check if user lost
                            if (mouseButton == MouseEvent.BUTTON1 && isLost(i, j)) {
                                state = LOST;
                                System.out.println("user lost");
                                break;
                            }

                            switch (mouseButton) {
                                // Left click
                                case MouseEvent.BUTTON1:
                                    board[i][j].setState(Tile.LAND);
                                    break;
                                case MouseEvent.BUTTON3:
                                    // If the user clicks one of their own flags, they can remove it
                                    if (board[i][j].getState() == Tile.FLAG)
                                        board[i][j].setState(Tile.EMPTY);

                                        // User can not turn anything into a flag
                                    else if (board[i][j].getState() != Tile.LAND)
                                        board[i][j].setState(Tile.FLAG);
                                    break;

                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Necessary for code to run
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Necessary for code to run
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Necessary for code to run
    }

    public void distributeMines() {
        int boardX;
        int boardY;
        for (int i = 0; i < TOTAL_MINES; i++) {
            // Generate random (x,y) coordinates
            boardX = (int) (Math.random() * BOARD_WIDTH);
            boardY = (int) (Math.random()* BOARD_HEIGHT);

            if (board[boardX][boardY].getIsMine() || board[boardX][boardY].getState() == Tile.LAND) {
                i--;
            } else {
                board[boardX][boardY].setIsMine(true);
            }
        }
    }

    public ArrayList<Tile> generateLand(int startRow, int startCol) {
        ArrayList<Tile> startingLand = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (((startRow + (i-2)) >= 0 && (startRow + (i-2)) < Game.BOARD_WIDTH) && ((startCol + (j-1)) >= 0 && (startCol + (j-1)) < Game.BOARD_HEIGHT)) {
                    startingLand.add(board[startRow + i-2][startCol + j-1]);
                }
            }
        }

        return startingLand;
    }

    public boolean isLost(int boardX, int boardY) {
        if (board[boardX][boardY].getIsMine()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Game g = new Game();
    }
}
