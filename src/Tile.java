import javax.swing.*;
import java.awt.*;

public class Tile {
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

    // Images
    private Image emptyTileImage;
    private Image landTileImage;
    private Image numMinesImage;
    private Image flagImage;
    private Image highlightedTileImage;

    // Window coordinates
    private int tileX;
    private int tileY;

    // Board coordinates
    private int row;
    private int col;

    public Tile(int tileX, int tileY, int row, int col, Image emptyImage, Image landImage) {
        this.tileX = tileX;
        this.tileY = tileY;

        this.row = row;
        this.col = col;

        state = EMPTY;
        isMine = false;
        numMines = 0;

        // Initialize images
        emptyTileImage = emptyImage;
        landTileImage = landImage;
        flagImage = new ImageIcon("Resources/flag.png").getImage();
        highlightedTileImage = new ImageIcon("Resources/highlightedTile.png").getImage();
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

    // Setters
    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setNumMines(int numMines) {
        this.numMines = numMines;

    }

    public void setState(int state) {
        this.state = state;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public boolean isClicked(int mouseX, int mouseY) {
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
        numMinesImage = new ImageIcon("Resources/" + numMines + ".png").getImage();
    }

    public boolean highlight(int mouseX, int mouseY) {
        if ((mouseX > tileX && mouseX < (tileX + TILE_WIDTH)) && (mouseY > tileY && mouseY < (tileY + TILE_WIDTH)))
            return true;
        else
            return false;
    }

    public void draw(Graphics g) {
        Image tileImage = null;
        Image numMinesImage = null;
        if (state == LAND) {
            tileImage = landTileImage;
            numMinesImage = this.numMinesImage;
        }
        else {
            tileImage = emptyTileImage;
            numMinesImage = null;

            if (isHighlighted)
                tileImage = highlightedTileImage;
        }



        g.drawImage(tileImage, tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);
        g.drawImage(numMinesImage, tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);


        // Need to draw flag after others so it doesn't get covered
        if (state == FLAG)
            g.drawImage(flagImage,tileX, tileY, TILE_WIDTH, TILE_WIDTH, window);
    }
}
