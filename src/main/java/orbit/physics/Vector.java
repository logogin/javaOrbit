package orbit.physics;

/**
 * Vector.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class Vector {

    public static final Vector ZERO = Vector.of(0, 0);

    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector of(double x, double y) {
        return new Vector(x, y);
    }

    public static Vector of(Position p) {
        return of(Position.ZERO, p);
    }

    public static Vector of(Position start, Position end) {
        return of(end.x - start.x, end.y - start.y);
    }

    public Vector scale(double s) {
        return new Vector(x * s, y * s);
    }

    public Vector unit() {
        return scale(1 / magnitude());
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector rotate90() {
        return new Vector(-y, x);
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    @Override
    public String toString() {
        return String.format("vec[x=%s, y=%s]", x, y);
    }
}
