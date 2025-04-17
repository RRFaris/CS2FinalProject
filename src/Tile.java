public class Tile {
    // States of a tile
    private int state;
    private final int EMPTY = 0;
    private final int LAND = 1;
    private final int FLAG = 2;

    private boolean isMine;

    private int numMines;

    public Tile() {
        state = EMPTY;
        isMine = false;
        numMines = 0;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    public
}
