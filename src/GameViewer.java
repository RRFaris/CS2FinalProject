import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameViewer extends JFrame {
    private Game game;

    private JButton startButton;

    public final int WINDOW_HEIGHT = 800;
    public final int WINDOW_WIDTH = 1000;

    public final int BOARD_X = 100;
    public final int BOARD_Y = 100;

    public GameViewer(Game game) {
        this.game = game;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("Minesweeper");
        this.setVisible(true);

        // Double buffering stuff
        createBufferStrategy(2);

        // Buttons
        startButton = new JButton();
    }

    // Double buffer
    public void paint(Graphics g) {
        BufferStrategy bf = this.getBufferStrategy();
        if (bf == null)
            return;
        Graphics g2 = null;
        try {
            g2 = bf.getDrawGraphics();
            myPaint(g2);
        }
        finally {
            g2.dispose();
        }
        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }

    public void myPaint(Graphics g) {
        Tile tile = null;
        if (game.getState() == game.PLAYING) {
            for (int i = 0; i < game.BOARD_WIDTH; i++) {
                for (int j = 0; j < game.BOARD_HEIGHT; j++) {
                    tile = game.getBoard()[i][j];
                    tile.draw(g);

                    // Debugging purposes
                    if (game.getState() == game.LOST && tile.getIsMine()) {
                        g.setColor(Color.red);
                        g.fillRect(tile.getTileX(), tile.getTileY(), Tile.TILE_WIDTH, Tile.TILE_WIDTH);
                    }
                }
            }
        }

    }
}
