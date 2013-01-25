package orbit.physics;

/**
 * Position.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class Position {

    public static final Position ZERO = Position.of(0, 0);

    public double x;
    public double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(double x, double y) {
        return new Position(x, y);
    }

    public double distance() {
        return distance(ZERO);
    }

    public double distance(Position p) {
        double dx = x - p.x;
        double dy = y - p.y;

        return Math.sqrt(dx * dx + dy * dy);
    }

    public Position move(Vector v) {
        return new Position(x + v.x, y + v.y);
    }

    @Override
    public String toString() {
        return String.format("pos[x=%s, y=%s]", x, y);
    }
}
