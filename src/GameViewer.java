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
    private Image titleImage;
    private Image flagImage;

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
        titleImage = new ImageIcon("Resources/title.png").getImage();
        flagImage = new ImageIcon("Resources/FlagAnimation/flagAnimation10.png").getImage();
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

    // Draws the background when ever the user loses or wins
    public void paintHelper(Graphics g) {
        Tile tile = null;

        g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
        for (int i = 0; i < Game.BOARD_WIDTH; i++) {
            for (int j = 0; j < Game.BOARD_HEIGHT; j++) {
                // Draw tiles
                tile = game.getBoard()[i][j];
                tile.draw(g, game.getState());
            }
        }
        g.drawImage(frameImage, BOARD_X - 2, BOARD_Y - 2, 724, 564, this);

        // Dims the screen
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0,0, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void myPaint(Graphics g) {
        switch(game.getState()) {
            case Game.WELCOME:
                g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
                g.drawImage(titleImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
                for (Button button : game.getButtons()) {
                    button.draw(g);
                }
                break;

            case Game.PLAYING:
                Tile tile = null;
                g.setColor(new Color(74,117,44));
                g.setFont(new Font("Courier New", Font.BOLD, 30));

                g.drawImage(backgroundImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, this);
                g.drawImage(flagImage, 150, 50, 50, 50, this);
                g.drawString(Game.TOTAL_MINES - game.getNumFlags() + "", 200, 90);

                // Draw player's timer
                g.drawString(game.getTime() + "", 500, 80);

                for (int i = 0; i < Game.BOARD_WIDTH; i++) {
                    for (int j = 0; j < Game.BOARD_HEIGHT; j++) {
                        // Draw tiles
                        tile = game.getBoard()[i][j];
                        tile.draw(g, game.getState());
                    }
                }
                g.drawImage(frameImage, BOARD_X - 2, BOARD_Y - 2, 724, 564, this);
                break;

            case Game.LOST:
                // Draws general background
                paintHelper(g);

                g.setColor(new Color(214, 255, 212));
                g.setFont(new Font("Courier New", Font.BOLD, 80));
                g.drawString("Game Over", 300, 380);
                break;

            case Game.WON:
                // Draws general background
                paintHelper(g);

                g.setColor(new Color(214, 255, 212));
                g.setFont(new Font("Courier New", Font.BOLD, 80));
                g.drawString("You Won!", 300, 380);
                break;
        }
    }
}
