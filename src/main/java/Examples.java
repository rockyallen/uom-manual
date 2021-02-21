
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.measure.Dimension;
import javax.measure.MetricPrefix;
import static javax.measure.MetricPrefix.KILO;
import javax.measure.UnitConverter;
import javax.measure.quantity.Area;
import javax.measure.quantity.Energy;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Pressure;
import javax.measure.spi.SystemOfUnits;
import tech.units.indriya.format.SimpleUnitFormat;

import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.NEWTON;
import static tech.units.indriya.unit.Units.CELSIUS;

//import systems.uom.common.Imperial;
//tag::import[]
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.QuantityFormat;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Force;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Power;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Time;
import static tech.units.indriya.AbstractUnit.ONE;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.format.SimpleQuantityFormat;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.quantity.Quantities;
import static tech.units.indriya.quantity.Quantities.getQuantity;
import tech.units.indriya.unit.AlternateUnit;
import tech.units.indriya.unit.TransformedUnit;
import tech.units.indriya.unit.Units;
import static tech.units.indriya.unit.Units.GRAM;
import static tech.units.indriya.unit.Units.HOUR;
import static tech.units.indriya.unit.Units.JOULE;
import static tech.units.indriya.unit.Units.KELVIN;
import static tech.units.indriya.unit.Units.RADIAN;
import static tech.units.indriya.unit.Units.SECOND;
import static tech.units.indriya.unit.Units.SQUARE_METRE;
import static tech.units.indriya.unit.Units.WATT;
import uom.bm.ObjectSizeFetcher;
//end::import[]

public class Examples {

//tag::constants[]
    public static final Unit<Angle> DEGREE_ANGLE = new TransformedUnit<>("\u00b0", RADIAN, MultiplyConverter.of(180.0 / Math.PI));
    public static final Unit<?> IRRADIANCE = WATT.divide(SQUARE_METRE);
    public static final Unit<Power> KILO_WATT = KILO(WATT);
    public static final Unit<Energy> KILO_WATT_HOUR = KILO(WATT.multiply(HOUR)).asType(Energy.class);

    public static final Quantity<Time> ZERO_TIME = Quantities.getQuantity(0.0, SECOND);
    public static final Quantity<Energy> ZERO_ENERGY = Quantities.getQuantity(0.0, JOULE);
    public static final Quantity<Power> ZERO_POWER = Quantities.getQuantity(0.0, WATT);
    public static final Quantity<Dimensionless> ZERO_NUMBER = Quantities.getQuantity(0.0, ONE);

    public static final Quantity<?> STANDARD_IRRADIANCE = Quantities.getQuantity(1000.0, IRRADIANCE);
//end::constants[]

    public static void main(String[] args) {
        Examples examples = new Examples();
        examples.run();
    }

    public void run() {
        System.out.println("\nalternateUnit1()");
        alternateUnit1();
        System.out.println("\nalternateUnit2()");
        alternateUnit2();
        System.out.println("\nproductUnit()");
        productUnit();
        System.out.println("\nunitConverter()");
        unitConverter();
        System.out.println("\nprintUnits()");
        printUnits();
        System.out.println("\ngivenUnits_WhenAdd_ThenSuccess()");
        givenUnits_WhenAdd_ThenSuccess();
        System.out.println("\nmixedCompatibleUnits()");
        mixedCompatibleUnits();
        System.out.println("\nsiprefixes()");
        siprefixes();
        System.out.println("\nuseCentripetalForce()");
        useCentripetalForce();
        System.out.println("\nquantitygetters()");
        quantitygetters();
        System.out.println("\nsi()");
        si();
        System.out.println("\nsimpleparse()");
        simpleparse();
        System.out.println("\nsimpleAsciiParse()");
        simpleAsciiParse();
        System.out.println("\nmultiplyresult()");
        multiplyresult();
        System.out.println("\ncollections()");
        collections();
        System.out.println("\ncomparable()");
        comparable();
        System.out.println("\nSIZE()");
        size();
        System.out.println("\ntemperatureSums()");
        temperatureSums();
        System.out.println("\ntext()");
        text();
//        System.out.println("\nnewQuantityType()");
//        newQuantityType();
        System.out.println("\nqtytypetest()");
        qtytypetest();
        System.out.println("\nasTypeIsRubbish()");
        asTypeIsRuntimeChecks();
        System.out.println("\nbmiTest()");
        BodyMassIndex.main(new String[]{});

    }

// tag::useCentripetalForce[]
    public void useCentripetalForce() {
        Quantity<Force> f
                = centripetalForce(getQuantity(3.5, Units.KILOGRAM),
                        getQuantity(25.7, Units.METRE_PER_SECOND),
                        getQuantity(30, Units.METRE));
        System.out.println(f); // 77.05716666666666 N
    }
// end::useCentripetalForce[]

// tag::einstein[]
    public static Quantity<Energy> einstein(Quantity<Mass> mass, Quantity<Speed> speedoflight) {
        double c = speedoflight.to(Units.METRE_PER_SECOND).getValue().doubleValue();
        double m = mass.to(Units.KILOGRAM).getValue().doubleValue();
        return getQuantity(m * c * c, Units.JOULE);
    }
// end::einstein[]

// tag::centripetalForce[]
    public static Quantity<Force> centripetalForce(Quantity<Mass> m, Quantity<Speed> v, Quantity<Length> r) {
        double f = m.to(Units.KILOGRAM)
                .multiply(v.to(Units.METRE_PER_SECOND))
                .multiply(v.to(Units.METRE_PER_SECOND))
                .divide(r.to(METRE)).getValue().doubleValue();
        return Quantities.getQuantity(f, Units.NEWTON);
    }
// end::centripetalForce[]

