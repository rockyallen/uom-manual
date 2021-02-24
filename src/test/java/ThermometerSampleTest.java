
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

/**
 *
 * @author rocky
 */
public class ThermometerSampleTest {

    public ThermometerSampleTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testSerialsationT() throws IOException, ClassNotFoundException {
        System.out.println("testSerialsationT()");

        int sample = 1000;

        ThermometerSample[] instance = new ThermometerSample[sample];
        for (int i = 0; i < sample; i++) {
            instance[i] = new ThermometerSample(Quantities.getQuantity(120, Units.SECOND), Quantities.getQuantity(37.5, Units.KELVIN));
        }

        byte[] bytes = SerialisationHelper.tobytes(instance);

        System.out.println("Size=" + bytes.length);
        assertEquals(8 * sample + 6094, bytes.length);

        ThermometerSample[] result = SerialisationHelper.frombytes(bytes, instance.getClass());
        assertEquals(instance[79], result[79]);
    }
}
