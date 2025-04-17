public class Game {
    private Tile[][] board;
    private int totalMines;

    // Game states
    private int state;
    public final int WELCOME = 0;
    public final int PLAYING = 1;

    GameViewer window;

    int mouseX;
    int mouseY;

    public Game() {
        window = new GameViewer(this);
    }


    public static void main(String[] args) {
        Game g = new Game();
    }
}
