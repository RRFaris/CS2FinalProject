import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tile {
    // Flag animation
    private static final int MAX_FLAG_INDEX = 9;
    private static final int NUM_FLAG_FRAMES = 10;

    // States of a tile
    private int state;
    public static final int EMPTY = 0;
    public static final int LAND = 1;
    public static final int FLAG = 2;

    public static final int TILE_WIDTH = 40;

    private boolean isMine;
    private boolean isHighlighted;

    // How many mines surround the tile
    private int numMines;

    private GameViewer window;

    // Use to fill in adjacent empty tiles
    private boolean isExplored;

    // Images
    private Image emptyTileImage;
    private Image landTileImage;
    private Image numMinesImage;
    private Image highlightedTileImage;
    private Image mineImage;
    private Image unecessaryFlagImage;
    private Image[] flagImage;


    // Window coordinates
    private int tileX;
    private int tileY;

    // Board coordinates
    private int row;
    private int col;

    // Flag Animation
    int frameCounter;

    public Tile(int tileX, int tileY, int row, int col, Image emptyImage, Image landImage, Image mineImage) {
        this.tileX = tileX;
        this.tileY = tileY;

        this.row = row;
        this.col = col;

        state = EMPTY;
        isMine = false;
        numMines = 0;

        frameCounter = 0;

        // Initialize images
        emptyTileImage = emptyImage;
        landTileImage = landImage;
        highlightedTileImage = new ImageIcon("Resources/Tiles/highlightedTile.png").getImage();
        unecessaryFlagImage = new ImageIcon("Resources/flagUnnecessary.png").getImage();
        this.mineImage = mineImage;

        // Initialize flag animation images
        flagImage = new Image[NUM_FLAG_FRAMES];
        for (int i = 0; i < NUM_FLAG_FRAMES; i++) {
            flagImage[i] = new ImageIcon("Resources/FlagAnimation/flagAnimation" + (i+1) + ".png").getImage();
        }
    }

    // Getters
    public boolean getIsMine() {
        return isMine;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public int getState() {
        return state;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getNumMines() {
        return numMines;
    }

    public boolean getIsHighlighted() {
        return isHighlighted;
    }

    public boolean getIsExplored() {
        return isExplored;
    }

    // Setters
    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setIsExplored(boolean isExplored) {
        this.isExplored = isExplored;
    }

    public void setNumMines(int numMines) {
        this.numMines = numMines;

    }

    public void setState(int state) {
        this.state = state;
        frameCounter = 0;

    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public boolean mouseEntered(int mouseX, int mouseY) {
        if ((mouseX > tileX && mouseX < (tileX + TILE_WIDTH)) && (mouseY > tileY && mouseY < (tileY + TILE_WIDTH)))
            return true;
        return false;
    }

    // Looks at surrounding tiles to count the mines
    public void countNumMines(Tile[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Checks if any coordinates are out of bounds
                if (((row + (i-1)) >= 0 && (row + (i-1)) < Game.BOARD_WIDTH) && ((col + (j-1)) >= 0 && (col + (j-1)) < Game.BOARD_HEIGHT)) {
                    if (board[row+i-1][col+j-1].isMine)
                        numMines++;
                }
            }
        }
        if (numMines == 0)
            return;
        numMinesImage = new ImageIcon("Resources/Numbers/" + numMines + ".png").getImage();
    }

    public void draw(Graphics g, int state) {
        Image tileImage = null;
        Image numMinesImage = null;
        Image mineImage = null;

        // Sets correct images for tile
        if (isMine && this.state != FLAG && state == Game.LOST) {
            mineImage = this.mineImage;
        }

        if (this.state == LAND) {
            tileImage = landTileImage;
            numMinesImage = this.numMinesImage;
        }
        else {
            tileImage = emptyTileImage;
            numMinesImage = null;

            if (isHighlighted)
                tileImage = highlightedTileImage;
        }

        // Draws tile
        g.drawImage(tileImage, tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);
        g.drawImage(numMinesImage, tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);
        g.drawImage(mineImage, tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);

        // Need to draw flag after others so it doesn't get covered
        if (this.state == FLAG) {
            if (state == Game.LOST && !isMine) {
                g.drawImage(unecessaryFlagImage, tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);
            }
            else {
                g.drawImage(flagImage[frameCounter], tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);
                frameCounter = Math.min(frameCounter + 1, MAX_FLAG_INDEX);
            }
        }
    }
}
