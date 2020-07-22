package game.hud;

import game.Game;
import game.onscreen.OnScreenElement;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * HUD class for LevelUI
 * @author KarpatTech
 */
public class LevelHUD extends HUD {
    /**
     * Represents the state of the game in HUD
     */
    // 0 - playing, 1 - pause, 2 - game over, 3 -victory
    private int state = 0;

    /**
     * Constructor. Builds LevelHUD
     * @param startHp HP to be displayed at the beginning of the level
     * @param startCoins Coins HP to be displayed at the beginning of the level
     * @throws IOException when some resources are missing
     */
    public LevelHUD(int startHp, int startCoins) throws IOException {
        super.Elements = new ArrayList<>();
        super.lastCommand = Command.NONE;

        // 0
        UIElement hp = new UIElement(Command.NONE, new Position(30, 30));
        hp.setDrawComponent(new SingleSprite("hp.png"));
        Elements.add(hp);
        // 1
        UIElement coin = new UIElement(Command.NONE, new Position(30, 90));
        coin.setDrawComponent(new SingleSprite("coin.png"));
        Elements.add(coin);
        // 2
        UIElement hpText = new UIElement(Command.NONE, new Position(80, 30), 50, 100);
        hpText.setDrawComponent(new SingleSprite("empty.png"));
        hpText.setText(Integer.toString(startHp), 30 * Game.getScale(), "TimesRoman", Color.WHITE);
        Elements.add(hpText);
        // 3
        UIElement coinText = new UIElement(Command.NONE, new Position(80, 90), 50, 100);
        coinText.setDrawComponent(new SingleSprite("empty.png"));
        coinText.setText(Integer.toString(startCoins), 30 * Game.getScale(), "TimesRoman", Color.WHITE);
        Elements.add(coinText);
        // 4
        UIElement nextWave = new UIElement(Command.NEXTWAVE, new Position(320, 30), 50, 150);
        nextWave.setDrawComponent(new SingleSprite("menubutton.png"));
        nextWave.setText("Start next wave", 20 * Game.getScale(), "TimesRoman", Color.BLACK);
        Elements.add(nextWave);
        // 5
        UIElement pause = new UIElement(Command.PAUSE, new Position(610, 30), 50, 50);
        pause.setDrawComponent(new SingleSprite("pause.png"));
        Elements.add(pause);
        // 6
        UIElement menuBG = new UIElement(Command.NONE, new Position(320, 180));
        menuBG.setDrawComponent(new SingleSprite("inlevelmenuBG.png"));
        Elements.add(menuBG);
        // 7
        UIElement menuTitle = new UIElement(Command.NONE, new Position(320, 110));
        menuTitle.setDrawComponent(new SingleSprite("empty.png"));
        menuTitle.setText("Title", 30 * Game.getScale(), "TimesRoman", Color.BLACK);
        Elements.add(menuTitle);
        // 8
        UIElement backToMenu = new UIElement(Command.BACK, new Position(320, 230), 50, 150);
        backToMenu.setDrawComponent(new SingleSprite("menubutton.png"));
        backToMenu.setText("Back to Menu", 20 * Game.getScale(), "TimesRoman", Color.BLACK);
        Elements.add(backToMenu);
        // 9
        UIElement resume = new UIElement(Command.RESUME, new Position(320, 160), 50, 150);
        resume.setDrawComponent(new SingleSprite("menubutton.png"));
        resume.setText("Resume", 20 * Game.getScale(), "TimesRoman", Color.BLACK);
        Elements.add(resume);
        // 10
        UIElement retry = new UIElement(Command.RETRY, new Position(320, 160), 50, 150);
        retry.setDrawComponent(new SingleSprite("menubutton.png"));
        retry.setText("Retry", 20 * Game.getScale(), "TimesRoman", Color.BLACK);
        Elements.add(retry);
        // 11
        UIElement nextLevel = new UIElement(Command.NEXTLEVEL, new Position(320, 160), 50, 150);
        nextLevel.setDrawComponent(new SingleSprite("menubutton.png"));
        nextLevel.setText("Next level", 20 * Game.getScale(), "TimesRoman", Color.BLACK);
        Elements.add(nextLevel);

        setState(0);
    }

    public UIElement getUIElement(int ind) {
        return Elements.get(ind);
    }

    /**
     * Sets state and UI elements according to that
     * @param state new state
     */
    public void setState(int state) {
        this.state = state;
        if (state == 0) {
            for (int i = 0; i < 6; ++i) {
                Elements.get(i).setClickable(true);
            }
            for (int i = 6; i < 12; ++i) {
                Elements.get(i).setActive(false);
            }
        } else {
            for (int i = 0; i < 6; ++i) {
                Elements.get(i).setClickable(false);
            }
            // set menu title
            switch (state) {
                case 1:
                    Elements.get(7).setText("Paused");
                    break;
                case 2:
                    Elements.get(7).setText("Game over!");
                    break;
                case 3:
                    Elements.get(7).setText("!!Victory!!");
                    break;
            }
            Elements.get(6).setActive(true); // MenuBG
            Elements.get(7).setActive(true); // MenuTitle
            Elements.get(8).setActive(true); // BackToMenu
            Elements.get(8 + state).setActive(true); // Action
        }
    }

    /**
     * Redraws the active UI elements in every tick
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        for(UIElement elem : super.Elements){
            if(elem.isActive()) {
                elem.draw(g2);
            }
        }
    }

}
