package game.hud;

import game.Game;
import game.onscreen.OnScreenElement;
import game.onscreen.Position;

import java.awt.*;

/**
 * Universal class for every UI element (Image, text, button, etc...)
 * @author KarpatTech
 */
public class UIElement extends OnScreenElement {

    private boolean clickable;
    private boolean isActive;

    private int height;
    private int width;

    private String text;
    private Font font;
    private Color color;

    /**
     * Action, when clicked. Command.NONE for no action
     */
    private Command command;

    /**
     * Constructor width height and width (used for rectengular buttons)
     * @param command action, when clicked
     * @param pos position
     * @param height hitbox height
     * @param width hitbox width
     */
    public UIElement(Command command, Position pos, int height, int width) {
        this.command = command;
        super.pos = pos;
        this.height = height;
        this.width = width;
        isActive = true;
        clickable = true;
        text = null;
    }

    /**
     * Constructor for non rectengular buttons
     * @param command action, when clicked
     * @param pos position
     */
    public UIElement(Command command, Position pos) {
        this(command, pos, 0, 0);
    }

    /**
     * Sets text and font
     * @param text the text
     * @param fontSize font size
     * @param font font name
     * @param color font color
     */
    public void setText(String text, int fontSize, String font, Color color) {
        this.text = text;
        this.font = new Font(font, Font.BOLD, fontSize);
        this.color = color;
    }

    /**
     * Sets text only
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Function for basic clicking (can be overwritten)
     * @param clickPos position of click
     * @return true is clickPos is in hitbox
     */
    public boolean isClicked(Position clickPos) {
        return Math.abs(pos.x - clickPos.x) < width / 2 && Math.abs(pos.y - clickPos.y) < height / 2;
    }

    public Command getCommand() {
        return command;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Redraws itself in every tick
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (text != null) {
            int scale = Game.getScale();
            g2.setColor(color);
            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics(font);
            int x = (int) (pos.x * scale - metrics.stringWidth(text) / 2);
            int y = (int) (pos.y * scale - metrics.getHeight() / 2 + metrics.getAscent());
            g2.drawString(text, x, y);
        }
    }
}
