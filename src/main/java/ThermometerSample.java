
import java.io.Serializable;
import java.util.Arrays;
import javax.measure.Quantity;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Time;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

/**
 * Reducing the size for serialization
 *
 * @author rocky
 */
// tag::class[]
public class ThermometerSample implements Serializable {

    /**
     * Stores time in whole seconds, but only visible to clients as a
     * Quantity<Time>. Good for up to 68 years?
     */
    private int time; // 4 bytes
    /**
     * Stores temperature in Kelvin, but only visible to clients as a
     * Quantity<Temperature>
     */
    private float temperature;  // 4 bytes

    public ThermometerSample(Quantity<Time> time, Quantity<Temperature> temperature) {
        this.time = time.to(Units.SECOND).getValue().intValue();
        this.temperature = temperature.to(Units.KELVIN).getValue().floatValue();
    }

    /**
     * Get the sample time in Seconds
     */
    public ComparableQuantity<Time> getTime() {
        return Quantities.getQuantity(time, Units.SECOND);
    }

    /**
     * Get the temperature in Kelvin
     */
    public ComparableQuantity<Temperature> getTemperature() {
        return Quantities.getQuantity(time, Units.KELVIN);
    }

    @Override
    public boolean equals(Object obj) {
        ThermometerSample that = (ThermometerSample) obj;
        return this.time == that.time && this.temperature == that.temperature;
    }
}
// end::class[]
