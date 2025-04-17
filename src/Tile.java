import java.awt.*;

public class Tile {
    // States of a tile
    private int state;
    private final int EMPTY = 0;
    private final int LAND = 1;
    private final int FLAG = 2;

    private boolean isMine;

    public static final int TILE_WIDTH = 40;

    private int numMines;

    // Coordinates
    private int tileX;
    private int tileY;

    public Tile(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;

        state = EMPTY;
        isMine = false;
        numMines = 0;
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

    // Setters

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setNumMines(int numMines) {
        this.numMines = numMines;
    }

    public boolean isClicked(int mouseX, int mouseY) {
        if ((mouseX > tileX && mouseX < (tileX + TILE_WIDTH)) && (mouseY > tileY && mouseY < (tileY + TILE_WIDTH)))
            return true;
        return false;
    }

    public void draw(Graphics g) {
        g.drawRect(tileX, tileY, TILE_WIDTH, TILE_WIDTH);
    }
}