    public void alternateUnit1() {
// tag::alternateUnit[]
        Unit<Pressure> PASCAL = NEWTON.divide(METRE.pow(2))
                .alternate("Pa").asType(Pressure.class);
        assert SimpleUnitFormat.getInstance().parse("Pa").equals(PASCAL);
// end::alternateUnit[]
    }

    // how is this different from the above?
    public void alternateUnit2() {
// tag::alternateUnit[]
        Unit<Pressure> PASCAL = new AlternateUnit<Pressure>(Units.NEWTON.divide(Units.METRE.pow(2)), "Pa");
// end::alternateUnit[]
    }

//Similarly, an example of ProductUnit and its conversion:
//@Test
    public void productUnit() {
// tag::productUnit[]
        Unit<Area> squareMetre = METRE.multiply(METRE).asType(Area.class);
        Quantity<Length> line = Quantities.getQuantity(2, METRE);
        System.out.println(line.multiply(line).getUnit() == squareMetre); // true
// end::productUnit[]
    }

    public void unitConverter() {
// tag::converter[]
        UnitConverter metreToKilometre = METRE.getConverterTo(MetricPrefix.KILO(METRE));

        System.out.println(metreToKilometre.convert(52.5)); // 0.0525
        System.out.println(metreToKilometre.convert(3000)); // 3
// end::converter[]
    }

    private void printUnits() {
        SystemOfUnits su;
    }

    public void givenUnits_WhenAdd_ThenSuccess() {
        // tag::givenUnits_WhenAdd_ThenSuccess[]
        Quantity<Length> l1 = Quantities.getQuantity(2, METRE);
        Quantity<Length> l2 = Quantities.getQuantity(3, METRE);
        Quantity<Length> sum = l1.add(l2); // 5 m
        System.out.println(sum);
        // end::givenUnits_WhenAdd_ThenSuccess[]
    }

    public void mixedCompatibleUnits() {
        // tag::mixedCompatibleUnits[]
        Unit<Length> KILO_METRE = MetricPrefix.KILO(Units.METRE);
        Quantity<Length> l1 = Quantities.getQuantity(200, METRE);
        Quantity<Length> l2 = Quantities.getQuantity(3, KILO_METRE);
        Quantity<Length> sum1 = l1.add(l2); // 3200 m
        Quantity<Length> sum2 = l2.add(l1); // 3.2 km
        // end::mixedCompatibleUnits[]
        System.out.println(sum1);
        System.out.println(sum2);
    }

