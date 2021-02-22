
import static javax.measure.MetricPrefix.KILO;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.format.SimpleUnitFormat;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.AlternateUnit;
import tech.units.indriya.unit.Units;
import static tech.units.indriya.unit.Units.GRAM;
import static tech.units.indriya.unit.Units.KILOGRAM;
import static tech.units.indriya.unit.Units.METRE;

/**
 * Measure of human body shape. 18.5 to 24.9 is considered ideal for health.
 * Over 30 is obese.
 */
public class BodyMassIndex {

    /**
     * Define a new Quantity type for the Bmi_unit
     */
    public interface Bmi extends Quantity<Bmi> {}
    /**
     * Unit to represent BMI, and attach it to the Quantity type
     */
    public static final Unit<Bmi> bmi_unit = 
            new AlternateUnit<Bmi>(Units.KILOGRAM.divide(Units.METRE.pow(2)), "BMI");

    // Register the symbol with the parser so that it knows how to convert string 
    static {
       SimpleUnitFormat.getInstance().label(bmi_unit, "BMI");        
    }
    
    /**
     * Provide some useful constants
     */
    public static final Quantity<Bmi> OVERWEIGHT 
            = Quantities.getQuantity(24.9, bmi_unit);
    
    public static final Quantity<Bmi> UNDERWEIGHT 
            = Quantities.getQuantity(18.5, bmi_unit);
    
    public static final Quantity<Bmi> OBESE 
            = Quantities.getQuantity(30.0, bmi_unit);

    /**
     * Utility method to create one. Note the return type is ComparableQuantity
     * because I expect to use it for comparisons. How can I get rid of the
     * cast?
     */
    public static ComparableQuantity<Bmi> bmi(Quantity<Mass> mass, 
            Quantity<Length> height) {
        
        return (ComparableQuantity) mass.divide(height).divide(height)
                .asType(Bmi.class).to(bmi_unit);
    }

    /**
     * Test it
     */
    public static void main(String[] args) {

        // 1. Create one directly from measurements
        Quantity<Mass> mass = Quantities.getQuantity("75 kg").asType(Mass.class);
        Quantity<Length> height = 
                Quantities.getQuantity("1.80 m").asType(Length.class);
        
        Quantity<Bmi> bmi1 = mass.divide(height).divide(height).asType(Bmi.class);
        System.out.println(bmi1); // 23.14814814814814814814814814814815 BMI

        // 2. Create one from the utility method
        Quantity<Bmi> bmi2 = bmi(mass, height);
        System.out.println(bmi2); // 23.14814814814814814814814814814815 BMI

        // 3. Show that the utility method accepts any commensurable units
        Quantity<Bmi> bmi3 = bmi(mass.to(GRAM), height.to(KILO(METRE)));
        System.out.println(bmi3); // 23.14814814814814814814814814814815 BMI
        
        // 4. Create one by parsing
        Quantity<Bmi> bmi4 = Quantities.getQuantity("27.6 BMI").asType(Bmi.class);
        System.out.println(bmi4); // 27.6 BMI

        // 5. Show that the quantity type works to prevent mismatches
        Quantity<Bmi> bmi5 = null;
        try {
            bmi5 = 
                Quantities.getQuantity(50, KILOGRAM).asType(Bmi.class);
            System.out.println("WRONG " + bmi5); // 50 kg !???
        } catch (java.lang.ClassCastException ex) {
        // Expect java.lang.ClassCastException: The unit: kg/m² is not compatible with quantities of type interface javax.measure.quantity.Length
            System.out.println(ex); 
        }
        // but this corrrectly spots the error
        try {
            bmi5.to(bmi_unit);
        }
        catch (javax.measure.UnconvertibleException ex)
        {
            System.out.println("Success");
        }
        
        // 6. Use it in an application
        checkMyBmi(mass, height); 
        // Prints "Your BMI is 23.148... BMI: You are a healthy weight"
    }

    /**
     * Sample method using Bmi.
     */
    public static void checkMyBmi(Quantity<Mass> mass, Quantity<Length> height) {

        ComparableQuantity<Bmi> bmi = bmi(mass, height);

        System.out.print("Your BMI is " + bmi + ": ");

        if (bmi.compareTo(OBESE) > 0) {
            System.out.println("You are obese");
        } else if (bmi.compareTo(OVERWEIGHT) > 0) {
            System.out.println("You are overweight");
        } else if (bmi.compareTo(UNDERWEIGHT) < 0) {
            System.out.println("You are underweight");
        } else {
            System.out.println("You are a healthy weight");
        }
    }
}
