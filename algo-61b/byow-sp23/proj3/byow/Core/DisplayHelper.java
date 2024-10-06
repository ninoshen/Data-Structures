package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class DisplayHelper {
    private static int horizontal;
    private static int vertical;
    public Princess myPrincess;
    public TETile[][] myWorld;
    public String myMemory;
    public TERenderer myHelp;
    public static boolean gameOver;

    public DisplayHelper(int x, int y, TERenderer t) {
        horizontal = x;
        vertical = y;
        myHelp = t;
        gameOver = false;
    }

    public void menu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setCanvasSize(horizontal * 16, vertical * 16);
        StdDraw.setXscale(0, horizontal);
        StdDraw.setYscale(0, vertical);
        Font font = new Font("Monaco", Font.BOLD, 25);
        StdDraw.setFont(font);
        StdDraw.enableDoubleBuffering();
        // draw menu options
        StdDraw.text(horizontal * 0.5, vertical * 0.7, "MENU");
        StdDraw.text(horizontal * 0.5, vertical * 0.6, "N: New World");
        StdDraw.text(horizontal * 0.5, vertical * 0.5, "L: Load World");
        StdDraw.text(horizontal * 0.5, vertical * 0.4, "R: Replay");
        StdDraw.text(horizontal * 0.5, vertical * 0.3, "Q: Quit");
        StdDraw.show();
    }

    public static void seedPage(String seed) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(horizontal * 0.5, vertical * 0.8, "Please enter your numerical seed below");
        StdDraw.text(horizontal * 0.5, vertical * 0.7, "and press S to start the game:");
        StdDraw.text(horizontal * 0.5, vertical * 0.5, seed);
        StdDraw.show();
    }


    public String gatherSeed() {
        String seed = "";
        while (true) {
            seedPage(seed);
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char curr = StdDraw.nextKeyTyped();
            if (curr == 's' || curr == 'S') {
                if (seed.isEmpty()) {
                    continue;
                }
                return seed;
            }
            if (!Character.isDigit(curr)) {
                continue;
            }
            seed += curr;
        }
    }

    public void whereToGo(char key) {
        if (key == 'a' || key == 'A') {
            myPrincess.moveLeft();
        } else if (key == 's' || key == 'S') {
            myPrincess.moveDown();
        } else if (key == 'd' || key == 'D') {
            myPrincess.moveRight();
        } else if (key == 'w' || key == 'W') {
            myPrincess.moveUp();
        }
    }

    public void saveWorld() {
        Out out = new Out("byow/Core/memories.txt");
        out.print(myMemory);
    }

    public String loadWorld() {
        In in = new In("byow/Core/memories.txt");
        String memory = in.readLine();
        return memory;
    }

    public void headsUP() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (!gameOver) {
            if (x < horizontal && y < vertical) {
                if (myWorld[x][y].equals(Tileset.FLOOR)) {
                    myHelp.renderFrame(myWorld);
                    StdDraw.enableDoubleBuffering();
                    StdDraw.setPenColor(new Color(158, 128, 192));
                    StdDraw.text(horizontal * 0.1, vertical - 1.0, "This is floor~");
                } else if (myWorld[x][y].equals(Tileset.WALL)) {
                    myHelp.renderFrame(myWorld);
                    StdDraw.enableDoubleBuffering();
                    StdDraw.setPenColor(new Color(158, 128, 192));
                    StdDraw.text(horizontal * 0.1, vertical - 1.0, "This is wall~");
                } else if (myWorld[x][y].equals(Tileset.AVATAR)) {
                    myHelp.renderFrame(myWorld);
                    StdDraw.enableDoubleBuffering();
                    StdDraw.setPenColor(new Color(158, 128, 192));
                    StdDraw.text(horizontal * 0.1, vertical - 1.0, "This is you~");
                } else if (myWorld[x][y].equals(Tileset.FLOWER)) {
                    myHelp.renderFrame(myWorld);
                    StdDraw.enableDoubleBuffering();
                    StdDraw.setPenColor(new Color(158, 128, 192));
                    StdDraw.text(horizontal * 0.1, vertical - 1.0, "This is flower~");
                } else {
                    myHelp.renderFrame(myWorld);
                    StdDraw.enableDoubleBuffering();
                    StdDraw.setPenColor(new Color(158, 128, 192));
                    StdDraw.text(horizontal * 0.1, vertical - 1.0, "There's nothing here >.<");
                }
                SimpleDateFormat cleaner = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z", Locale.ENGLISH);
                Date realTime = new Date(System.currentTimeMillis());
                StdDraw.text(horizontal * 0.5, vertical - 1.0, cleaner.format(realTime));
                StdDraw.text(horizontal * 0.9, vertical - 1.0, "Flowers collected: " + myPrincess.collected);
                StdDraw.show();
            }
        }
    }
}
