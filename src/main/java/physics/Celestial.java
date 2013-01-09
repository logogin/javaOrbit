package physics;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Celestial.java
 *
 * @created Jan 7, 2013
 * @author Pavel Danchenko
 */
public class Celestial {

    private static final ForkJoinPool pool = new ForkJoinPool();

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

    public static Celestial make(Position position, double mass, Vector velocity, Vector force, String name) {
        return new Celestial(position, mass, velocity, force, name);
    }

    public void updateAll(List<Celestial> world) {
        collideAll(world);
        calculateForcesOnAll(world);
        accelerateAll(world);
        repositionAll(world);
    }

    public void repositionAll(List<Celestial> world) {
        for ( Celestial obj : world ) {
            reposition(obj);
        }
    }

    public void reposition(Celestial o) {
        o.position = Position.add(o.position, o.velocity);

    }

    public void collideAll(List<Celestial> world) {
        boolean hasCollisions = false;
        do {
            hasCollisions = false;
            for ( int i = 0; i < world.size() - 1; i++ ) {
                Celestial o1 = world.get(i);
                if ( null == o1 ) {
                    continue;
                }
                for ( int j = i + 1; j < world.size(); j++ ) {
                    Celestial o2 = world.get(j);
                    if ( null == o2 ) {
                        continue;
                    }
                    if ( collided(o1, o2) ) {
                        hasCollisions = true;
                        o1 = collide(o1, o2);
                        world.set(j, null);
                    }
                }
                world.set(i, o1);
            }
        } while (hasCollisions);
        cleanupWorld(world);
    }

    public void cleanupWorld(List<Celestial> world) {
        Iterator<Celestial> iter = world.iterator();
        while ( iter.hasNext() ) {
            if ( null == iter.next() ) {
                iter.remove();
            }
        }
    }

    public boolean collided(Celestial o1, Celestial o2) {
        return Position.distance(o1.position, o2.position) <= 3;
    }

    public Celestial collide(Celestial o1, Celestial o2) {
        return merge(o1, o2);
    }

    public Celestial merge(Celestial o1, Celestial o2) {
        Position p = centerOfMass(o1, o2);
        double m = o1.mass + o2.mass;
        Vector mv1 = Vector.scale(o1.velocity, o1.mass);
        Vector mv2 = Vector.scale(o2.velocity, o2.mass);
        Vector v = Vector.scale(Vector.add(mv1, mv2), 1/m);
        Vector f = Vector.add(o1.force, o2.force);
        String n = o1.mass > o2.mass ? o1.name + "." + o2.name : o2.name + "." + o1.name;
        return make(p, m, v, f, n);
    }

    public Position centerOfMass(Celestial o1, Celestial o2) {
        double s = o1.mass / (o1.mass + o2.mass);
        Vector uv = Vector.unit(Vector.subtract(o1.position, o2.position));
        Vector d = Vector.scale(uv, s);
        return Position.add(o1.position, d);
    }

    public static Vector forceBetween(Celestial o1, Celestial o2) {
        Position p1 = o1.position;
        Position p2 = o2.position;
        double d = Position.distance(p1, p2);
        Vector uv = Vector.unit(Vector.subtract(p2, p1));
        double g = gravity(o1.mass, o2.mass, d);
        return Vector.scale(uv, g);
    }

    public static double gravity(double m1, double m2, double r) {
        return m1 * m2 / (r * r);
    }

    public static class AccumulateForces extends RecursiveTask<Vector> {
        private final Celestial o;
        private final List<Celestial> world;
        private final int start;
        private final int length;

        public AccumulateForces(Celestial o, List<Celestial> world, int start, int length) {
            this.o = o;
            this.world = world;
            this.start = start;
            this.length = length;
        }

        @Override
        protected Vector compute() {
            if ( length <= 10 ) {
                return accumulateForces(o, world, start, length);
            }
            int split = length / 2;
            AccumulateForces f1 = new AccumulateForces(o, world, start, split);
            AccumulateForces f2 = new AccumulateForces(o, world, start + split, length - split);
            f2.fork();
            return Vector.add(f1.compute(), f2.join());
        }

        public Vector accumulateForces(Celestial o, List<Celestial> world, int start, int length) {
            Vector force = Vector.make();
            for ( int i = start; i < length; i++ ) {
                Celestial obj = world.get(i);
                if ( o != obj ) {
                    force = Vector.add(force, forceBetween(o, obj));
                }
            }
            return force;
        }
    }

    public static void calculateForcesOnAll(List<Celestial> world) {
        for ( Celestial obj : world ) {
            Vector force = pool.invoke(new AccumulateForces(obj, world, 0, world.size()));
            obj.force = force;
            //accumulateForces(obj, world);
        }
    }

    public static void accumulateForces(Celestial o, List<Celestial> world) {
        o.force = Vector.make();
        for ( Celestial obj : world ) {
            if ( o != obj ) {
                o.force = Vector.add(o.force, forceBetween(o, obj));
            }
        }
    }

    public void accelerateAll(List<Celestial> world) {
        for (Celestial obj : world) {
            accelerate(obj);
        }
    }

    public void accelerate(Celestial o) {
        Vector f = o.force;
        double m = o.mass;
        Vector v = o.velocity;
        Vector av = Vector.add(v, Vector.scale(f, 1.0 / m));
        o.velocity = av;
    }

    @Override
    public String toString() {
        return String.format("cel[name=%s, %s]", name, position);
    }
}
