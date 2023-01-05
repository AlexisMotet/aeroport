package aeroport;

public class Point {
    private final int x;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private final int y;
    public Point(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }
}
