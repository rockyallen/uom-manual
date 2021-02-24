
import java.io.IOException;
import static javax.measure.MetricPrefix.DECI;
import javax.measure.Quantity;
import javax.measure.UnconvertibleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tech.units.indriya.quantity.Quantities;
import static tech.units.indriya.unit.Units.LITRE;
import static tech.units.indriya.unit.Units.NEWTON;

/**
 */
public class DimensionedVectorTest {

    public DimensionedVectorTest() {
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

    /**
     * Test of getUnit method, of class DimensionedVector.
     */
    @Test
    public void testNew() {
        DimensionedVector instance = new DimensionedVector(new double[]{1.1, 2.2}, NEWTON);
        assertEquals("2.2 N", instance.get(1).toString());
    }

    /**
     * Test of getUnit method, of class DimensionedVector.
     */
    @Test
    public void testGetUnit() {
        DimensionedVector instance = new DimensionedVector(new double[]{1.1, 2.2}, NEWTON);
        assertEquals("N", instance.getUnit().toString());
    }

    /**
     * Test of set method, of class DimensionedVector.
     */
    @Test
    public void testSetGoodExact() {
        DimensionedVector instance = new DimensionedVector(new double[]{1.1, 2.2}, NEWTON);
        instance.set(1, Quantities.getQuantity(5.2, NEWTON));
        assertEquals("5.2 N", instance.get(1).toString());
    }

    @Test
    public void testSetGoodCommensurate() {
        // tag::runtimeexception[]
        DimensionedVector instance = new DimensionedVector(new double[]{1.1, 2.2}, NEWTON);
        instance.set(1, Quantities.getQuantity(52, DECI(NEWTON)));
        assertEquals("5.2 N", instance.get(1).toString());
        // end::runtimeexception[]
    }

    @Test
    public void testSetBad() {
        DimensionedVector instance = new DimensionedVector(new double[]{1.1, 2.2}, NEWTON);
        Quantity wrong = Quantities.getQuantity(5.2, LITRE);

        assertThrows(UnconvertibleException.class,
                () -> {
                    instance.set(1, wrong);
                });
    }

    @Test
    public void testSerialsation() throws IOException, ClassNotFoundException {
        System.out.println("testSerialsation()");
        
        int samples = 1000;
        
        DimensionedVector instance = new DimensionedVector(new double[1000], NEWTON);
        byte[] bytes = SerialisationHelper.tobytes(instance);

        System.out.println("Size=" + bytes.length);
        assertEquals(8*samples + 913, bytes.length);

        DimensionedVector result = SerialisationHelper.frombytes(bytes, instance.getClass());
        assertEquals(instance, result);
    }
}
