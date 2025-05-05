import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameViewer extends JFrame {
    private Game game;

    public final int WINDOW_HEIGHT = 800;
    public final int WINDOW_WIDTH = 1000;

    public final int BOARD_X = 140;
    public final int BOARD_Y = 120;

    private Image backgroundImage;
    private Image frameImage;

    public GameViewer(Game game) {
        this.game = game;

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setTitle("Minesweeper");
        this.setVisible(true);

        // Double buffering stuff
        createBufferStrategy(2);

        backgroundImage = new ImageIcon("Resources/backgroundColor.png").getImage();
        frameImage = new ImageIcon("Resources/frame.png").getImage();
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
        switch(game.getState()) {
            case Game.WELCOME:
                g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
                for (Button button : game.getButtons()) {
                    button.draw(g);
                }
                break;

            case Game.PLAYING:
                Tile tile = null;
                g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);

                for (int i = 0; i < Game.BOARD_WIDTH; i++) {
                    for (int j = 0; j < Game.BOARD_HEIGHT; j++) {
                        tile = game.getBoard()[i][j];
                        tile.draw(g);
                    }
                }
                g.drawImage(frameImage, BOARD_X - 2, BOARD_Y - 2, 724, 564, this);
                break;

            case Game.LOST:
                for (int i = 0; i < Game.BOARD_WIDTH; i++) {
                    for (int j = 0; j < Game.BOARD_HEIGHT; j++) {
                        if (game.getBoard()[i][j].getIsMine()) {
                            g.setColor(Color.red);
                            g.fillRect(game.getBoard()[i][j].getTileX(), game.getBoard()[i][j].getTileY(), Tile.TILE_WIDTH, Tile.TILE_WIDTH);
                        }
                    }
                }
                break;
        }
    }
}
