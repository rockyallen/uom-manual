
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;
import static javax.measure.MetricPrefix.KILO;
import static javax.measure.MetricPrefix.MEGA;
import static javax.measure.MetricPrefix.MILLI;
import org.junit.jupiter.api.Test;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Power;
import javax.measure.quantity.Pressure;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Time;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.format.SimpleQuantityFormat;
import tech.units.indriya.quantity.Quantities;
import static tech.units.indriya.quantity.Quantities.*;
import static tech.units.indriya.unit.Units.*;

/**
 *
 * @author rocky
 */
public class UnitsTest {

    public static final Unit<Power> KILO_WATT = KILO(WATT);

    @Test
    public void testPower() {
        System.out.println("testPower()");
        Quantity<Time> t = getQuantity(0.5, HOUR);
        Quantity<Power> p = getQuantity(10.0, WATT);

        Quantity<?> e = p.multiply(t);

        Quantity<Energy> e1 = p.multiply(t).asType(Energy.class);

        System.out.println(e1);
    }

    @Test
    public void testPercent() {
        System.out.println("testPercent()");
        Quantity<Dimensionless> t = getQuantity(50, PERCENT);
        Quantity<Power> p = getQuantity(10.0, WATT);

        Quantity<Power> e = p.multiply(t).asType(Power.class).to(WATT);

        myAssertEquals(getQuantity(5, WATT), e);
    }

    public static <T extends Quantity<T>> void myAssertEquals(Quantity<T> exp, Quantity<T> is) {
        assertEquals(exp.getUnit(), is.getUnit());
        assertEquals(exp.getValue().doubleValue(), is.getValue().doubleValue(), 0.0001);
    }

