import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Game implements MouseListener, MouseMotionListener {
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
        window.addMouseMotionListener(this);
    }

    public Tile[][] getBoard() {
        return board;
    }

    public int getState() {
        return state;
    }


    /// MOUSE STUFF
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

            Tile startingTile = null;

            if (numMouseClicks == 1) {
                // Checks which tile has been clicked and sets it to land
                for (int i = 0; i < BOARD_WIDTH; i++) {
                    for (int j = 0; j < BOARD_HEIGHT; j++) {
                        if (board[i][j].isClicked(mouseX, mouseY)) {
                            startingTile = board[i][j];
                            startingTile.setState(Tile.LAND);
                        }
                    }
                }

                // Distributes mines after first click
                distributeMines();

                // Spawns in user's starting area
                fillEmptyTiles(startingTile);

                for (int i = 0; i < BOARD_WIDTH; i++) {
                    for (int j = 0; j < BOARD_HEIGHT; j++) {
                        // Sets each tile's number of mines
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

                            // Determine which mouse button the user pressed
                            switch (mouseButton) {
                                // Left click
                                case MouseEvent.BUTTON1:
                                    board[i][j].setState(Tile.LAND);
                                    if (board[i][j].getNumMines() == 0) {
                                        fillEmptyTiles(board[i][j]);
                                    }
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


    /// MOUSE MOTION STUFF
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

    @Override
    public void mouseDragged(MouseEvent e) {
        // Necessary for code to run
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j].highlight(mouseX, mouseY)) {
                    board[i][j].setHighlighted(true);
                }
                else
                    board[i][j].setHighlighted(false);
            }
        }
        window.repaint();
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

    // Fill in adjacent tiles with 0 mines
    public void fillEmptyTiles(Tile tile) {
        ArrayList<Tile> tilesToExplore = new ArrayList<>();
        int row;
        int col;
        int newI;
        int newJ;

        tilesToExplore.add(tile);

        while (!tilesToExplore.isEmpty()) {
            // Go to every tile around the tile
            row = tile.getRow();
            col = tile.getCol();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    newI = row + i - 1;
                    newJ = col + j - 1;

                    // Checks for out of bounds
                    if ((newI >= 0 && newI < Game.BOARD_WIDTH) && (newJ >= 0 && newJ < Game.BOARD_HEIGHT)) {
                        if (board[newI][newJ].getNumMines() == 0 && board[newI][newJ].getState() != Tile.LAND)
                            tilesToExplore.add(board[newI][newJ]);
                        else {
                            // Sets all tiles around central to land
                            board[newI][newJ].setState(Tile.LAND);
                        }
                    }
                }
            }

            tilesToExplore.remove(tile);
            tile.setState(Tile.LAND);

            if (!tilesToExplore.isEmpty()) {
                tile = tilesToExplore.getFirst();
            }
        }
    }

    public static void main(String[] args) {
        Game g = new Game();
    }
}
