package game;

import game.hud.BoundaryHUD;
import game.onscreen.BackgroundElement;
import game.onscreen.Position;
import game.onscreen.draw.SingleSprite;
import game.onscreen.entity.*;
import game.onscreen.projectile.Projectile;
import game.onscreen.tower.TowerPlace;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Class responsible for core inLevel mechanics
 * @author KarpatTech
 */
public class Level {

    private ArrayList<TowerPlace> towers;
    private ArrayList<Wave> waves;
    private Map map;

    private ArrayList<BoundaryPlace> boundaries;
    private BoundaryHUD bHUD;
    private int activatingTowerInd;

    // Hero stuffs
    private Hero hero;
    private int delayToSpawn = 300;
    private int nextTimeToSpawn = 0;
    private Position heroSpawn;
    private Position heroDest;

    private final int ROUTING_RANDOM = 10;
    private int actualWave = -1;

    private int hp = 10;
    private int coins = 4000;

    /**
     * Constructor. Initializes a current level from a txt file
     * @param fname filename
     * @throws IOException when some resources are missing
     */
    public Level(String fname) throws IOException {
        towers = new ArrayList<>();
        waves = new ArrayList<>();
        boundaries = new ArrayList<>();
        bHUD = new BoundaryHUD();
        map = new Map();

        Scanner in = new Scanner(new BufferedReader(new FileReader(fname)));
        //Hero
        heroSpawn = new Position(in.nextInt(), in.nextInt());
        heroDest = new Position(in.nextInt(), in.nextInt());
        spawnHero();
        //TowerPlaces
        int n = in.nextInt();
        for (int i = 0; i < n; ++i) {
            int x = in.nextInt(), y = in.nextInt(), dir = in.nextInt();
            towers.add(new TowerPlace(new Position(x, y), dir));
        }
        //Map -> Routes
        n = in.nextInt();
        for (int i = 0; i < n; ++i) {
            int m = in.nextInt();
            Route r = new Route();
            for (int j = 0; j < m; ++j) {
                int x = in.nextInt(), y = in.nextInt();
                r.addPoint(new Position(x ,y));
            }
            map.addRoute(r);
        }
        //Map -> BGElements
        n = in.nextInt();
        for (int i = 0; i < n; ++i) {
            int x = in.nextInt(), y = in.nextInt();
            String texture = in.next();
            //BackgroundElement bge = new BackgroundElement(new Position(x, y));
            //bge.setDrawComponent(new SingleSprite(texture));
            //map.addDecoration(bge);
        }
        //Waves
        n = in.nextInt();
        for (int i = 0; i < n; ++i) {
            int m = in.nextInt();
            if (m == 0) {
                int ind = in.nextInt();
                Position pos = map.getRoute(ind).getPointWithRandom(0, ROUTING_RANDOM);
                Position targetPos = map.getRoute(ind).getPointWithRandom(1, ROUTING_RANDOM);
                waves.add(new BossWave(pos, targetPos, ind));
            } else {
                Wave w = new Wave();
                for (int j = 0; j < m; ++j) {
                    String type = in.next();
                    int ind = in.nextInt(), delay = in.nextInt();
                    Position pos = map.getRoute(ind).getPointWithRandom(0, ROUTING_RANDOM);
                    Position targetPos = map.getRoute(ind).getPointWithRandom(1, ROUTING_RANDOM);
                    switch (type) {
                        case "g":
                        case "o":
                        case "t":
                            w.addEnemy(new Enemy(type, pos, targetPos, ind), delay);
                            break;
                        case "w":
                            w.addEnemy(new Werewolf(pos, targetPos, ind), delay);
                            break;
                        case "s":
                            w.addEnemy(new Skinwalker(pos, targetPos, ind), delay);
                            break;
                        case "b":
                            w.addEnemy(new Boss(pos, targetPos, ind), delay);
                            break;
                    }
                }
                waves.add(w);
            }
        }
        //BoundaryPlaces
        n = in.nextInt();
        for (int i = 0; i < n; ++i) {
            int x = in.nextInt(), y = in.nextInt();
            String type = in.next();
            boundaries.add(new BoundaryPlace(new Position(x, y), type.equals("v")));
            bHUD.addPlace(new Position(x, y));
        }
    }