    public static <T extends Quantity<T>> void myAssertEquals(Quantity<T> exp, Quantity<T> is, double maxdiff) {
        assertEquals(exp.getUnit(), is.getUnit());
        assertEquals(exp.getValue().doubleValue(), is.getValue().doubleValue(), maxdiff);
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

    @Test
    public void testEqualityUnitAliasWithCast() {
        System.out.println("testEqualityUnitAliasWithCast");
        Quantity<Pressure> q1 = Quantities.getQuantity(45, PASCAL).asType(Pressure.class);
        Quantity<Pressure> q2 = Quantities.getQuantity(45, NEWTON.divide(METRE).divide(METRE)).asType(Pressure.class);
        System.out.println(q1.getValue().equals(q2.getValue()) + ":" + q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertFalse(q1.equals(q2));
    }

    /**
     * Base doesn't mean back to MLT, it means back to the SI unit (eg Watt if
     * it was in KiloWatt). Since PA *IS* an SI unit, it is unchanged.
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
        Collection<Quantity<Power>> bag = new TreeSet<>();
        bag.add((Quantity) getQuantity(62, WATT)); // Why is this not an error?        
        bag.add(getQuantity("100 W").asType(Power.class)); // Why is this not an error?        
        System.out.println(bag); // [1.2 W, 62 W, 3.4 kW, 4000 W]
        assertEquals(2, bag.size()); // based on strict equality
    }

    @Test
    public void collectionsSortedSet2() {
        System.out.println("collectionsSet2");
        Collection<Quantity<Power>> bag = new TreeSet<>();
        bag.add(getQuantity(400, WATT));
        bag.add(getQuantity(0.4, KILO_WATT));
        System.out.println(bag); // [1.2 W, 62 W, 3.4 kW, 4000 W]
        assertEquals(1, bag.size()); // Equals appears to be compring the bse unit
    }

    @Test
    public void collectionsSortedSet3() {
        System.out.println("collectionsSet3");
        Collection<Quantity<Mass>> bag = new TreeSet<>();
        bag.add(getQuantity(400, GRAM));
        bag.add(getQuantity(0.4, KILOGRAM));
        System.out.println(bag); // [1.2 W, 62 W, 3.4 kW, 4000 W]
        assertEquals(1, bag.size()); // surprising?
    }

//    @Test
//    public void collectionsSortedSet2() {
//        System.out.println("collectionsSet2");
//        Collection<Quantity<Power>> bag = new TreeSet<>();
//        bag.add(getQuantity(3.4, KILO_WATT));
//        bag.add(getQuantity(1.2, WATT));
//        bag.add(getQuantity(400, WATT));
//        bag.add(getQuantity(0.4, KILO_WATT));
//        bag.add((Quantity) getQuantity(62, WATT)); // Why is this not an error?        
//        System.out.println(bag); // [1.2 W, 62 W, 3.4 kW, 4000 W]
//        assertEquals(5, bag.size()); // based on strict equality
//    }
//
//    @Test
//    public void collectionsSortedSet2() {
//        System.out.println("collectionsSet2");
//        Collection<Quantity<Power>> bag = new TreeSet<>();
//        bag.add(getQuantity(3.4, KILO_WATT));
//        bag.add(getQuantity(1.2, WATT));
//        bag.add(getQuantity(400, WATT));
//        bag.add(getQuantity(0.4, KILO_WATT));
//        bag.add((Quantity) getQuantity(62, WATT)); // Why is this not an error?        
//        System.out.println(bag); // [1.2 W, 62 W, 3.4 kW, 4000 W]
//        assertEquals(5, bag.size()); // based on strict equality
//    }
    @Test
    public void collectionsList1() {
        System.out.println("collectionsList1");
        Collection<Quantity<Power>> bag = new ArrayList<>();
        bag.add(getQuantity(3.4, KILO_WATT));
        bag.add(getQuantity(1.2, WATT));
        bag.add(getQuantity(400, WATT));
        bag.add(getQuantity(0.4, KILO_WATT));
        bag.add(getQuantity(700, WATT));
        bag.add(getQuantity(0.7, KILO(WATT)));
        bag.add((Quantity) getQuantity(62, WATT)); // Why is this not an error?
        System.out.println(bag);
        assertEquals(7, bag.size());
        Object[] arr = bag.toArray();
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr)); // [3.4 kW, 1.2 W, 400 W, 0.4 kW, 700 W, 0.7 kW, 62 W]
    }

    @Test
    public void collectionsList2() {
        System.out.println("collectionsList2");
        Collection<Quantity<Power>> bag = new ArrayList<>();
        bag.add(getQuantity(3.4, KILO_WATT));
        bag.add(getQuantity(1.2, WATT));
        bag.add(getQuantity(0.4, KILO_WATT));
        bag.add(getQuantity(400, WATT));
        bag.add(getQuantity(0.7, KILO(WATT)));
        bag.add(getQuantity(700, WATT));
        bag.add((Quantity) getQuantity(62, WATT)); // Why is this not an error?
        System.out.println(bag);
        assertEquals(7, bag.size());
        Object[] arr = bag.toArray();
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr)); // [3.4 kW, 1.2 W, 0.4 kW, 400 W, 0.7 kW, 700 W, 62 W]

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

        assertEquals(q1.compareTo(q2), 1); // 1
        assertEquals(q3.compareTo(q4), 0); // 0
        assertEquals(q3.compareTo(q6), 0); // 0
        assertEquals(q4.compareTo(q6), 0); // 0
        assertEquals(q7.compareTo(q8), 0); // 0
        assertEquals(q2.compareTo(q4), -1); // -1
    }

    @Test
    public void equalsIsNotCompatibleWithCompareTo() {
        System.out.println("equalsIsNotCompatibleWithCompareTo");
        ComparableQuantity<Pressure> q1 = Quantities.getQuantity(45, PASCAL);
        ComparableQuantity<Pressure> q2 = Quantities.getQuantity(45, NEWTON.divide(METRE).divide(METRE)).asType(Pressure.class);
        assertTrue(q1.getValue().equals(q2.getValue()));
        assertFalse(q1.getUnit().equals(q2.getUnit()));
        assertTrue(q1.isEquivalentTo(q2));
        assertEquals(q1.compareTo(q2), 0);
        assertFalse(q1.equals(q2));
    }

    /**
     * Letter	Quantity Component	Presentation	Examples
     *
     * n	Numeric value	Number	27
     *
     * u	Unit	Text	m
     *
     * ~	Mixed radix	Text	1 m; 27 cm
     *
     */
    @Test
    public void testParse() {
        System.out.println("testParse");
        System.out.println("|Case |Input |Output\n");
        formatHelper("Simple", "6 m", null, 6, METRE, "6 m");
        formatHelper("Extra space", "6       m", null, 6, METRE, "6 m");
        formatHelper("Prefix", "28.95 mm", null, 28.95, MILLI(METRE), "28.95 mm");
        formatHelper("Prefix", "15.6 MN", null, 15.6, MEGA(NEWTON), "15.6 MN");
        formatHelper("Prefix", "28.95 mm", null, 28.95, MILLI(METRE), "28.95 mm");
        formatHelper("Prefix", "11 kV", null, 11, KILO(VOLT), "11 kV");
        formatHelper("Rational", "-5÷3 m", null, -5.0 / 3.0, METRE, "-1.666666666666666666666666666666667 m");
        formatHelper("Compound", "11 N·m", null, 11, NEWTON.multiply(METRE), "11 N·m");
        formatHelper("Compound", "11 m·N", null, 11, METRE.multiply(NEWTON), "11 m·N");
        formatHelper("Compound", "6 m/s", null, 6, METRE.divide(SECOND), "6 m");
        //formatHelper("Powers", "6 m/s/s", null, 6, METRE.divide(SECOND).divide(SECOND), "6 m/s2");
        //formatHelper("Powers", "6 m/s2", null, 6, METRE.divide(SECOND).divide(SECOND), "6 m/s2");
        formatHelper("Powers", "1013 kg/m·m·m", null, 1013, KILOGRAM.divide(METRE).divide(METRE).divide(METRE), "6 kg/m3");
        formatHelper("Powers", "1013 kg/m³", null, 1013, KILOGRAM.divide(METRE).divide(METRE).divide(METRE), "6 kg/m3");
        //formatHelper("Scientific", "6 m·s-2", null, 6, METRE.divide(SECOND).divide(SECOND), "6 m/s2");
        //formatHelper("Compound", "6 ONE/s", null, 6, HERTZ, "6 Hz");
        formatHelper("Compound", "6 1/s", null, 6, HERTZ, "6 Hz");
	

        //formatHelper("Space not dot", "11 m N", null, 11, METRE.multiply(NEWTON), "11 m·N");

        formatHelper("Mixed radix", "11 mo; 3 day 2 hr", "n u ~;", 0, null, "11 mol; 3 day 2 hr");

//        formatHelper("28.95 mm", "n u ~;", 28.95, MILLI(METRE), "28.95 mm");
//        formatHelper("28.95 mm", "n u", 28.95, MILLI(METRE), "28.95 mm");
//        formatHelper("1 m; 27 cm", "n  u ~; ", 0, null, "1 m; 27 cm");
//        formatHelper("28.95 mm", "nnnnnn u", 28.95, MILLI(METRE), "28.95 mm");
//        formatHelper("28.95 cm", "n u", 28.95, CENTI(METRE), "cm 28.95");
//        formatHelper("1 min", "n u", 1, MINUTE, "1 min");
//        formatHelper("30 s", "n u", 30, SECOND, "30 s");
//        formatHelper("1 min; 30 s", "n u ~;", 28.95, MILLI(METRE), "1 min; 30 s");
//        formatHelper("28.95_mm", "n_u", 28.95, MILLI(METRE), "28.95_mm");
    }

    public void formatHelper(String caseLabel, String input, String fmt, double expectVal, Unit expectUnit, String expectOut) {
        SimpleQuantityFormat f = (fmt == null ? SimpleQuantityFormat.getInstance() : SimpleQuantityFormat.getInstance(fmt));

        Quantity expectedQuantity = null;
        Quantity parsed = null;
        Quantity reparsed = null;
        boolean equivalent = false;
        boolean equal = false;
        String restringed = null;
        Exception eParse = null;
        Exception eReparse = null;

        try {
            parsed = f.parse(input);
            if (expectUnit != null) {
                expectedQuantity = Quantities.getQuantity(expectVal, expectUnit);
                equivalent = expectedQuantity.isEquivalentTo(parsed);
                equal = expectedQuantity.equals(parsed);
            }

            try {
                restringed = parsed.toString();
                reparsed = f.parse(restringed);
            } catch (Exception ex) {
                eReparse = ex;
            }
        } catch (Exception ex) {
            eParse = ex;
        }

        System.out.println("// exp=" + expectedQuantity + " equal=" + equal + " equivalent=" + equivalent + " reparse="+reparsed);
                
        System.out.println("|" + caseLabel);
        System.out.println("|[`" + input + "`]");
        if (eParse == null) {
            System.out.println("|" + parsed);
        } else {
            System.out.println("|" + eParse);
        }
        
//        if (eReparse == null) {
//            System.out.println("|" + (reparsed == null ? "NA" : reparsed));
//        } else {
//            System.out.println("|" + eParse);
//        }
    }

    @Test
    public void testdelim() {
        System.out.println("testDelim");
        SimpleQuantityFormat("n u");
        SimpleQuantityFormat("n*u");
        SimpleQuantityFormat("n_u");
        SimpleQuantityFormat("n u ~;");
        SimpleQuantityFormat("n u ~ ");
    }

    public void SimpleQuantityFormat(String pattern) {
        final String NUM_PART = "n";
        final String UNIT_PART = "u";
        final String RADIX = "~";

        /**
         * The pattern string of this formatter. This is always a non-localized
         * pattern. May not be null. See class documentation for details.
         *
         * @serial
         */
        String delimiter = null;

        String mixDelimiter = null;

        if (pattern != null && !pattern.isEmpty()) {
            if (pattern.contains(RADIX)) {
                final String singlePattern = pattern.substring(0, pattern.indexOf(RADIX));
                mixDelimiter = pattern.substring(pattern.indexOf(RADIX) + 1);
                delimiter = singlePattern.substring(pattern.indexOf(NUM_PART) + 1, pattern.indexOf(UNIT_PART));
            } else {
                delimiter = pattern.substring(pattern.indexOf(NUM_PART) + 1, pattern.indexOf(UNIT_PART));
            }
        }
        System.out.println("pattern='" + pattern + "' delim='" + delimiter + "' mix='" + mixDelimiter + "'");
    }
}