    public void multiplyresult() {
        // tag::multiplyresult[]
        Unit<Length> KILO_METRE = MetricPrefix.KILO(Units.METRE);
        Unit<?> WorkDone = NEWTON.multiply(METRE);
        Quantity<Force> l1 = Quantities.getQuantity(200, NEWTON);
        Quantity<Length> l2 = Quantities.getQuantity(3, KILO_METRE);
        Quantity<?> prod1 = l1.multiply(l2); // 600 N·km
        Quantity<?> prod2 = l2.multiply(l1); // 600 km·N
        // end::multiplyresult[]
        // tag::multiplyresultcast[]
        Quantity<Energy> prod3 = l2.multiply(l1).asType(Energy.class).to(JOULE); // 600000 J
        // end::multiplyresultcast[]
        System.out.println(prod1);
        System.out.println(prod2);
        System.out.println(prod3);
    }

    /**
     * Hertz is built-in. Create prefixed units as you need them
     *
     */
    public void siprefixes() {

// tag::prefixes[]    
        Unit<Length> Kilometer = MetricPrefix.KILO(METRE);
        Unit<Length> Centimeter = MetricPrefix.CENTI(METRE);
// end::prefixes[]    

        // tag::siprefixes[]
        Unit<Frequency> KILOHERTZ = MetricPrefix.KILO(Units.HERTZ);
        Unit<Frequency> MEGAHERTZ = MetricPrefix.MEGA(Units.HERTZ);
        Unit<Frequency> GIGAHERTZ = MetricPrefix.GIGA(Units.HERTZ);

        //tag::to[]
        Quantity<Frequency> radio_one = Quantities.getQuantity(97.1, MEGAHERTZ);
        System.out.println(radio_one); // "97.1 MHz"
        System.out.println(radio_one.to(KILOHERTZ)); // "97100 kHz"
        System.out.println(radio_one.to(GIGAHERTZ)); // "0.0971 GHz"
        // end::to[]
        // end::siprefixes[]
    }

    public void siprefixesdirect() {

        // tag::siprefixesdirect[]
        Quantity<Frequency> radio_one = Quantities.getQuantity(97.1, MetricPrefix.KILO(Units.HERTZ));
        System.out.println(radio_one); // "97100 kHz"
        // end::siprefixesdirect[]
    }

    public void quantitygetters() {

        // tag::quantitygetters[]
        Quantity<Frequency> radio_one = Quantities.getQuantity(97.1, MetricPrefix.KILO(Units.HERTZ));
        System.out.println(radio_one.getValue()); // 97100
        System.out.println(radio_one.getUnit()); // kHz
        Dimension dimension = radio_one.getUnit().getDimension();
        System.out.println(dimension); //  1/[T]
        System.out.println(dimension.getBaseDimensions()); // {[T]=-1}
        // end::quantitygetters[]
    }

    public void list(SystemOfUnits su) {
        Set<Unit> sorted = new TreeSet<>(su.getUnits());
        for (Unit u : sorted) {
            StringBuilder sb = new StringBuilder();
            sb.append("| " + u.getName() + " | " + u.getDimension() + " | " + u.toString() + " | " + u.getSymbol() + " | ");

            if (u.getDimension().getBaseDimensions() != null) {
                for (Map.Entry<? extends Dimension, Integer> e : u.getDimension().getBaseDimensions().entrySet()) {
                    sb.append(e.getKey());
                    if (e.getValue()!=1) sb.append("^" + e.getValue() + "^ ");
                }
            }
            sb.append("\n");
            System.out.println(sb); //  1/[T]
        }
    }

    public void si() {
        System.out.println("\n\nSI:");
        list(Units.getInstance());
        System.out.println("\n\nImperial:");
        //list(systems.uom.ucum.UCUM.getInstance()); 

    }

    public void simpleparse() {
        // tag::simpleparse[]
        QuantityFormat format = SimpleQuantityFormat.getInstance();
        System.out.println(format.parse("3.95 m°C")); // 3.95 m°C
        System.out.println(format.parse("6.0 kW")); // 6 kW
        //System.out.println(format.parse("2.5 Nm/s")); // 2.5 N m/s
        // end::simpleparse[]
        //System.out.println(format.parse("12 ft").equals(Quantities.getQuantity(12, Units.METRE.multiply(0.3048))));
    }

    public void simpleAsciiParse() {
        // tag::simpleparseascii[]
//        QuantityFormat format = SimpleQuantityFormat.getInstance("ASCII");
//        System.out.println(format.parse("3.95 m°C").equals(Quantities.getQuantity(3.95, MetricPrefix.MILLI(Units.CELSIUS)))); // true
//        System.out.println(format.parse("6 kW").equals(Quantities.getQuantity(6, MetricPrefix.KILO(Units.WATT)))); // true
        //System.out.println(format.parse("12 ft").equals(Quantities.getQuantity(12, Units.METRE.multiply(0.3048))));
        // end::simpleparseascii[]
    }

