
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
import static javax.measure.MetricPrefix.KILO;
import org.junit.jupiter.api.Test;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Power;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Time;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.NumberQuantity;
import tech.units.indriya.quantity.Quantities;
import static tech.units.indriya.quantity.Quantities.*;
import tech.units.indriya.unit.AlternateUnit;
import tech.units.indriya.unit.Units;
import static tech.units.indriya.unit.Units.*;

/**
 *
 * @author rocky
 */
public class UnitsTest {

    public static <T extends Quantity<T>> void myAssertEquals(Quantity<T> exp, Quantity<T> is) {
        assertEquals(exp.getUnit(), is.getUnit());
        assertEquals(exp.getValue().doubleValue(), is.getValue().doubleValue(), 0.0001);
    }

    public static <T extends Quantity<T>> void myAssertEquals(Quantity<T> exp, Quantity<T> is, double maxdiff) {
        assertEquals(exp.getUnit(), is.getUnit());
        assertEquals(exp.getValue().doubleValue(), is.getValue().doubleValue(), maxdiff);
    }

    public static final Unit<Power> KILO_WATT = KILO(WATT);

    @Test
    public void testPercent() {
        System.out.println("testPercent()");
        Quantity<Dimensionless> t = getQuantity(50, PERCENT);
        Quantity<Power> p = getQuantity(10.0, WATT);

        Quantity<Power> e = p.multiply(t).asType(Power.class).to(WATT);

        myAssertEquals(getQuantity(5, WATT), e);
    }

    @Test
    public void testEqualityPrefix1() {
        System.out.println("testEqualityPrefix1");
        Quantity<Mass> q1 = Quantities.getQuantity(45, KILOGRAM);
        Quantity<Mass> q2 = Quantities.getQuantity(45000, GRAM);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertFalse(q1.equals(q2));
    }

    @Test
    public void testEqualityPrefix2() {
        System.out.println("testEqualityPrefix2");
        Quantity<Mass> q1 = Quantities.getQuantity(45, KILO(GRAM));
        Quantity<Mass> q2 = Quantities.getQuantity(45000, GRAM);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertFalse(q1.equals(q2));
    }

    @Test
    public void testEqualityNumberClass() {
        System.out.println("testEqualityNumberClass");
        Quantity<Mass> q1 = Quantities.getQuantity(45, KILOGRAM);
        Quantity<Mass> q2 = Quantities.getQuantity(45.0, KILOGRAM);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertTrue(q1.equals(q2));
    }

    @Test
    public void testEqualityUnitOrder() {
        System.out.println("testEqualityUnitOrder");
        Quantity<Energy> q1 = Quantities.getQuantity(45, NEWTON.multiply(METRE)).asType(Energy.class);
        Quantity<Energy> q2 = Quantities.getQuantity(45, METRE.multiply(NEWTON)).asType(Energy.class);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertTrue(q1.equals(q2));
    }

    @Test
    public void testEqualityUnitAlias() {
        System.out.println("testEqualityUnitAlias");
        Quantity<Pressure> q1 = Quantities.getQuantity(45, PASCAL);
        Quantity<Pressure> q2 = Quantities.getQuantity(45, NEWTON.divide(METRE).divide(METRE)).asType(Pressure.class);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertFalse(q1.equals(q2));
    }

    /**
     * toSystemUnit doesn't mean back to MLT, it means back to the SI unit (eg
     * Watt if it was in KiloWatt). Since PA *IS* an SI unit, it is unchanged.
     */
    @Test
    public void testEqualityUnitAfterConversionToBase() {
        System.out.println("testEqualityUnitAfterConversionToBase");
        Quantity<Pressure> q1 = Quantities.getQuantity(45, PASCAL).toSystemUnit();
        Quantity<Pressure> q2 = Quantities.getQuantity(45, NEWTON.divide(METRE).divide(METRE)).asType(Pressure.class).toSystemUnit();
        System.out.println(q1);
        System.out.println(q2);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertFalse(q1.equals(q2));
    }

