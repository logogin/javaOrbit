package orbit.physics;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * CelestialTest.java
 *
 * @created Jan 11, 2013
 * @author Pavel Danchenko
 */
@Test
public class CelestialTest {

    public void forceOn() {
        Celestial o1 = new Celestial(Position.ZERO, 1, Vector.ZERO, Vector.ZERO, "o1");
        Celestial o2 = new Celestial(Position.of(1, 0), 1, Vector.ZERO, Vector.ZERO, "o2");

        assertVectorEquals(o1.forceOn(o2), Vector.of(-World.G, 0));
    }

    public void accelerate1() {
        Celestial o1 = new Celestial(Position.ZERO, 1, Vector.ZERO, Vector.ZERO, "o1");
        Celestial o2 = new Celestial(Position.of(1, 0), 1, Vector.ZERO, Vector.ZERO, "o2");

        o1.accelerate(1);
        assertVectorEquals(o1.velocity, Vector.ZERO);

        o1.force = o2.forceOn(o1);
        o1.accelerate(1);
        assertVectorEquals(o1.velocity, Vector.of(World.G, 0));
    }

    public void accelerate2() {
        Celestial o1 = new Celestial(Position.ZERO, 1, Vector.ZERO, Vector.ZERO, "o1");
        Celestial o2 = new Celestial(Position.of(1, 0), 1, Vector.ZERO, Vector.ZERO, "o2");

        o1.force = o2.forceOn(o1);
        o1.accelerate(1);
        assertVectorEquals(o1.velocity, Vector.of(World.G, 0));
    }

    public void accelerate3() {
        Celestial o1 = new Celestial(Position.ZERO, 1, Vector.ZERO, Vector.ZERO, "o1");
        Celestial o2 = new Celestial(Position.of(1, 0), 1, Vector.ZERO, Vector.ZERO, "o2");

        o1.force = o2.forceOn(o1);
        o1.accelerate(2);
        assertVectorEquals(o1.velocity, Vector.of(2 * World.G, 0));
    }

    public void move1() {
        Celestial o1 = new Celestial(Position.ZERO, 1, Vector.ZERO, Vector.ZERO, "o1");
        Celestial o2 = new Celestial(Position.of(1, 0), 1, Vector.ZERO, Vector.ZERO, "o2");

        o1.force = o2.forceOn(o1);
        o1.accelerate(1);
        o1.move(1);
        assertPositionEquals(o1.position, Position.of(World.G, 0));
    }

    private void assertVectorEquals(Vector actual, Vector expected) {
        assertEquals(actual.x, expected.x);
        assertEquals(actual.y, expected.y);
    }

    private void assertPositionEquals(Position actual, Position expected) {
        assertEquals(actual.x, expected.x);
        assertEquals(actual.y, expected.y);
    }
}
