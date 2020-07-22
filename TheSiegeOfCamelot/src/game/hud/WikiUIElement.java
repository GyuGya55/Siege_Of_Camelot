package game.hud;

import game.Game;
import game.onscreen.Position;
import game.onscreen.draw.DrawComponent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for Towers and Bestiary elements
 * @author KarpatTech
 */
public class WikiUIElement extends UIElement {

    private ArrayList<DrawComponent> drawComps;
    private ArrayList<Position> poss;

    private String name;
    private ArrayList<String> stats;
    private ArrayList<String> descr;

    private Font titleFont;
    private Font smallFont;

    /**
     * Constructor
     * @param name name of element
     * @throws IOException when some textures are missing
     */
    public WikiUIElement(String name) throws IOException {
        super(Command.NONE, new Position(420, 180));
        this.name = name;
        titleFont = new Font("TimesRoman", Font.BOLD, 28 * Game.getScale());
        smallFont = new Font("TimesRoman", Font.BOLD, 12 * Game.getScale());

        drawComps = new ArrayList<>();
        poss = new ArrayList<>();
        stats = new ArrayList<>();
        descr = new ArrayList<>();
    }

    public void addDrawComp(Position pos, DrawComponent drawComp) {
        poss.add(pos);
        drawComps.add(drawComp);
    }

    public void addStat(String statName, String statValue) {
        stats.add(statName + ": " + statValue);
    }

    /**
     * Cutting secription to lines
     * @param description secription
     */
    public void setDescr(String description) {
        final int lineLimit = 45;
        String words[] = description.split(" ");
        int i = 0, lineNum = 0;
        while (i < words.length) {
            int currLength = words[i].length();
            int j = i + 1;
            descr.add(words[i]);
            while (j < words.length && (currLength += words[j].length()) < lineLimit) {
                ++j;
            }
            for (int k = i + 1; k < j; ++k) {
                descr.set(lineNum, descr.get(lineNum) + " " + words[k]);
            }
            i = j;
            ++lineNum;
        }
    }

    /**
     * Redraws itself in every tick
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        // Entities/towers
        for (int i = 0; i < drawComps.size(); ++i) {
            drawComps.get(i).drawWithScale(g2, poss.get(i), 2);
            int scale = Game.getScale();
        }
        //Title
        int scale = Game.getScale();
        g2.setColor(Color.WHITE);
        g2.setFont(titleFont);
        g2.drawString(name, 440 *scale, 50 * scale);
        //Stats
        g2.setColor(Color.BLACK);
        g2.setFont(smallFont);
        for (int i = 0; i < stats.size(); ++i) {
            g2.drawString(stats.get(i), 440 * scale, (90 + 15 * i) * scale);
        }
        //Description
        for (int i = 0; i < descr.size(); ++i) {
            g2.drawString(descr.get(i), 260 * scale, (250 + 15 * i) * scale);
        }
    }
}
