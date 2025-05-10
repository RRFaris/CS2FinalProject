import javax.swing.*;
import java.awt.*;

public class Button {
    // Instance Variables

    // Front-end
    private GameViewer window;

    // Button position
    private int buttonX;
    private int buttonY;

    // Button dimensions
    private int buttonWidth;
    private int buttonHeight;

    private String name;

    private boolean isClicked;

    // Images
    private Image image;
    private Image highlightedImage;
    private Image pressedImage;

    private boolean isHighlighted;

    // Constructor
    public Button(int buttonX, int buttonY, int buttonWidth, int buttonHeight, String name) {
        this.buttonX = buttonX;
        this.buttonY = buttonY;

        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        this.name = name;

        image = new ImageIcon("Resources/Buttons/" + name + "ButtonImage.png").getImage();
        highlightedImage = new ImageIcon("Resources/Buttons/highlighted" + name + "ButtonImage.png").getImage();
        pressedImage = new ImageIcon("Resources/Buttons/pressed" + name + "ButtonImage.png").getImage();
    }

    // Getters
    public int getButtonX() {
        return buttonX;
    }

    public int getButtonY() {
        return buttonY;
    }

    public boolean getIsClicked() {
        return isClicked;
    }

    // Setters
    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    // Checks if button has been clicked
    public boolean mouseEntered(int mouseX, int mouseY) {
        if ((mouseX > buttonX && mouseX < (buttonX + buttonWidth)) && (mouseY > buttonY && mouseY < (buttonY + buttonHeight)))
            return true;
        return false;
    }

    public int click() {
        int state = 0;
        switch (name) {
            case "Play":
                state = Game.PLAYING;
                break;
            case "Quit":
                System.exit(0);
                break;
        }
        return state;
    }

    public void draw(Graphics g) {
        Image image = null;

        if (isHighlighted) {
            if (isClicked) {
                image = pressedImage;
            }
            else {
                image = highlightedImage;
            }
        }

        else {
            image = this.image;
        }

        g.drawImage(image, buttonX, buttonY, buttonWidth, buttonHeight, window);
    }
}