    @Test
    public void testEqualityUnitAfterConversionToCommon() {
        System.out.println("testEqualityUnitAfterConversionToCommon");
        Quantity<Pressure> q1 = Quantities.getQuantity(45, PASCAL);
        Quantity<Pressure> q2 = Quantities.getQuantity(45, NEWTON.divide(METRE).divide(METRE)).asType(Pressure.class).to(PASCAL);
        System.out.println(q1);
        System.out.println(q2);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertTrue(q1.equals(q2));
    }

    @Test
    public void testEqualityNotIdentical1() {
        System.out.println("testEqualityNotIdentical1");
        Quantity<Energy> q1 = Quantities.getQuantity(45.6, NEWTON.multiply(METRE)).asType(Energy.class);
        Quantity<Energy> q2 = Quantities.getQuantity(45.6, NEWTON.multiply(METRE)).asType(Energy.class);
        System.out.println(q1);
        System.out.println(q2);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertTrue(q1.equals(q2));
    }

    /**
     * This could fail depending on how numbers are implemented. For indriya it
     * happens to pass.
     */
    @Test
    public void testEqualityNotIdentical2() {
        System.out.println("testEqualityNotIdentical2");
        Quantity<Energy> q1 = Quantities.getQuantity(450000000, NEWTON.multiply(METRE)).asType(Energy.class);
        Quantity<Energy> q2 = Quantities.getQuantity(450000000.000000, NEWTON.multiply(METRE)).asType(Energy.class);
        System.out.println(q1);
        System.out.println(q2);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertTrue(q1.equals(q2));
    }

    @Test
    public void collectionsSortedSet1() {
        System.out.println("collectionsSet1");
        Collection<Quantity<Power>> bag = new HashSet<>();
        bag.add((Quantity) getQuantity(62, WATT));
        bag.add(getQuantity("62000 mW").asType(Power.class));
        // System.out.println(bag);
        assertEquals(2, bag.size()); // based on strict equality
    }

    @Test
    public void collectionsSortedSet2() {
        System.out.println("collectionsSet2");
        Collection<Quantity<Power>> bag = new TreeSet<>();
        bag.add(getQuantity(400, WATT));
        bag.add(getQuantity(0.4, KILO_WATT));
        assertEquals(1, bag.size()); // sorted collections use compareTo
    }

    @Test
    public void arraysSortInAscendingOrder() {
        System.out.println("arraysSortInAscendingOrder");
        Quantity<?> Qhi = getQuantity(3.4, KILO_WATT);
        Quantity<?> Qlo = getQuantity(1.2, WATT);
        Quantity<?>[] arr = {Qhi, Qlo};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
        assertEquals(Qlo, arr[0]);
        assertEquals(Qhi, arr[1]);
    }

    @Test
    public void comparable() {
        ComparableQuantity<Power> q1 = getQuantity(3.4, KILO_WATT);
        ComparableQuantity<Power> q2 = getQuantity(5.2, WATT);
        ComparableQuantity<Power> q3 = getQuantity(4.0, KILO_WATT);
        ComparableQuantity<Power> q6 = getQuantity(4.0, KILO(WATT));
        ComparableQuantity<Power> q4 = getQuantity(4000, WATT);
        ComparableQuantity<Temperature> q5 = getQuantity(273, KELVIN);
        ComparableQuantity<Mass> q7 = getQuantity(4000, GRAM);
        ComparableQuantity<Mass> q8 = getQuantity(4, KILOGRAM);

        assertEquals(q1.compareTo(q2), 1);
        assertEquals(q3.compareTo(q4), 0);
        assertEquals(q3.compareTo(q6), 0);
        assertEquals(q4.compareTo(q6), 0);
        assertEquals(q7.compareTo(q8), 0);
        assertEquals(q2.compareTo(q4), -1);
    }

    @Test
    public void showEqualsIsNotCompatibleWithCompareTo() {
        System.out.println("showEqualsIsNotCompatibleWithCompareTo");
        ComparableQuantity<Pressure> q1 = Quantities.getQuantity(45, PASCAL);
        ComparableQuantity<Pressure> q2 = Quantities.getQuantity(45, NEWTON.divide(METRE).divide(METRE)).asType(Pressure.class);

        assertFalse(q1.equals(q2)); // not equal

        assertEquals(q1.compareTo(q2), 0); // equal

        assertTrue(q1.isEquivalentTo(q2));
        assertTrue(q1.getValue().equals(q2.getValue()));
        assertFalse(q1.getUnit().equals(q2.getUnit()));
    }

