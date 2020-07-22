package game.onscreen.tower;

import game.Game;
import game.hud.Command;
import game.hud.TowerHUD;
import game.hud.UIElement;
import game.onscreen.OnScreenElement;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;
import game.onscreen.entity.Enemy;
import game.onscreen.entity.Soldier;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class responsible for tower places
 * @author KarpatTech
 */
public class TowerPlace extends OnScreenElement {
    private Tower tower;
    private TowerHUD hud;
    private int direction;
    private int towerPrices[] = {100, 120, 150}; // Archery, Barracks, Cannons

    /**
     * Constructor
     * @param pos, direction
     * @throws IOException when some resources are missing
     */
    public TowerPlace(Position pos, int direction) throws IOException {
        super.pos = pos;
        super.setDrawComponent(new SingleSprite("towerplace.png"));
        this.direction = direction;
        hud = new TowerHUD(pos);
    }

    /**
     * Called once every tick. Responsible for buying towers
     * @return how many coins was spent
     * @throws IOException when some resources are missing
     */
    public int tick(int coins) throws IOException {
        int spent = 0; // should not be -1 (-1 is a signal)
        Command currCommand = hud.getLastCommand();
        if (currCommand != Command.NONE) {
            switch (currCommand) {
                case OPEN:
                    hud.open();
                    if (hud.getState() == 32) { // isTrebuchet
                        if (((Cannons)tower).isLoaded()) {
                            return -1; // open boundaryHUD signal
                        }
                    }
                    break;
                case ARCHERY:
                    if (coins > towerPrices[0]) {
                        tower = new Archery(new Position(pos.x, pos.y - 10));
                        hud.setState(1);
                        spent += towerPrices[0];
                    }
                    hud.close();
                    break;
                case BARRACKS:
                    if (coins > towerPrices[1]) {
                        tower = new Barracks(new Position(pos.x, pos.y - 10), direction);
                        hud.setState(2);
                        spent += towerPrices[1];
                    }
                    hud.close();
                    break;
                case CANNONS:
                    if (coins > towerPrices[2]) {
                        tower = new Cannons(new Position(pos.x, pos.y - 10));
                        hud.setState(3);
                        spent += towerPrices[2];
                    }
                    hud.close();
                    break;
                case SPEC_A:
                    if (tower.getLevel() == 3 && coins > tower.getPriceOfSpecA()) {
                        tower.specialize(Command.SPEC_A);
                        spent += tower.getPriceOfSpecA();
                        hud.setState(hud.getState() * 10 + 1);
                    }
                    hud.close();
                    break;
                case SPEC_B:
                    if (tower.getLevel() == 3 && coins > tower.getPriceOfSpecB()) {
                        tower.specialize(Command.SPEC_B);
                        spent += tower.getPriceOfSpecB();
                        hud.setState(hud.getState() * 10 + 2);
                    }
                    hud.close();
                    break;
                case CANCEL:
                    hud.close();
                    break;
                case DESTROY:
                    if (tower.getLevel() == 4) {
                        spent -= tower.getPriceOfSpecA() / 2;
                    } else if (tower.getLevel() == 5) {
                        spent -= tower.getPriceOfSpecB() / 2;
                    } else {
                        spent -= towerPrices[hud.getState() - 1] / 2;
                    }
                    tower.clear();
                    tower = null;
                    hud.setState(0);
                    hud.close();
            }
        }

        if (tower != null) {
            tower.tick();
        }

        return spent;
    }

    public void shotAt(Position pos) {
        hud.close();
        Cannons cannon = (Cannons)tower;
        cannon.shotAt(pos);
    }

    public boolean clicked(Position pos) {
        return hud.onClick(pos);
    }

    /**
     * Void responsible for handling enemies
     * @param enemies list of enemies
     * @throws IOException when some resources are missing
     */
    public void handleEnemies(ArrayList<Enemy> enemies) throws IOException {
        if (tower != null) {
            tower.handleEnemies(enemies);
        }
    }

    public boolean isTowerNull() {
        return tower == null;
    }

    public boolean isInRange(Enemy e) {
        return tower.isInRange(e);
    }

    public Position getPos() {
        return pos;
    }

    /**
     * Void responsible for drawing towers
     * @param g2 parameter for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (tower != null) {
            tower.draw(g2);
        }
        hud.draw(g2);
    }
}
