package game.hud;

import game.Game;
import game.onscreen.Position;
import game.onscreen.draw.DrawComponent;
import game.onscreen.draw.SingleSprite;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * HUD class for MenuUI
 * @author KarpatTech
 */
public class MenuHUD extends HUD {

    private ArrayList<UIElement> mainMenuElements;
    private ArrayList<UIElement> levelMenuElements;
    private ArrayList<UIElement> bestiaryElements;
    private ArrayList<UIElement> towerElements;
    private ArrayList<UIElement> optionElements;

    /**
     * Represents the active Menu page
     */
    //0 - main, 1 - level, 2 - tower, 3 - bestiary, 4 - options
    private int state;

    /**
     * The index of the currently active sheet in Towers or Bestiary
     */
    private int currActive = 0;

    /**
     * Constructor. Builds MenuHUD
     * @param state starting menu page's index
     * @throws IOException when some resources are missing
     */
    public MenuHUD(int state) throws IOException {
        super.Elements = new ArrayList<>();
        mainMenuElements = new ArrayList<>();
        levelMenuElements = new ArrayList<>();
        bestiaryElements = new ArrayList<>();
        towerElements = new ArrayList<>();
        optionElements = new ArrayList<>();
        super.lastCommand = Command.NONE;
        setState(state);


        //Main Menu
        String texts[] = { "Play", "Towers", "Bestiary", "Options", "Quit" };
        int Ys[] = { 30, 90, 150, 210, 320 };
        Command commands[] = { Command.PLAY, Command.TOWERS, Command.BESTIARY, Command.OPTIONS, Command.QUIT };
        DrawComponent drawComponent = new SingleSprite("menubutton.png");
        for (int i = 0; i < texts.length; ++i) {
            UIElement ui = new UIElement(commands[i], new Position(120, Ys[i]), 50, 150);
            ui.setText(texts[i], 30 * Game.getScale(), "TimesRoman", Color.BLACK);
            ui.setDrawComponent(drawComponent);
            mainMenuElements.add(ui);
        }


        //Level Menu
        String textsLevel[] = { "Level 01", "Level 02", "Level 03", "Level 04", "Level 05", "Back" };
        int YsLevel[] = { 30, 90, 150, 210, 270, 330 };
        Command commandsLevel[] = { Command.LEVEL1, Command.LEVEL2, Command.LEVEL3, Command.LEVEL4, Command.LEVEL5, Command.BACK };
        for (int i = 0; i < textsLevel.length; ++i) {
            UIElement ui = new UIElement(commandsLevel[i], new Position(120, YsLevel[i]), 50, 150);
            ui.setText(textsLevel[i], 30 * Game.getScale(), "TimesRoman", Color.BLACK);
            ui.setDrawComponent(drawComponent);
            levelMenuElements.add(ui);
        }


        //Towers
        int YsTowers[] = { 30, 90, 150, 210, 270 };
        int XsTowers[] = { 60, 120, 180 };
        String[][] spriteNames = {
                { "archery1", "archery2", "archery3", "crossbows", "ballist" },
                { "barracks1", "barracks2", "barracks3", "assassins", "knights" },
                { "cannons1", "cannons2", "cannons3", "mortar", "trebuchet" }
        };
        for (int i = 0; i < XsTowers.length; ++i) {
            for (int j = 0; j < YsTowers.length; ++j) {
                UIElement ui = new UIElement(Command.NONE, new Position(XsTowers[i], YsTowers[j]), 50, 50);
                ui.setDrawComponent(new SingleSprite("wiki_" + spriteNames[i][j] + ".png"));
                towerElements.add(ui);
            }
        }
        UIElement back = new UIElement(Command.BACK, new Position(120, 330), 50, 150);
        back.setText("Back", 30 * Game.getScale(), "TimesRoman", Color.BLACK);
        back.setDrawComponent(drawComponent);
        towerElements.add(back);
        { // BG
            UIElement bg = new UIElement(Command.NONE, new Position(420, 180));
            bg.setDrawComponent(new SingleSprite("towersBG.png"));
            towerElements.add(bg);
        }
        // Towers common
        String[][] towersNames = {
                { "Archery 1", "Archery 2", "Archery 3", "Crossbows", "Ballist" },
                { "Barracks 1", "Barracks 2", "Barracks 3", "Assassins", "Knights" },
                { "Cannons 1", "Cannons 2", "Cannons 3", "Mortar", "Trebuchet" }
        };
        Position towersPos = new Position(335, 120);
        String[][] towersGet = {
                { "100 C", "20 XP", "50 XP", "250 C", "350 C" },
                { "120 C", "25 XP", "69 XP", "420 C", "300 C" },
                { "150 C", "50 XP", "100 XP", "500 C", "600 C"}
        };
        String[][] towersRanges = {
                { "100", "100", "100", "100", "150" },
                { "100", "100", "100", "150", "100" },
                { "100", "100", "100", "150"}
        };
        String[][] towersDamages = {
                { "3", "4", "5", "15", "10" },
                { "3", "5", "7", "15", "10" },
                { "15", "20", "25", "35"}
        };
        String[][] towersDelays = {
                { "36", "36", "36", "48", "72" },
                { "30", "30", "30", "12", "40" },
                { "100", "100", "100", "200", "300" }
        };
        String[][] towersDMGsecs = {
                { "4.0", "5.3", "6.7", "15.0", "3.3" },
                { "7.2", "12.0", "16.8", "60.0", "18.0" },
                { "3.6", "4.8", "6.0", "4.2"}
        };
        String[][] towersDescrs = {
                {
                    "Archers are your rapid weapons. They are especially good against goblins. Moreover one archery has two archers, what means double power.",
                    "Crossbows is a specialized archery. They shoot more seldom, but their damage is higher. Their ability is to slow down any enemy by 25%.",
                    "Another specialized archery. And yeah, this is kinda OP. You've got just a single archer, but those arrows are unstoppable, and they can deal realy massive damage."
                },
                {
                    "Troooops! Barracks are your soldier factory. They spawning soldiers to slow down enemies. If a soldier dies don't worry. It will respawn soon.",
                    "Assassins, the masters of murder. They are very fast, they can deal massive damage, and if it's not enough... their first hit to any enemy has an X2 crit. You might wanna have some, cause they can spot even the SkinWalker.",
                    "Knights are heavy weaponed soldiers. Slow, but strong. Moreover they have the thorns effect, so if an enemy hits them, they get beck 5% of the damage."
                },
                {
                    "Booom! Cannons are ready. You might wanna have some of these. They are not a fast shooters, but their damage output is high.",
                    "High range, high damage, aaand aditional areal damage, with a range of 100. Quite impressive, huh?",
                    "Trebuchets, they are not the most expesives for nothing. With them you can build boundaries to prespecified locations. Every boundary has 250HP, so it takes a little time for enemies to break through."
                }
        };
        // Archeries
        for (int i = 0; i < 5; ++i ) {
            WikiUIElement bgbg = new WikiUIElement(towersNames[0][i]);
            bgbg.addDrawComp(towersPos, new SingleSprite(spriteNames[0][i] + ".png"));
            bgbg.addStat("Cost", towersGet[0][i]);
            bgbg.addStat("Range", towersRanges[0][i]);
            bgbg.addStat("Archers' damage", towersDamages[0][i]);
            bgbg.addStat("Archers' attack rate", towersDelays[0][i]);
            bgbg.addStat("Tower's dmg/sec", towersDMGsecs[0][i]);
            bgbg.setDescr(towersDescrs[0][Math.max(0, i - 2)]);
            towerElements.add(bgbg);
        }
        // Barrackss
        Position barracksPoss[] = {
                new Position(285, 160),
                new Position(335, 165),
                new Position(385, 160),
        };
        String baracksTextures[] = { "soldier", "soldier", "soldier", "assassin", "knight" };
        String barracksTimes[] = { "10.0", "9.2", "8.3", "7.5", "11.6" };
        String barracksHPs[] = { "40", "50", "60", "35", "80" };
        String barracksSpeeds[] = { "1.0", "1.0", "1.5", "2.0", "0.8" };
        for (int i = 0; i < 5; ++i ) {
            WikiUIElement bgbg = new WikiUIElement(towersNames[1][i]);
            bgbg.addDrawComp(towersPos, new SingleSprite(spriteNames[1][i] + ".png"));
            for (int j = 0; j < 3; ++j) {
                bgbg.addDrawComp(barracksPoss[0], new SingleSprite(baracksTextures[i] + ".png"));
                bgbg.addDrawComp(barracksPoss[1], new SingleSprite(baracksTextures[i] + ".png"));
                if (i != 3) {
                    bgbg.addDrawComp(barracksPoss[2], new SingleSprite(baracksTextures[i] + ".png"));
                }
            }
            bgbg.addStat("Cost", towersGet[1][i]);
            bgbg.addStat("Range", towersRanges[1][i]);
            bgbg.addStat("Respawn time", barracksTimes[i]);
            bgbg.addStat("Soldiers' HP", barracksHPs[i]);
            bgbg.addStat("Soldiers' speed", barracksSpeeds[i]);
            bgbg.addStat("Soldiers' damage", towersDamages[1][i]);
            bgbg.addStat("Soldiers' attack rate", towersDelays[1][i]);
            bgbg.addStat("Tower's dmg/sec", towersDMGsecs[1][i]);
            bgbg.setDescr(towersDescrs[1][Math.max(0, i - 2)]);
            towerElements.add(bgbg);
        }
        // Cannonss
        for (int i = 0; i < 5; ++i ) {
            WikiUIElement bgbg = new WikiUIElement(towersNames[2][i]);
            bgbg.addDrawComp(towersPos, new SingleSprite(spriteNames[2][i] + ".png"));
            bgbg.addStat("Cost", towersGet[2][i]);
            if (i == 4) {
                bgbg.addStat("Recharge time(ticks)", towersDelays[2][i]);
                bgbg.addStat("Recharge time(secs)", "12.5");
            } else {
                bgbg.addStat("Range", towersRanges[2][i]);
                bgbg.addStat("Tower's damage", towersDamages[2][i]);
                bgbg.addStat("Tower's attack rate", towersDelays[2][i]);
                bgbg.addStat("Tower's dmg/sec", towersDMGsecs[2][i]);
            }
            bgbg.setDescr(towersDescrs[2][Math.max(0, i - 2)]);
            towerElements.add(bgbg);
        }


        //Bestiary
        String textsBestiary[] = { "Goblin", "Ork", "Troll", "Werewolf", "SkinWalker", "Boss" };
        int YsBestiary[] = { 25, 75, 125, 175, 225, 275 };
        DrawComponent bestiaryDrawComp = new SingleSprite("bestiarybutton.png");
        for (int i = 0; i < textsBestiary.length; ++i) {
            UIElement ui = new UIElement(Command.NONE, new Position(120, YsBestiary[i]), 40, 150);
            ui.setText(textsBestiary[i], 30 * Game.getScale(), "TimesRoman", Color.BLACK);
            ui.setDrawComponent(bestiaryDrawComp);
            bestiaryElements.add(ui);
        }
        bestiaryElements.add(back);
        { // BG
            UIElement bg = new UIElement(Command.NONE, new Position(420, 180));
            bg.setDrawComponent(new SingleSprite("bestiaryBG.png"));
            bestiaryElements.add(bg);
        }
        String bestiaryNames[] = { "Goblin", "Ork", "Troll", "Werewolf", "SkinWalker", "Boss" };
        Position beatiaryPoss[] = {
                new Position(290, 150),
                new Position(320, 160),
                new Position(350, 120),
                new Position(380, 110),
                new Position(410, 140)
        };
        int[][] bestiaryPosindexes = {
                { 0, 2, 4 },
                { 1, 3 },
                { 2 },
                { 2, 1 },
                { 1, 3 },
                { 2 }
        };
        String[][] bestiaryTextures = {
                { "goblin", "goblin", "goblin" },
                { "ork", "ork" },
                { "troll" },
                { "werewolf", "werewolf_running" },
                { "skinwalker_human", "skinwalker" },
                { "boss" }
        };
        String bestiaryHPs[] = { "20", "50", "100", "200", "80", "5000" };
        String bestiarySpeeds[] = { "1.5", "1.0", "0.3", "3.0 -> 1.0", "1.0", "0.25" };
        String bestiaryDamages[] = { "1", "7", "30", "50", "60", "100" };
        String bestiaryAttackRates[] = { "12", "30", "70", "30", "20", "24" };
        String bestiaryDMGsecs[] = { "2.0", "5.6", "10.3", "40.0", "72.0", "100.0" };
        String bestiaryCoinAwards[] = { "10", "20", "50", "80", "100", "500" };
        String bestiaryXPAwards[] = { "5", "8", "15", "30", "25", "500" };
        String bestiaryHPlosses[] = { "1", "1", "3", "3", "4", "10" };
        String bestiaryDescrs[] = {
                "Goblins are small and fast enemies. They are not really strong, but you better not underestimate them!",
                "Orks are the standard enemies. With medium speed, hp, and damage they are slightly stronger, than the avarege soldiers.",
                "Trolls are the heavy guns. Slow, but very though. However, their slowness might be the key to defeat them.",
                "Werewolves are the all-in-ones. Fast and strong at the same time. First they are running, way faster, then anyone else. To slow them down you need to sacrifice some troops.",
                "Skinwalkers are the most evil enemies. They are walking in the disguise of normal humans. Only your hero, and the assassins can spot him.",
                "WATCH OOOUUT!!! Here comes the boss. Your final challenge is to defeat it. Be aware, it can spawn other enemies quite frequently, and it will take all your HPs away, if it reaches the castle..."
        };
        for (int i = 0; i < bestiaryNames.length; ++i ) {
            WikiUIElement bgbg = new WikiUIElement(bestiaryNames[i]);
            for (int j = 0; j < bestiaryTextures[i].length; ++j) {
                bgbg.addDrawComp(beatiaryPoss[bestiaryPosindexes[i][j]], new SingleSprite(bestiaryTextures[i][j] + ".png"));
            }
            bgbg.addStat("HP", bestiaryHPs[i]);
            bgbg.addStat("Speed", bestiarySpeeds[i]);
            bgbg.addStat("Damage", bestiaryDamages[i]);
            bgbg.addStat("Attack rate", bestiaryAttackRates[i]);
            bgbg.addStat("Dmg/sec", bestiaryDMGsecs[i]);
            bgbg.addStat("Coin award", bestiaryCoinAwards[i]);
            bgbg.addStat("Xp award", bestiaryXPAwards[i]);
            bgbg.addStat("Hp loss", bestiaryHPlosses[i]);
            bgbg.setDescr(bestiaryDescrs[i]);
            bestiaryElements.add(bgbg);
        }


        //Options
        String textsOptions[] = { "Set FullHD",  "Sound On/Off", "Back" };
        int YsOptions[] = { 30, 90, 320};
        Command commandsOptions[] = { Command.RESOLUTION, Command.NONE, Command.BACK };
        for (int i = 0; i < textsOptions.length; ++i) {
            UIElement ui = new UIElement(commandsOptions[i], new Position(120, YsOptions[i]), 50, 150);
            ui.setText(textsOptions[i], 30 * Game.getScale(), "TimesRoman", Color.BLACK);
            ui.setDrawComponent(drawComponent);
            optionElements.add(ui);
        }
    }

