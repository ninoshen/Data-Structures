package byow.Core;

import byow.TileEngine.TETile;

public class Hallway {
    private final Mark m1;
    private final Mark m2;
    private final Mark corner;

    public Hallway(Mark mark1, Mark mark2) {
        m1 = mark1;
        m2 = mark2;
        int magic = DisneyFactory.fairy.nextInt(2);
        if (magic == 0) {
            corner = new Mark(m1.xCord, m2.yCord);
        } else {
            corner = new Mark(m2.xCord, m1.yCord);
        }
    }

    public void buildWalls(TETile[][] ground, TETile pattern) {
        if (corner.isSame(m1) || corner.isSame(m2)) {
            twoPointsWall(m1, m2, ground, pattern);
        }
        twoPointsWall(m1, corner, ground, pattern);
        twoPointsWall(m2, corner, ground, pattern);
    }

    public void buildFloors(TETile[][] ground, TETile pattern) {
        if (corner.isSame(m1) || corner.isSame(m2)) {
            twoPointsFloor(m1, m2, ground, pattern);
        }
        twoPointsFloor(m1, corner, ground, pattern);
        twoPointsFloor(m2, corner, ground, pattern);

    }

    public void twoPointsWall(Mark m1, Mark m2, TETile[][] ground, TETile pattern) {
        if (m1.sameRow(m2)) {
            if (m1.compareTo(m2) < 0) {
                horizontalWall(m1, m2, ground, pattern);
            } else {
                horizontalWall(m2, m1, ground, pattern);
            }
        } else {
            if (m1.compareTo(m2) < 0) {
                verticalWall(m1, m2, ground, pattern);
            } else {
                verticalWall(m2, m1, ground, pattern);
            }
        }
    }

    public void twoPointsFloor(Mark m1, Mark m2, TETile[][] ground, TETile pattern) {
        if (m1.sameRow(m2)) {
            if (m1.compareTo(m2) < 0) {
                horizontalFloor(m1, m2, ground, pattern);
            } else {
                horizontalFloor(m2, m1, ground, pattern);
            }
        } else {
            if (m1.compareTo(m2) < 0) {
                verticalFloor(m1, m2, ground, pattern);
            } else {
                verticalFloor(m2, m1, ground, pattern);
            }
        }
    }


    public void horizontalWall(Mark left, Mark right, TETile[][] ground, TETile pattern) {
        for (int x = left.xCord; x <= right.xCord; x++) {
            ground[x][left.yCord + 1] = pattern;
            ground[x][left.yCord - 1] = pattern;
        }
    }

    public void horizontalFloor(Mark left, Mark right, TETile[][] ground, TETile pattern) {
        for (int x = left.xCord; x <= right.xCord; x++) {
            ground[x][left.yCord] = pattern;
        }
    }

    public void verticalWall(Mark bottom, Mark top, TETile[][] ground, TETile pattern) {
        for (int y = bottom.yCord; y <= top.yCord; y++) {
            ground[bottom.xCord - 1][y] = pattern;
            ground[bottom.xCord + 1][y] = pattern;
        }
    }

    public void verticalFloor(Mark bottom, Mark top, TETile[][] ground, TETile pattern) {
        for (int y = bottom.yCord; y <= top.yCord; y++) {
            ground[bottom.xCord][y] = pattern;
        }
    }
}
