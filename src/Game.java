import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game implements MouseListener, MouseMotionListener, ActionListener {
    private Tile[][] board;
    private final int TOTAL_MINES = 40;
    private final int TOTAL_TILES = 252;

    public static final int BOARD_WIDTH = 18;
    public static final int BOARD_HEIGHT = 14;

    // Tile images
    private Image emptyTileImage;
    private Image landTileImage;

    // Buttons
    private Button[] buttons;

    // Game states
    private int state;
    public final static int WELCOME = 0;
    public final static int PLAYING = 1;
    public final static int LOST = 2;
    public final static int WON = 3;

    // Front-end
    GameViewer window;

    // Mouse stuff
    int mouseX;
    int mouseY;
    int mouseButton;

    // Use to set up the mines after user's first click
    int numMouseClicks;
    int numLand;

    public Game() {
        window = new GameViewer(this);

        // Initialize game state
        state = WELCOME;

        // Initialize board
        board = new Tile[BOARD_WIDTH][BOARD_HEIGHT];

        // Initialize buttons
        buttons = new Button[]{
                  new Button(375, 200, 180, 90, "Play"),
                  new Button(375, 350, 225, 90, "Options"),
                  new Button(375, 500, 165, 90, "Quit")
                  };

        initializeTileImages();

        // Initialize how much land there is
        numLand = 0;

        // Add mouse and mouse motion listeners
        window.addMouseListener(this);
        window.addMouseMotionListener(this);
    }

    public Tile[][] getBoard() {
        return board;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public int getState() {
        return state;
    }

    public void initializeTileImages() {
        // Initialize images
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                // Make color alternate between tiles
                if ((i + j) % 2 == 0) {
                    emptyTileImage = new ImageIcon("Resources/Tiles/emptyLightTile.png").getImage();
                    landTileImage = new ImageIcon("Resources/Tiles/landLightTile.png").getImage();
                }
                else {
                    emptyTileImage = new ImageIcon("Resources/Tiles/emptyDarkTile.png").getImage();
                    landTileImage = new ImageIcon("Resources/Tiles/landDarkTile.png").getImage();
                }
                board[i][j] = new Tile(window.BOARD_X + (i*Tile.TILE_WIDTH), window.BOARD_Y + (j*Tile.TILE_WIDTH), i, j, emptyTileImage, landTileImage);
            }
        }
    }

    /// MOUSE Listener STUFF
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

        switch(state) {
            case WELCOME:
                for (Button button : buttons) {
                    if (button.mouseEntered(mouseX, mouseY)) {
                        button.setIsClicked(true);
                    }
                }

                numMouseClicks = 0;
                window.repaint();
                break;

            case PLAYING:
                // All the setup stuff that happens right after the user's first click
                if (numMouseClicks == 1) {
                    firstClick(mouseX, mouseY);
                }

                // Main game "loop"
                else {
                    gameLoop(mouseX, mouseY);
                }
                break;

        }
        window.repaint();
    }


    /// MOUSE MOTION STUFF
    @Override
    public void mouseReleased(MouseEvent e) {
        switch(state) {
            case WELCOME:
                for (Button button : buttons) {
                    if (!button.mouseEntered(mouseX, mouseY)) {
                        button.setIsClicked(false);
                    }
                }

                numMouseClicks = 0;
                window.repaint();
                break;
        }
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

        switch (state) {
            case WELCOME:
                for (Button button : buttons) {
                    if (button.mouseEntered(mouseX, mouseY))
                        button.setHighlighted(true);
                    else
                        button.setHighlighted(false);
                }
        }

        Tile tile = null;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                tile = board[i][j];
                if (tile.mouseEntered(mouseX, mouseY))
                    tile.setHighlighted(true);
                else
                    tile.setHighlighted(false);
            }
        }
        window.repaint();
    }


    /// ACTION LISTENER STUFF
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    // What happens after user's first click
    public void firstClick(int mouseX, int mouseY) {
        Tile startingTile = null;

        // Checks which tile has been clicked and sets it to land
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j].mouseEntered(mouseX, mouseY)) {
                    numLand++;
                    System.out.println(numLand);
                    startingTile = board[i][j];
                    startingTile.setState(Tile.LAND);
                }
            }
        }

        // Distributes mines after first click
        distributeMines(startingTile.getRow(), startingTile.getCol());

        // Sets each tile's number of mines
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j].countNumMines(board);
            }
        }

        // Spawns in user's starting area
        fillEmptyTiles(startingTile);
        System.out.println(numLand);
    }

    // Main game "loop"
    public void gameLoop(int mouseX, int mouseY) {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                // Find the tile that has been clicked
                if (board[i][j].mouseEntered(mouseX, mouseY)) {
                    // Check if user lost
                    if (mouseButton == MouseEvent.BUTTON1 && isLost(i, j)) {
                        state = LOST;
                        System.out.println("User Lost");
                        break;
                    }
                    // Check if user won
//                    if (isWin()) {
//                        state = WON;
//                        System.out.println("User Won");
//                        break;
//                    }

                    // Determine which mouse button the user pressed
                    switch (mouseButton) {
                        // Left click
                        case MouseEvent.BUTTON1:
                            board[i][j].setState(Tile.LAND);
                            numLand++;
                            System.out.println(numLand);
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

    // No mines should be able to generate in a 3 by 3 square around user's starting tile
    public boolean isSafeZone(int mineX, int mineY, int startX, int startY) {
        if ((mineX >= startX - 1 && mineX <= startX + 1) && (mineY >= startY - 1 && mineY <= startY + 1))
            return true;
        return false;
    }

    // Randomly distributes 40 mines on the board
    public void distributeMines(int startX, int startY) {
        int mineX;
        int mineY;
        for (int i = 0; i < TOTAL_MINES; i++) {
            // Generate random (x,y) coordinates
            mineX = (int) (Math.random() * BOARD_WIDTH);
            mineY = (int) (Math.random() * BOARD_HEIGHT);

            // A mine can't spawn on another mine, a piece of land, or in a 3 by 3 grid around the player's first click
            if (board[mineX][mineY].getIsMine() || board[mineX][mineY].getState() == Tile.LAND || isSafeZone(mineX, mineY, startX, startY)) {
                i--;
            }
            else
                board[mineX][mineY].setIsMine(true);
        }
    }

    // Checks if the user has lost
    public boolean isLost(int boardX, int boardY) {
        if (board[boardX][boardY].getIsMine()) {
            return true;
        }
        return false;
    }

    // Checks if the user has won
    public boolean isWin() {
        if (numLand == TOTAL_TILES - TOTAL_MINES)
            return true;
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
                        else if (board[newI][newJ].getState() != Tile.LAND) {
                            // Sets all tiles around central tile to land
                            board[newI][newJ].setState(Tile.LAND);
                            numLand++;
                        }
                    }
                }
            }

            tilesToExplore.remove(tile);
            tile.setState(Tile.LAND);
            numLand++;

            if (!tilesToExplore.isEmpty()) {
                tile = tilesToExplore.getFirst();
            }
        }
    }

    // Main
    public static void main(String[] args) {
        Game g = new Game();
    }
}