    public void setState(int state) {
        this.state = state;
        currActive = 0;
    }


    public UIElement getUIElementFromOptions(int ind) {
        return optionElements.get(ind);
    }

    /**
     * Redraws the active UI elements int the active menu page in every tick
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        switch (state) {
            case 0:
                for (UIElement elem : mainMenuElements) {
                    if (elem.isActive()) {
                        elem.draw(g2);
                    }
                }
                break;

            case 1:
                for (UIElement elem : levelMenuElements) {
                    if (elem.isActive()) {
                        elem.draw(g2);
                    }
                }
                break;

            case 2:
                for (int i = 0; i < 17; ++i) {
                    towerElements.get(i).draw(g2);
                }
                towerElements.get(17 + currActive).draw(g2);
                break;

            case 3:
                for (int i = 0; i < 8; ++i) {
                    bestiaryElements.get(i).draw(g2);
                }
                bestiaryElements.get(8 + currActive).draw(g2);
                break;

            case 4:
                for (UIElement elem : optionElements) {
                    if (elem.isActive()) {
                        elem.draw(g2);
                    }
                }
                break;
        }
    }

    /**
     * Manages click
     * @param pos position of click
     * @return true if any button is clicked
     */
    @Override
    public boolean onClick(Position pos) {
        switch (state){
            case 0:
                for(UIElement elem : mainMenuElements){
                    if(elem.isActive() && elem.isClickable() && elem.isClicked(pos)){
                        lastCommand = elem.getCommand();
                    }
                }
                break;
            case 1:
                for(UIElement elem : levelMenuElements){
                    if(elem.isActive() && elem.isClickable() && elem.isClicked(pos)){
                        lastCommand = elem.getCommand();
                    }
                }
                break;
            case 2:
                for (int i = 0; i < 15; ++i) {
                    if (towerElements.get(i).isClicked(pos)) {
                        currActive = i;
                    }
                }
                if (towerElements.get(15).isClicked(pos)) { // Back button
                    lastCommand = towerElements.get(15).getCommand();
                }
                break;
            case 3:
                for (int i = 0; i < 6; ++i) {
                    if (bestiaryElements.get(i).isClicked(pos)) {
                        currActive = i;
                    }
                }
                if (bestiaryElements.get(6).isClicked(pos)) { // Back button
                    lastCommand = bestiaryElements.get(6).getCommand();
                }
                break;
            case 4:
                for(UIElement elem : optionElements){
                    if(elem.isActive() && elem.isClickable() && elem.isClicked(pos)){
                        lastCommand = elem.getCommand();
                    }
                }
                break;
        }
        return false;
    }
}