    public interface Bmi extends Quantity<Bmi> {
    }

    @Test
    public void testCustomTypePreventsErrors() {
        System.out.println("testCustomTypePreventsErrors");

        // sanity
        assertThrows(ClassCastException.class, () -> {
            Quantity<Length> len1 = Quantities.getQuantity(3000, KILOGRAM).asType(Length.class);
        });

        // why not the same as length?
        final Unit<Bmi> x = new AlternateUnit<Bmi>(Units.KILOGRAM.divide(Units.METRE.pow(2)), "BMI");

        assertThrows(ClassCastException.class, () -> {
            Quantity<Bmi> x1 = Quantities.getQuantity(3000, METRE).asType(Bmi.class);
        });
    }

    public interface Xordinate extends Quantity<Xordinate> {
    }

    public interface Yordinate extends Quantity<Yordinate> {
    }
    public static final Unit<Xordinate> XUnit = new AlternateUnit<Xordinate>((Units.METRE), "X");

    public static final Unit<Yordinate> YUnit = new AlternateUnit<Yordinate>((Units.METRE), "Y");

    @Test
    public void differentUnitsConsideredUnEqualEvenIfDimsSame() {
        System.out.println("differentUnitsConsideredUnEqualEvenIfDimsSame()");
        Quantity<Xordinate> x1 = Quantities.getQuantity(10, XUnit);
        Quantity<Yordinate> y1 = Quantities.getQuantity(10, YUnit);
        //assertTrue(x1.isEquivalentTo(y1));

        // Different units so should be unequal
        assertFalse(x1.equals(y1));
    }

    @Test
    public void testMathException() {
        System.out.println("testMathException()");

        // DIV 0
        assertThrows(IllegalArgumentException.class, () -> {
            getQuantity(0.0, WATT).inverse();
        });
        assertThrows(IllegalArgumentException.class, () -> {
            getQuantity(0, WATT).inverse();
        });
        assertThrows(IllegalArgumentException.class, () -> {
            getQuantity(10, WATT).divide(getQuantity(0, WATT));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            getQuantity(10, WATT).divide(0);
        });

        // NaN
        assertThrows(IllegalArgumentException.class, () -> {
            getQuantity(Double.NaN, WATT);
        });

        // double Overflow: intermediate ops can exceed MAX double, but casting it back to double gives INF
        //assertThrows(IllegalArgumentException.class, () -> {
        Quantity q = getQuantity(Double.MAX_VALUE, WATT).multiply(getQuantity(Double.MAX_VALUE, WATT));
        double d = q.getValue().doubleValue();
        assertTrue(Double.isInfinite(d));

        // sum brings it back into range
        q = q.divide(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, q.getValue().doubleValue());

        // infinity
        assertThrows(IllegalArgumentException.class, () -> {
            getQuantity(Double.POSITIVE_INFINITY, WATT);
        });
    }

    @Test
    public void testNegateWorksOn0() {
        System.out.println("testNegateWorksOn0()");
        Quantity q = getQuantity(0.0, WATT);
        q = q.negate();
        System.out.println(q);
    }

    @Test
    public void testSerialsationOfQuantity() throws IOException, ClassNotFoundException {
        System.out.println("testSerialsationOfQuantity()");

        int sample = 1000;
        ComparableQuantity[] instance = new ComparableQuantity[sample];
        for (int i = 0; i < sample; i++) {
            instance[i] = Quantities.getQuantity(1000.1, WATT);
        }
        byte[] bytes = SerialisationHelper.tobytes(instance);

        System.out.println("Size=" + bytes.length);
        assertEquals(21 * sample + 1361, bytes.length);

        ComparableQuantity[] result = SerialisationHelper.frombytes(bytes, instance.getClass());
        assertEquals(instance[30], result[30]);
    }
}
