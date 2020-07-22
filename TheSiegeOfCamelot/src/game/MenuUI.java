package game;

import game.hud.Command;
import game.hud.MenuHUD;
import game.onscreen.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Class responsible for displaying menu
 * @author KarpatTech
 */
public class MenuUI extends JPanel {

    private MenuHUD hud;
    private Image background;
    private Image logo;

    /**
     * Constructor. Initializes Menu
     * @param hudState starting menu page index
     * @throws IOException when some resources are missing
     */
    public MenuUI(int hudState) throws IOException {
        background = ImageIO.read(new File("textures/mainmenu.png"));
        logo = ImageIO.read(new File("textures/egy_logo.png"));
        hud = new MenuHUD(hudState);
    }

    /**
     * Called once evry tick. Handles UI interaction
     * @return signal for Game class
     */
    public int tick() {
        Command currCommand = hud.getLastCommand();
        if (currCommand != Command.NONE) {
            switch (currCommand) {
                case PLAY:
                    hud.setState(1); //Level Menu
                    break;
                case TOWERS:
                    hud.setState(2); //Towers
                    break;
                case BESTIARY:
                    hud.setState(3); //Bestiary
                    break;
                case OPTIONS:
                    hud.setState(4); //Options
                    break;
                case BACK:
                    hud.setState(0); //Main Menu
                    break;
                case RESOLUTION:
                    if (Game.getScale() == 2) {
                        Game.setScale(3);
                        hud.getUIElementFromOptions(0).setText("Set HD");
                    } else {
                        Game.setScale(2);
                        hud.getUIElementFromOptions(0).setText("Set FullHD");
                    }
                    this.setPreferredSize(new Dimension(Game.getBaseWidth() * Game.getScale(), Game.getBaseHeight() * Game.getScale()));
                    return -1;
                case LEVEL1:
                    return 1;
                case LEVEL2:
                    return 2;
                case LEVEL3:
                    return 3;
                case LEVEL4:
                    return 4;
                case LEVEL5:
                    return 5;
                case QUIT:
                    System.exit(0);
                    break;
            }
        }
        return 0;
    }

    /**
     * Forwards click from Game class to HUD
     * @param pos position of click
     */
    public void clicked(Position pos) {
        hud.onClick(pos);
    }

    /**
     * Redraws the Menu every tick
     * @param grphcs parameter for drawing
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        if (GameTimer.getTickNum() > 48) {
            g2.drawImage(background, 0 ,0,
                    background.getWidth(null) * Game.getScale(),
                    background.getHeight(null) * Game.getScale(),
                    null);
            hud.draw(g2);
        } else {
            g2.drawImage(logo, 0, 0,
                    logo.getWidth(null) * Game.getScale(),
                    logo.getHeight(null) * Game.getScale(),
                    null);
        }
    }
}
