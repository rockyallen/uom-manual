package uom.bm;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.math.BigDecimal;
import static javax.measure.MetricPrefix.KILO;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Power;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.TransformedUnit;
import static tech.units.indriya.unit.Units.RADIAN;
import static tech.units.indriya.unit.Units.WATT;

//@BenchmarkMode({Mode.AverageTime})
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 8)
//@Threads(1)

/**
 * Results:
 
QObjectActions.Qmake            thrpt    8   23719733.231 ±   110214.889  ops/s
QObjectActions.QnewUnit         thrpt    8    1419023.144 ±    16208.260  ops/s
QObjectActions.Qto              thrpt    8      93713.987 ±      235.952  ops/s
QObjectActions.REFERENCE_dMult  thrpt    8  300439944.151 ±  2298890.470  ops/s

 */

public class QObjectActions {

    public static final Unit KILOWATT = KILO(WATT);

    @State(Scope.Benchmark)
    public static class AdditionalState {

        public double dNumber1;
        public double dNumber2;
        public Double DNumber1;
        public Double DNumber2;
        public Long longNumber1;
        public Long longNumber2;
        public BigDecimal bdNumber1;
        public BigDecimal bdNumber2;
        public Quantity<Power> q1;
        public Quantity<Power> q2;

        @Setup(Level.Iteration)
        public void doInitializing() {
            longNumber1 = 9877L;
            longNumber2 = 1282L;
            dNumber1 = 9877.5;
            dNumber2 = 1282.6;
            DNumber1 = Double.valueOf(9877.5);
            DNumber2 = Double.valueOf(1282.6);
            bdNumber1 = new BigDecimal(longNumber1);
            bdNumber2 = new BigDecimal(longNumber2);
            q1 = Quantities.getQuantity(9877.5, WATT);
            q2 = Quantities.getQuantity(1282.6, WATT);
        }
    }

    /**
     * uSE THIS A A REFERECE COMPARSOM
     */
    @Benchmark
    public void REFERENCE_dMult(AdditionalState state, Blackhole bh) {
        bh.consume(state.dNumber1 * state.dNumber2);
    }
    /**
     * Create a complicated unit
     */
    @Benchmark
    public void QnewUnit(AdditionalState state, Blackhole bh) {
        bh.consume(
                new TransformedUnit<>("°", RADIAN,
        MultiplyConverter.ofPiExponent(1).concatenate(MultiplyConverter.ofRational(1, 180))));
    }

    @Benchmark
    public void Qto(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.to(KILOWATT));
    }

    @Benchmark
    public void QtoSame(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.to(WATT));
    }

    /**
     * Create a single quantity
     */
    @Benchmark
    public void Qmake(AdditionalState state, Blackhole bh) {
        bh.consume(Quantities.getQuantity(97.6, WATT));
    }

    /**
     * Create a single quantity
     */
    @Benchmark
    public void QmakeBig(AdditionalState state, Blackhole bh) {
        bh.consume(Quantities.getQuantity(9323424323422333437.6, WATT));
    }
}
