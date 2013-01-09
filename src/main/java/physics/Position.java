package physics;

/**
 * Position.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class Position {

    public double x;
    public double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double distance(Position p, Position q) {
        double x = p.x - q.x;
        double y = p.y - q.y;

        return Math.sqrt(x * x + y * y);
    }

    public static Position make(double x, double y) {
        return new Position(x, y);
    }

    public static Position add(Position p, Vector q) {
        return make(p.x + q.x, p.y + q.y);
    }

    @Override
    public String toString() {
        return String.format("pos[x=%f, y=%f]", x, y);
    }
}
