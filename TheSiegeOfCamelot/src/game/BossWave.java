package game;

import game.onscreen.Position;
import game.onscreen.entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class representing a boss wave. Spawns enemies
 */
public class BossWave extends Wave {

    /**
     * Delay between enemy spawns
     */
    private int delay = 360;
    private int posRandom = 30;

    /**
     * Chances of each enemy to spawn
     */
    private int[][] chances = {
        {100, 0, 0, 0, 0},
        {10, 50, 40, 0, 0},
        {0, 0, 30, 40, 30}
    };
    private Random rand;

    /**
     * Constructor
     * @param pos starter position of the Boss
     * @param targetPos starter target of the Boss
     * @param routeInd route index of the Boss
     * @throws IOException if some resources are missing
     */
    public BossWave(Position pos, Position targetPos, int routeInd) throws IOException {
        enemies = new ArrayList<>();
        enemies.add(new Boss(pos, targetPos, routeInd));
        numOfSpawned = 1;

        rand = new Random();
    }

    @Override
    public void start() throws IOException {
        nextSpawn = GameTimer.getTickNum() + delay;
        tick();
    }

    /**
     * Called once in every tick (24 times per second)
     * @throws IOException
     */
    @Override
    public void tick() throws IOException {
        if (nextSpawn == GameTimer.getTickNum()) {
            Position pos = enemies.get(0).getPos(); // Boss
            Position targetPos = enemies.get(0).getTargetPos();
            int routeInd = enemies.get(0).getRouteInd(); // Boss

            for (int i = 0; i < 3; ++i) {
                int a = rand.nextInt(100);
                int j = 0;
                while (a > 0) {
                    a -= chances[i][j];
                    ++j;
                }

                switch (j) {
                    case 1:
                        enemies.add(new Enemy("g", pos.randomize(posRandom), targetPos.randomize(posRandom), routeInd));
                        break;
                    case 2:
                        enemies.add(new Enemy("o", pos.randomize(posRandom), targetPos.randomize(posRandom), routeInd));
                        break;
                    case 3:
                        enemies.add(new Enemy("t", pos.randomize(posRandom), targetPos.randomize(posRandom), routeInd));
                        break;
                    case 4:
                        enemies.add(new Werewolf(pos.randomize(posRandom), targetPos.randomize(posRandom), routeInd));
                        break;
                    case 5:
                        enemies.add(new Skinwalker(pos.randomize(posRandom), targetPos.randomize(posRandom), routeInd));
                        break;
                }
                ++numOfSpawned;
            }
            nextSpawn = GameTimer.getTickNum() + delay;
        }
    }

    @Override
    public boolean isEnded() {
        return enemies.get(0).getHp() <= 0; // Boss
    }
}
