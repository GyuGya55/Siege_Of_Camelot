package game;

/**
 * Class for providing unified tick number
 * @author KarpatTech
 */
public class GameTimer {

    private static int tickNum = 0;

    public static int getTickNum() {
        return tickNum;
    }

    /**
     * Called once in every tick (24 times in a second)
     */
    public static void tick() {
        ++tickNum;
    }
}
