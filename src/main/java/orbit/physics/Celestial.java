package orbit.physics;

/**
 * Celestial.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class Celestial {

    public double mass;
    public Position position;
    public Vector velocity;
    public Vector force;
    public String name;

    public Celestial(Position position, double mass, Vector velocity, Vector force, String name) {
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.force = force;
        this.name = name;
    }

    public static Celestial of(Position position, double mass, Vector velocity, Vector force, String name) {
        return new Celestial(position, mass, velocity, force, name);
    }

    public void accelerate(double dt) {
        velocity = velocity.add(force.scale(1.0 / mass).scale(dt));
    }

    public void move(double dt) {
        position = position.move(velocity.scale(dt));
    }

    public Vector forceOn(Celestial obj) {
        double d = position.distance(obj.position);
        Vector uv = Vector.of(obj.position, position).unit();
        double g = gravity(mass, obj.mass, d);
        return uv.scale(g);
    }

    public double gravity(double m1, double m2, double r) {
        return World.G * m1 * m2 / (r * r);
    }

    @Override
    public String toString() {
        return String.format("cel[name=%s, %s]", name, position);
    }
}
