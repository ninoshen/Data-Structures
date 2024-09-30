package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
    TERenderer mermaid = new TERenderer();
    DisplayHelper helper = new DisplayHelper(WIDTHH, HEIGHTT, mermaid);
    String stored = "";
    Boolean isValid = true;
    Boolean gameStarted = false;
    Boolean isLoading = false;
    Boolean isReplay = false;
    Boolean worldCreated = false;
    public static final int WIDTHH = 60;
    public static final int HEIGHTT = 45;
    public static final int ELAPSE = 100;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        helper.menu();
        while (true) {
            if (isValid) {
                if (gameStarted) {
                    helper.headsUP();
                }
                if (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                while (StdDraw.hasNextKeyTyped()) {
                    char curr = StdDraw.nextKeyTyped();
                    magicWand(curr);
                }
            } else {
                isValid = true;
                continue;
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.)
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        if ((input.charAt(0) == 'l') || (input.charAt(0) == 'L')) {
            for (int n = 0; n < input.length(); n++) {
                magicWand(input.charAt(n));
            }
        } else {
            int i = 1;
            String seed = "";
            while ((input.length() > i) && (input.charAt(i) != 's') && (input.charAt(i) != 'S')) {
                seed += input.charAt(i);
                i++;
            }
            int temp = i;
            stored += 'n';
            stored += seed;
            stored += 's';
            DisneyFactory palace = new DisneyFactory(seed);
            TETile[][] disney = palace.createWorld();
            worldCreated = true;
            helper.myWorld = disney;
            helper.myPrincess = new Princess(disney);
            if (input.length() > temp + 1) {
                for (int j = temp + 1; j < input.length(); j++) {
                    if (input.charAt(j) != ':') {
                        magicWand(input.charAt(j));
                    } else {
                        helper.myMemory = stored;
                        helper.saveWorld();
                    }
                }
            }
        }
        return helper.myWorld;
    }

    public void magicWand(char curr) {
        ArrayList<Character> moves = new ArrayList<>(Arrays.asList('A', 'a', 'S', 's', 'D', 'd', 'W', 'w'));
        if ((curr == 'n') || (curr == 'N')) {
            if (!isLoading && !isReplay && !gameStarted) {
                stored += Character.toString(curr);
                String seed = helper.gatherSeed();
                stored += seed;
                stored += "S";
                DisneyFactory palace = new DisneyFactory(seed);
                TETile[][] disney = palace.createWorld();
                worldCreated = true;
                helper.myWorld = disney;
                helper.myPrincess = new Princess(disney);
                mermaid.initialize(WIDTHH, HEIGHTT);
                mermaid.renderFrame(palace.disneyland);
                gameStarted = true;
            }
        } else if (moves.contains(curr)) {
            if (worldCreated) {
                stored += Character.toString(curr);
                helper.whereToGo(curr);
                if (!isLoading) {
                    mermaid.renderFrame(helper.myWorld);
                    if (isReplay) {
                        StdDraw.pause(ELAPSE);
                    }
                }
            }
        } else if (curr == ':') {
            while (true) {
                if (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                curr = StdDraw.nextKeyTyped();
                if (!((curr == 'q') || (curr == 'Q'))) {
                    throw new IllegalArgumentException("input :Q to quit!");
                }
                if (stored.isEmpty()) {
                    System.exit(0);
                }
                helper.myMemory = stored;
                helper.saveWorld();
                System.exit(0);
            }
        } else if ((curr == 'L') || (curr == 'l')) {
            if (!gameStarted) {
                String story = helper.loadWorld();
                if (story.isEmpty()) {
                    System.exit(0);
                }
                this.loading(story);
            }
        } else if ((curr == 'r') || (curr == 'R')) {
            if (!gameStarted) {
                String last = helper.loadWorld();
                if (last.isEmpty()) {
                    System.exit(0);
                }
                this.replay(last);
            }
        }
        {
            isValid = false;
        }
    }

    public void loading(String input) {
        isLoading = true;
        if ((input.charAt(0) == 'l') || (input.charAt(0) == 'L')) {
            magicWand(input.charAt(0));
        }
        int i = 1;
        String seed = "";
        while ((input.length() > i) && (input.charAt(i) != 's') && (input.charAt(i) != 'S')) {
            seed += input.charAt(i);
            i++;
        }
        int temp = i;
        stored += 'n';
        stored += seed;
        stored += 's';
        DisneyFactory palace = new DisneyFactory(seed);
        TETile[][] disney = palace.createWorld();
        worldCreated = true;
        helper.myWorld = disney;
        helper.myPrincess = new Princess(disney);
        if (input.length() > temp + 1) {
            for (int j = temp + 1; j < input.length(); j++) {
                if (input.charAt(j) != ':') {
                    magicWand(input.charAt(j));
                } else {
                    helper.myMemory = stored;
                    helper.saveWorld();
                }
            }
        }
        mermaid.initialize(WIDTHH, HEIGHTT);
        mermaid.renderFrame(palace.disneyland);
        gameStarted = true;
        isLoading = false;
    }

    public void replay(String input) {
        isReplay = true;
        if ((input.charAt(0) == 'l') || (input.charAt(0) == 'L')) {
            magicWand(input.charAt(0));
        }
        int i = 1;
        String seed = "";
        while ((input.length() > i) && (input.charAt(i) != 's') && (input.charAt(i) != 'S')) {
            seed += input.charAt(i);
            i++;
        }
        int temp = i;
        stored += 'n';
        stored += seed;
        stored += 's';
        DisneyFactory palace = new DisneyFactory(seed);
        TETile[][] disney = palace.createWorld();
        worldCreated = true;
        helper.myWorld = disney;
        helper.myPrincess = new Princess(disney);
        mermaid.initialize(WIDTHH, HEIGHTT);
        mermaid.renderFrame(palace.disneyland);
        if (input.length() > temp + 1) {
            for (int j = temp + 1; j < input.length(); j++) {
                if (input.charAt(j) != ':') {
                    magicWand(input.charAt(j));
                } else {
                    helper.myMemory = stored;
                    helper.saveWorld();
                }
            }
        }
        isReplay = false;
        gameStarted = true;
    }
}
