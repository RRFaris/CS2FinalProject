import javax.swing.*;
import java.awt.*;

public class GameViewer extends JFrame {
    private Game game;

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
    }

    public void paint(Graphics g) {
        Tile tile = null;
        if (game.getState() == game.PLAYING) {
            for (int i = 0; i < game.BOARD_WIDTH; i++) {
                for (int j = 0; j < game.BOARD_HEIGHT; j++) {
                    tile = game.getBoard()[i][j];
                    tile.draw(g);
//                    if (tile.getIsHighlighted()) {
//                        g.setColor(Color.yellow);
//                        g.fillRect(tile.getTileX(), tile.getTileY(), Tile.TILE_WIDTH, Tile.TILE_WIDTH);
//                    }
                }
            }
        }

    }
}
