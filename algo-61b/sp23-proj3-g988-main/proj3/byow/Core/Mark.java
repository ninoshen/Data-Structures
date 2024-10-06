package byow.Core;


public class Mark implements Comparable<Mark> {
    public int xCord;
    public int yCord;

    public Mark(int x, int y) {
        this.xCord = x;
        this.yCord = y;
    }

    public int compareV(Mark other) {
        return this.yCord - other.yCord;
    }

    public int compareH(Mark other) {
        return this.xCord - other.xCord;
    }

    public boolean isSame(Mark other) {
        return compareV(other) == 0 && compareH(other) == 0;
    }

    public boolean sameRow(Mark other) {
        return compareV(other) == 0;
    }

    public boolean sameColumn(Mark other) {
        return compareH(other) == 0;
    }

    @Override
    public int compareTo(Mark o) {
        if (this.xCord == o.xCord) {
            return this.yCord - o.yCord;
        } else if (this.yCord == o.yCord) {
            return this.xCord - o.xCord;
        } else {
            return this.xCord + this.yCord - o.xCord - o.yCord;
        }
    }
}
