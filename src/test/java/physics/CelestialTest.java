package physics;

import static org.testng.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * CelestialTest.java
 *
 * @created Jan 8, 2013
 * @author Pavel Danchenko
 */
@Test
public class CelestialTest {

    private Vector v0 = Vector.make();
    private Celestial o1 = Celestial.make(Position.make(1, 1), 2, v0, v0, "o1");
    private Celestial o2 = Celestial.make(Position.make(1, 2), 3, v0, v0, "o2");
    private Celestial o3 = Celestial.make(Position.make(4, 5), 4, v0, v0, "o3");
    private List<Celestial> os;

    @BeforeMethod
    public void beforeMethod() {
        v0 = Vector.make();
        o1 = Celestial.make(Position.make(1, 1), 2, v0, v0, "o1");
        o2 = Celestial.make(Position.make(1, 2), 3, v0, v0, "o2");
        o3 = Celestial.make(Position.make(4, 5), 4, v0, v0, "o3");
        os = new LinkedList<>();
        os.add(o1);
        os.add(o2);
        os.add(o3);
    }

    public void gravity() {
        assertEquals(Celestial.gravity(2, 3, 4), 6.0/16.0);
    }

    public void forceBetween() {
        double c3r2 = 3 / Math.sqrt(2);
        Celestial o1 = Celestial.make(Position.make(1, 1), 2, v0, v0, "o1");
        Celestial o2 = Celestial.make(Position.make(2, 2), 3, v0, v0, "o2");

        assertEquals(Celestial.forceBetween(o1, o2), Vector.make(c3r2, c3r2));
    }

    public void accumulateForces() {
        Vector expected = Vector.add(Celestial.forceBetween(o1, o2), Celestial.forceBetween(o1, o3));

        Celestial.accumulateForces(o1, os);
        assertEquals(o1.force, expected);
    }

    public void calculateForcesOnAll() {
        Vector f1 = Vector.add(Celestial.forceBetween(o1, o2), Celestial.forceBetween(o1, o3));
        Vector f2 = Vector.add(Celestial.forceBetween(o2, o1), Celestial.forceBetween(o2, o3));
        Vector f3 = Vector.add(Celestial.forceBetween(o3, o1), Celestial.forceBetween(o3, o2));

        Celestial.calculateForcesOnAll(os);
        assertEquals(o1.force, f1);
        assertEquals(o2.force, f2);
        assertEquals(o3.force, f3);
    }
}
