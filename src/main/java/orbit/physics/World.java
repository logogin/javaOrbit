package orbit.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * World.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class World {

    public static final double G = 6.674e-11;
    public static final double SOLAR_MASS = 1.98855e+30;
    public static final double EARTH_MASS = 5.9736e+24;
    public static final double EARTH_DISTANCE = 147098290000.0;
    public static final double EARTH_SPEED = 30300;
    public static final double EARTH_RADIUS = 6371.0;

    public static final int NUMBER_OF_OBJECTS = 500;
    public final List<Celestial> objects = new ArrayList<>(NUMBER_OF_OBJECTS);

    public Celestial findSun() {
        for ( Celestial obj : objects ) {
            if ( obj.name.contains("sun") ) {
                return obj;
            }
        }
        throw new IllegalStateException("The Sun has gone!");
    }

    public void create() {
        Celestial sun = new Celestial(Position.ZERO, SOLAR_MASS, Vector.ZERO, Vector.ZERO, "sun");
        objects.add(sun);
//        Celestial earth = new Celestial(Position.of(EARTH_DISTANCE, 0), EARTH_MASS, Vector.of(0, EARTH_SPEED), Vector.ZERO, "earth");
//        objects.add(earth);
        for ( int i = 0; i < NUMBER_OF_OBJECTS; i++ ) {
            objects.add(randomObject("r" + i));
        }
    }

    public void update(double dt) {
        collide();
        calculateForces();
        accelerate(dt);
        move(dt);
    }

    public List<Celestial> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    private void collide() {
        boolean hasCollisions = false;
        do {
            hasCollisions = false;
            for ( int i = 0; i < objects.size() - 1; i++ ) {
                Celestial o1 = objects.get(i);
                if ( null == o1 ) {
                    continue;
                }
                for ( int j = i + 1; j < objects.size(); j++ ) {
                    Celestial o2 = objects.get(j);
                    if ( null == o2 ) {
                        continue;
                    }
                    if ( collides(o1, o2) ) {
                        hasCollisions = true;
                        o1 = collide(o1, o2);
                        objects.set(j, null);
                    }
                }
                objects.set(i, o1);
            }
        } while (hasCollisions);
        cleanup();
    }

    private void cleanup() {
        List<Celestial> retained = new LinkedList<>();
        for ( Celestial obj : objects ) {
            if ( null != obj ) {
                retained.add(obj);
            }
        }
        objects.clear();
        objects.addAll(retained);
    }

    //private static double min = Double.MAX_VALUE;

    private boolean collides(Celestial o1, Celestial o2) {
        double d = o1.position.distance(o2.position);
//        if ( min > d ) {
//            min = d;
//            System.out.println(min);
//        }
        return d <= EARTH_RADIUS*2;
    }

    private Celestial collide(Celestial o1, Celestial o2) {
        Position p = centerOfMass(o1, o2);
        double m = o1.mass + o2.mass;
        Vector mv1 = o1.velocity.scale(o1.mass);
        Vector mv2 = o2.velocity.scale(o2.mass);
        Vector v = mv1.add(mv2).scale(1/m);
        String n = o1.mass > o2.mass ? o1.name + "." + o2.name : o2.name + "." + o1.name;
        return new Celestial(p, m, v, Vector.ZERO, n);
    }

    private Position centerOfMass(Celestial o1, Celestial o2) {
        double s = o1.mass / (o1.mass + o2.mass);
        Vector uv = Vector.of(o1.position, o2.position).unit();
        Vector d = uv.scale(s);
        return o1.position.move(d);
    }

    private void calculateForces() {
        for ( Celestial obj : objects ) {
            obj.force = accumulateForces(obj);
        }
    }

    private Vector accumulateForces(Celestial o) {
        Vector force = Vector.ZERO;
        for ( Celestial obj : objects ) {
            if ( o != obj ) {
                force = force.add(obj.forceOn(o));
            }
        }
        return force;
    }

    private void accelerate(double dt) {
        for (Celestial obj : objects) {
            obj.accelerate(dt);
        }
    }

    private void move(double dt) {
        for ( Celestial obj : objects ) {
            obj.move(dt);
        }
    }

    private Vector randomVelocity(Position p) {
        Vector direction = Vector.of(p).unit().rotate90();
        if ( Math.random() < 0.5 ) {
            direction = direction.scale(-1);
        }
        //double v = Math.sqrt(1 / p.distance());
        return direction.scale(EARTH_SPEED/2 + rand(EARTH_SPEED));
    }

    private Position randomPosition() {
        double r = EARTH_DISTANCE + rand(EARTH_DISTANCE/2);
        double theta = rand(2 * Math.PI);
        return Position.of(r * Math.cos(theta), r * Math.sin(theta));
    }

    private Celestial randomObject(String name) {
        Position p = randomPosition();
        return new Celestial(p, EARTH_MASS/2 + rand(EARTH_MASS*2), randomVelocity(p), Vector.ZERO, name);
    }

    public double rand(double range) {
        return Math.random() * range;
    }
}
