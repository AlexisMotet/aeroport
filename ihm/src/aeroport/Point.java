package aeroport;

public class Point {
    private final int x;
    private final int y;
    private int theta;
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Point(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }
    @Override
    public String toString() {
        return String.format("Point : {x = %d, y = %d}", this.x, this.y);
    }
}
