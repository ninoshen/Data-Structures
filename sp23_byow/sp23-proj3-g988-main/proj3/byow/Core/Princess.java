package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


public class Princess {
    public Mark position;
    public TETile[][] wonderland;
    public int collected;


    public Princess(TETile[][] world) {
        wonderland = world;
        collected = 0;
        boolean goodPlace = false;
        while (!goodPlace) {
            int x = DisneyFactory.fairy.nextInt(world.length);
            int y = DisneyFactory.fairy.nextInt(world[0].length);
            if (world[x][y] == Tileset.FLOOR) {
                world[x][y] = Tileset.AVATAR;
                position = new Mark(x, y);
                goodPlace = true;
            }
        }
    }

    // move according to key
    public void moveLeft() {
        if (wonderland[position.xCord - 1][position.yCord] != Tileset.WALL) {
            if (wonderland[position.xCord - 1][position.yCord] == Tileset.FLOWER) {
                collected += 1;
            }
            wonderland[position.xCord][position.yCord] = Tileset.FLOOR;
            wonderland[position.xCord - 1][position.yCord] = Tileset.AVATAR;
            position.xCord -= 1;
        }
    }

    public void moveDown() {
        if (wonderland[position.xCord][position.yCord - 1] != Tileset.WALL) {
            if (wonderland[position.xCord][position.yCord - 1] == Tileset.FLOWER) {
                collected += 1;
            }
            wonderland[position.xCord][position.yCord] = Tileset.FLOOR;
            wonderland[position.xCord][position.yCord - 1] = Tileset.AVATAR;
            position.yCord -= 1;
        }
    }

    public void moveRight() {
        if (wonderland[position.xCord + 1][position.yCord] != Tileset.WALL) {
            if (wonderland[position.xCord + 1][position.yCord] == Tileset.FLOWER) {
                collected += 1;
            }
            wonderland[position.xCord][position.yCord] = Tileset.FLOOR;
            wonderland[position.xCord + 1][position.yCord] = Tileset.AVATAR;
            position.xCord += 1;
        }
    }

    public void moveUp() {
        if (wonderland[position.xCord][position.yCord + 1] != Tileset.WALL) {
            if (wonderland[position.xCord][position.yCord + 1] == Tileset.FLOWER) {
                collected += 1;
            }
            wonderland[position.xCord][position.yCord] = Tileset.FLOOR;
            wonderland[position.xCord][position.yCord + 1] = Tileset.AVATAR;
            position.yCord += 1;
        }
    }
}
