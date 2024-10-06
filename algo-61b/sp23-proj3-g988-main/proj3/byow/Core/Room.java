package byow.Core;

import byow.TileEngine.TETile;


public class Room implements Comparable<Room> {
    public Mark star; // left corner
    private final int height;
    private final int width;
    public Mark secretPoint;
    private final int proximity;

    public Room(int h, int w, Mark m) {
        this.height = h;
        this.width = w;
        this.star = m;
        this.secretPoint = getSecretPoint(m, w, h);
        this.proximity = star.xCord + star.yCord;
    }

    public void buildWalls(TETile[][] ground, TETile pattern) {
        for (int x = 0; x < width; x++) {
            ground[star.xCord + x][star.yCord] = pattern;
            ground[star.xCord + x][star.yCord + height - 1] = pattern;
        }
        for (int y = 0; y < height; y++) {
            ground[star.xCord][star.yCord + y] = pattern;
            ground[star.xCord + width - 1][star.yCord + y] = pattern;
        }
    }

    public void buildFloors(TETile[][] ground, TETile pattern) {
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                ground[star.xCord + x][star.yCord + y] = pattern;
            }
        }
    }

    private Mark getSecretPoint(Mark m, int width, int height) {
        int x = m.xCord + 1 + DisneyFactory.fairy.nextInt(width - 2);
        int y = m.yCord + 1 + DisneyFactory.fairy.nextInt(height - 2);
        return new Mark(x, y);
    }

    public void createFlowers(TETile[][] ground, TETile pattern) {
        Mark secret = getSecretPoint(this.star,this.width,this.height);
        int x = secret.xCord;
        int y = secret.yCord;
        ground[x][y] = pattern;
    }

    private Room getLeft(Room other) {
        if (this.star.xCord < other.star.xCord) {
            return this;
        }
        return other;
    }

    private Room getRight(Room other) {
        if (this.star.xCord > other.star.xCord) {
            return this;
        }
        return other;
    }

    private Room getTop(Room other) {
        if (this.star.yCord > other.star.yCord) {
            return this;
        }
        return other;
    }

    private Room getBottom(Room other) {
        if (this.star.yCord < other.star.yCord) {
            return this;
        }
        return other;
    }

    public boolean checkOverlap(Room other) {
        return horizontalOverlap(other) && verticalOverlap(other);
    }

    private boolean horizontalOverlap(Room other) {
        Room left = this.getLeft(other);
        Room right = this.getRight(other);
        int edge = left.star.xCord + left.width;
        return edge > right.star.xCord;
    }

    private boolean verticalOverlap(Room other) {
        Room bottom = this.getBottom(other);
        Room top = this.getTop(other);
        int edge = bottom.star.yCord + bottom.height;
        return edge > top.star.yCord;
    }

    @Override
    public int compareTo(Room o) {
        if (this.proximity == o.proximity) {
            return Integer.compare(this.star.xCord, o.star.xCord);
        } else {
            return Integer.compare(this.proximity, o.proximity);
        }
    }
}
