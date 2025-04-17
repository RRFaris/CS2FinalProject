import javax.swing.*;
import java.awt.*;

public class GameViewer extends JFrame {
    private Game game;

    private final int WINDOW_HEIGHT = 800;
    private final int WINDOW_WIDTH = 1000;

    public GameViewer(Game game) {
        this.game = game;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("Minesweeper");
        this.setVisible(true);
    }
}
