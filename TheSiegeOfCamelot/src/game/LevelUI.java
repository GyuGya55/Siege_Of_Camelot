package game;

import game.hud.Command;
import game.hud.LevelHUD;
import game.onscreen.Position;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Class responsible for displaying level
 * @author KarpatTech
 */
public class LevelUI extends JPanel {

    private Level level;
    private int levelNum;
    private LevelHUD hud;

    private Image background;

    /**
     * Constructor. Initializes level
     * @param levelNum number of level needs to be initialized
     * @throws IOException when some resources are missing
     */
    public LevelUI(int levelNum) throws IOException {
        level = new Level("levels/" + levelNum + ".txt");
        this.levelNum = levelNum;

        background = ImageIO.read(new File("textures/level_" + levelNum + ".png"));

        hud = new LevelHUD(level.getHp(), level.getCoins());
    }

    /**
     * Called once evry tick. Handles UI interaction
     * @return signal for Game class
     */
    public int tick() throws IOException {
        Command currCommand = hud.getLastCommand();
        if (currCommand != Command.NONE) {
            switch (currCommand) {
                case NEXTWAVE:
                    level.startNextWave();
                    hud.getUIElement(4).setActive(false); // Next wave button
                    break;
                case PAUSE:
                    hud.setState(1);
                    Game.pause();
                    break;
                case RESUME:
                    hud.setState(0);
                    Game.resume();
                    break;
                case RETRY:
                    hud.setState(0);
                    level = new Level("levels/" + levelNum + ".txt");
                    hud = new LevelHUD(level.getHp(), level.getCoins());
                    Game.resume();
                    break;
                case NEXTLEVEL:
                    if (levelNum == 5) {
                        Game.resume();
                        return 1;
                    }
                    hud.setState(0);
                    ++levelNum;
                    level = new Level("levels/" + levelNum + ".txt");
                    background = ImageIO.read(new File("textures/level_" + levelNum + ".png"));
                    hud = new LevelHUD(level.getHp(), level.getCoins());
                    Game.resume();
                    break;
                case BACK:
                    Game.resume();
                    return 1;
            }
        }

        if (!Game.isGamePaused()) {
            int signal = level.tick();
            if (signal == 1) { // Need to refresh HUD signal
                hud.getUIElement(2).setText(Integer.toString(level.getHp())); // HP text
                hud.getUIElement(3).setText(Integer.toString(level.getCoins())); // Coins text
            } else if (signal == 2) { // Activate next wave button signal
                hud.getUIElement(4).setActive(true); // Next wave button
            } else if (signal == 3) { // Game over signal
                hud.setState(2);
                Game.pause();
            } else if (signal == 4) { // Victory signal
                hud.setState(3);
                Game.pause();
            }
        }
        return 0;
    }

    /**
     * Forwards click from Game class to HUD and to Level
     * @param pos position of click
     */
    public void clicked(Position pos) {
        if (!hud.onClick(pos)) {
            level.clicked(pos);
        }
    }

    /**
     * Redraws the Menu every tick
     * @param grphcs parameter for drawing
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.drawImage(background, 0 ,0,
                background.getWidth(null) * Game.getScale(),
                background.getHeight(null) * Game.getScale(),
                null);

        level.draw(g2);
        hud.draw(g2);
    }
}