    public void collections() {
        // tag::collections[]
        Collection<Quantity<Power>> bag = new TreeSet<>();
        bag.add(getQuantity(3.4, KILO_WATT));
        bag.add(getQuantity(1.2, WATT));
        bag.add(getQuantity(4000, WATT));
        bag.add((Quantity) getQuantity(62, WATT)); // Why is this not an error?
        // bag.add(getQuantity(3.4, KELVIN)); // Compile time error: incompatible types
        System.out.println(bag); // [1.2 W, 62 W, 3.4 kW, 4000 W]
        // end::collections[]

        // incompatible types: inference variable Q has incompatible equality constraints Power,Temperature
        // where Q is a type-variable:
        // Q extends Quantity<Q> declared in method <Q>getQuantity(Number,Unit<Q>)
    }

    public void comparable() {
        // tag::comparable[]
        ComparableQuantity<Power> q1 = getQuantity(3.4, KILO_WATT);
        ComparableQuantity<Power> q2 = getQuantity(5.2, WATT);
        ComparableQuantity<Power> q3 = getQuantity(4.0, KILO_WATT);
        ComparableQuantity<Power> q4 = getQuantity(4000, WATT);
        ComparableQuantity<Temperature> q5 = getQuantity(273, KELVIN);

        System.out.println(q1.compareTo(q2)); // 1
        System.out.println(q3.compareTo(q4)); // 0
        System.out.println(q2.compareTo(q4)); // -1
        //System.out.println(q2.compareTo(q5)); // Compile time error: Incompatibe types        
        // end::comparable[]
    }

    public void size() {
//        showSize(Double.valueOf(12345.6));
//        showSize(BigDecimal.valueOf(12345.6));
//        showSize(KILO_WATT);
//        showSize(KELVIN);
//        showSize(getQuantity(5.2, WATT));
//        showSize(getQuantity(3.4, KILO_WATT));
    }

    public void showSize(Object obj) {

        System.out.println(obj.getClass() + " " + obj.toString() + " = " + ObjectSizeFetcher.getObjectSize(obj));
    }

    public void temperatureSums() {
        // tag::temperaturesumswrong[]
        System.out.println(getQuantity(2, CELSIUS).add(getQuantity(3, CELSIUS))); // 278.15 ℃
        // end::temperaturesumswrong[]
        // tag::temperaturesumsright[]
        System.out.println(getQuantity(2, CELSIUS).add(getQuantity(3, KELVIN))); // 5 ℃
        // end::temperaturesumsright[]        
    }

    public void text() {
        //SimpleQuantityFormat fmt = SimpleQuantityFormat.getInstance();
        // tag::text[]
        Quantity<Power> q = Quantities.getQuantity(2, KILO_WATT);
        Quantity<?> q1 = Quantities.getQuantity(q.toString());
        System.out.println(q1.equals(q)); // true, for any q
        // end::text[]
    }

    //tag::newQuantityTypeInterfaceAlias[]
    public interface Xordinate extends Quantity<Xordinate> {
    }

    public interface Yordinate extends Quantity<Yordinate> {
    }

    public void qtytypetest() {
        final Quantity<Length> xordinate = Quantities.getQuantity(3000, METRE);
        final Quantity<Length> yordinate = Quantities.getQuantity(500, METRE);
        // Works but not physically meaningful:
        xordinate.add(yordinate);

        final Quantity<Xordinate> xordinate2 = Quantities.getQuantity(3000, METRE).asType(Xordinate.class);
        final Quantity<Yordinate> yordinate2 = Quantities.getQuantity(500, METRE).asType(Yordinate.class);
        //  incompatible type compiler message:
        // xordinate2.add(yordinate2);
    }
    //end::newQuantityTypeInterfaceAlias[]

    public void asTypeIsRuntimeChecks() {
        try {
            Quantity<Length> xordinate = Quantities.getQuantity(3000, WATT).asType(Length.class);
        } catch (java.lang.ClassCastException e) {
            System.out.println(e);  // java.lang.ClassCastException: The unit: W is not compatible with quantities of type interface javax.measure.quantity.Length
        }
    }
}
