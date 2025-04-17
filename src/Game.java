import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game implements MouseListener {
    private Tile[][] board;
    private int totalMines;

    public final int BOARD_WIDTH = 18;
    public final int BOARD_HEIGHT = 14;

    // Game states
    private int state;
    public final int WELCOME = 0;
    public final int PLAYING = 1;

    GameViewer window;

    int mouseX;
    int mouseY;

    public Game() {
        window = new GameViewer(this);

        // Initialize game state
        state = WELCOME;

        // Initialize board
        board = new Tile[BOARD_WIDTH][BOARD_HEIGHT];

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = new Tile(window.BOARD_X + (i*Tile.TILE_WIDTH), window.BOARD_Y + (j*Tile.TILE_WIDTH));
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
        mouseX = e.getX();
        mouseY = e.getY();
        state = PLAYING;
        window.repaint();

        for (int i = 0; i <BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board[i][j].isClicked(mouseX, mouseY)) {
                    board[i][j].setIsMine(true);
                }
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Necessary for code to run
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

    public static void main(String[] args) {
        Game g = new Game();
    }
}
