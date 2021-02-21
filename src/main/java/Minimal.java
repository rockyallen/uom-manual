//tag::import[]
import javax.measure.Quantity;
import javax.measure.quantity.Speed;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;
//end::import[]

public class Minimal {

// tag::main[]
    public static void main(String[] args) {
        Quantity<Speed> C = Quantities.getQuantity(1079252849, Units.KILOMETRE_PER_HOUR);
        System.out.println("The speed of light: " + C);
    }
// end::main[]
}
