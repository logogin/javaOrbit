package physics;

/**
 * Vector.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class Vector {

    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector make() {
        return make(0, 0);
    }

    public static Vector make(double x, double y) {
        return new Vector(x, y);
    }

    public static Vector scale(Vector v, double s) {
        return make(v.x * s, v.y * s);
    }

    public static Vector subtract(Position v1, Position v2) {
        return make(v1.x - v2.x, v1.y - v2.y);
    }

    public static Vector unit(Vector v) {
        return scale(v, 1 / magnitude(v));
    }

    public static double magnitude(Vector v) {
        return Math.sqrt(v.x * v.x + v.y * v.y);
    }

    public static Vector rotate90(Vector v) {
        return make(-v.y, v.x);
    }

    public static Vector add(Vector v1, Vector v2) {
        return make(v1.x + v2.x, v1.y + v2.y);
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj instanceof Vector ) {
            Vector v = (Vector)obj;
            return x == v.x && y == v.y;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("vec[x=%f, y=%f]", x, y);
    }

}
