
import java.io.Serializable;
import java.util.Arrays;
import javax.measure.Quantity;
import javax.measure.Unit;
import tech.units.indriya.quantity.Quantities;

/**
 * Wraps the primitive array in a unit.
 *
 * You can put any commensurate quantity in it; you always get the constructor
 * unit out.
 *
 * Currently only throws a runtime exception. How can I make it throw a compile
 * time error instead?
 */
// tag::class[]
public class DimensionedVector<T extends Quantity<T>> implements Serializable {

    private final double[] arr;
    private final Unit<T> unit;

    public DimensionedVector(double[] arr, Unit<T> unit) {
        this.arr = arr;
        this.unit = unit;
    }

    // Currently only throws a runtime exception. How can I make it throw a compile
    // time error instead?
    public void set(int i, Quantity<T> qty) {
        arr[i] = qty.to(unit).getValue().doubleValue();
    }

    public Quantity<T> get(int i) {
        return Quantities.getQuantity(arr[i], unit);
    }

    public Unit<T> getUnit() {
        return unit;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        DimensionedVector that = (DimensionedVector)obj;
        return this.unit.equals(that.unit)&&Arrays.equals(this.arr, that.arr);
    }
}
// end::class[]