    /**
     * Called once every tick. Handles core mechanics
     * @return signal for LevelUI class
     * @throws IOException when some resources are missing
     */
    public int tick() throws IOException {
        // Hero stuffs
        if (hero == null && GameTimer.getTickNum() >= nextTimeToSpawn) {
            spawnHero();
        } else if (hero != null) {
            hero.tick();
            if (hero.getHp() <= 0) {
                hero = null;
                nextTimeToSpawn = GameTimer.getTickNum() + delayToSpawn;
            }
        }

        // tower ticks
        boolean needToRefreshHUD = false;
        for (int i = 0; i < towers.size(); ++i) {
            int spent = towers.get(i).tick(coins);
            if (spent == -1) { // open boundaryHUD signal
                bHUD.open();
                activatingTowerInd = i;
            } else if (spent != 0) {
                needToRefreshHUD = true;
                coins -= spent;
            }
        }

        // Boundary ticks
        for (int i = 0; i < boundaries.size(); ++i) {
            if (boundaries.get(i).tick()) {
                bHUD.getUIElement(i).setActive(true);
            }
        }

        // Enemy routing
        if (actualWave != -1) {
            waves.get(actualWave).tick();
            ArrayList<Enemy> spawned = waves.get(actualWave).getSpawnedEnemies();
            for(Enemy enemy : spawned){
                enemy.tick();

                for (BoundaryPlace bound : boundaries) {
                    if (bound.isInRange(enemy)) {
                        enemy.setTarget(bound.getBoundary());
                        enemy.setState(2); // Fighting
                    }
                }

                if (enemy.isCloseToTarget() && enemy.getState() == 0) {
                    enemy.nextPoint();
                    if (enemy.getRouteStatus() == map.getRoute(enemy.getRouteInd()).size()) { // ended route
                        enemy.setHp(0);
                        hp -= enemy.getHpLoss();
                        if (hp <= 0) {
                            return 3; // Game over signal
                        }
                        needToRefreshHUD = true;
                    } else {
                        enemy.setTargetPos(map.getRoute(enemy.getRouteInd()).getPointWithRandom(enemy.getRouteStatus(), ROUTING_RANDOM));
                    }
                }
            }

            spawned = waves.get(actualWave).getSpawnedEnemies();
            for (TowerPlace tow : towers) {
                tow.handleEnemies(spawned);
            }
            if (hero != null) {
                hero.handleEnemies(spawned);
            }

            int price = waves.get(actualWave).getPrice();
            if (price != 0) {
                coins += price;
                needToRefreshHUD = true;
            }
        }

        if (needToRefreshHUD) {
            return 1; // Need to refresh HUD signal
        }
        if (actualWave != -1 && waves.get(actualWave).isEnded()) {
            if (actualWave < waves.size() - 1) {
                return 2; // Activate next wave button signal
            } else {
                return 4; // Victory signal
            }
        }
        return 0;
    }

    /**
     * Forwards click from LevelUI to Towers (to TowerHUDs)
     * @param pos
     * @return
     */
    public boolean clicked(Position pos) {
        // boundaryHUD stuffs
        if (bHUD.onClick(pos)) {
            int boundaryInd = bHUD.getLastClickResult();
            bHUD.getUIElement(boundaryInd).setActive(false);
            BoundaryPlace actualBoundary = boundaries.get(boundaryInd);
            TowerPlace actualTower = towers.get(activatingTowerInd);
            actualTower.shotAt(actualBoundary.getPos());
            actualBoundary.buildWhen(GameTimer.getTickNum() + (int)actualTower.getPos().plus(new Position(20, -40)).distance(actualBoundary.getPos()) / 6);
            bHUD.close();
            return true;
        }
        // forward click
        int i = 0;
        while (i < towers.size() && !towers.get(i).clicked(pos)) {
            ++i;
        }
        return i < towers.size();
    }

    public void startNextWave() throws IOException {
        ++actualWave;
        waves.get(actualWave).start();
    }

    public void spawnHero() throws IOException {
        hero = new Hero(heroSpawn, 100, 1f, 20, 30);
        hero.setTargetPos(heroDest);
    }

    public int getHp() {
        return hp;
    }

    public int getCoins() {
        return coins;
    }

    /**
     * Redraws every active OnScreenElement in level once in a tick
     * @param g2 parameter for drawing
     */
    public void draw(Graphics2D g2) {
        if (actualWave != -1) {
            waves.get(actualWave).draw(g2);
        }

        if (hero != null) {
            hero.draw(g2);
        }

        map.draw(g2);

        for (TowerPlace tow : towers) {
            tow.draw(g2);
        }

        for (BoundaryPlace bound : boundaries) {
            bound.draw(g2);
        }

        bHUD.draw(g2);
    }
}
