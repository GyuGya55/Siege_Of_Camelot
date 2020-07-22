import game.Game;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            new Game();
        } catch (IOException e) {
            System.out.println("Some resources are missing!");
        }
    }

}
