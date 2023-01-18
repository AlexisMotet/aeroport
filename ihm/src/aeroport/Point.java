package aeroport;

public class Point {
    private int x;

    private int y;

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    private int theta;
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }
    public Point(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }
    public Point(double x, double y, int theta) {
        this.x = (int) x;
        this.y = (int) y;
        this.theta = theta;
    }

    @Override
    public String toString() {
        return String.format("Point : {x = %d, y = %d}", this.x, this.y);
    }
}
