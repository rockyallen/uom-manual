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
 *
Benchmark                        Mode  Cnt          Score          Error  Units
MathsCompare.BigDecimaladd      thrpt    8  105207328.262 ±  1136341.633  ops/s
MathsCompare.BigDecimalmult     thrpt    8   96333983.582 ±   502521.999  ops/s
MathsCompare.BigDecimalsub      thrpt    8  102424913.107 ±  3202872.100  ops/s
MathsCompare.DAdd               thrpt    8  271092784.456 ±  4027925.049  ops/s
MathsCompare.DDiv               thrpt    8  152567415.741 ±   357874.264  ops/s
MathsCompare.DMult              thrpt    8  268332846.343 ±  7002552.594  ops/s
MathsCompare.DSub               thrpt    8  268729482.861 ±  5566639.339  ops/s
MathsCompare.Ladd               thrpt    8  259400344.478 ± 18792029.443  ops/s
MathsCompare.Ldivide            thrpt    8   73131014.254 ±   136577.285  ops/s
MathsCompare.Lmultiply          thrpt    8  266082433.479 ±  6635213.417  ops/s
MathsCompare.Lsub               thrpt    8  300842929.024 ±  3304924.077  ops/s
MathsCompare.Qadd               thrpt    8     162613.226 ±      173.419  ops/s
MathsCompare.QaddDiff           thrpt    8      91612.474 ±      340.334  ops/s
MathsCompare.Qdivide            thrpt    8     116975.536 ±      695.022  ops/s
MathsCompare.QdivideDiff        thrpt    8     114292.574 ±      260.402  ops/s
MathsCompare.Qmultiply          thrpt    8    4870867.934 ±    16870.031  ops/s
MathsCompare.QmultiplyDiff      thrpt    8    4070883.725 ±    13254.326  ops/s
MathsCompare.Qsub               thrpt    8     158574.899 ±      715.447  ops/s
MathsCompare.QsubDiff           thrpt    8      91978.639 ±      231.071  ops/s
MathsCompare.dAdd               thrpt    8  303080596.857 ±  1704548.028  ops/s
MathsCompare.dDiv               thrpt    8  152549954.652 ±   304940.925  ops/s
MathsCompare.dMult              thrpt    8  297813163.834 ±  2017278.976  ops/s
MathsCompare.dSub               thrpt    8  311210031.309 ±  2178087.510  ops/s
QObjectActions.Qmake            thrpt    8   24314440.797 ±    99099.202  ops/s
QObjectActions.QnewUnit         thrpt    8    1281513.473 ±     3314.339  ops/s
QObjectActions.Qto              thrpt    8      94397.899 ±      227.174  ops/s
QObjectActions.REFERENCE_dMult  thrpt    8  303211016.421 ±  4689382.444  ops/s

* Benchmark                        Mode  Cnt          Score          Error  Units
MathsCompare.BigDecimaladd      thrpt    8  103778470.779 ±   667408.534  ops/s
MathsCompare.BigDecimalmult     thrpt    8   94862995.907 ±   987498.734  ops/s
MathsCompare.BigDecimalsub      thrpt    8  101279608.015 ±  1554439.319  ops/s
MathsCompare.DAdd               thrpt    8  266663166.844 ±  6775815.595  ops/s
MathsCompare.DDiv               thrpt    8  146827809.901 ±  8575975.114  ops/s
MathsCompare.DMult              thrpt    8  259540192.657 ± 19434536.304  ops/s
MathsCompare.DSub               thrpt    8  258775927.738 ± 13648589.237  ops/s
MathsCompare.Ladd               thrpt    8  256610824.068 ± 16599326.821  ops/s
MathsCompare.Ldivide            thrpt    8   71062094.543 ±   132527.036  ops/s
MathsCompare.Lmultiply          thrpt    8  263440755.713 ±  6400284.397  ops/s
MathsCompare.Lsub               thrpt    8  295766176.307 ±  2095192.222  ops/s
MathsCompare.Qadd               thrpt    8     150216.437 ±     4388.297  ops/s
MathsCompare.QaddDiff           thrpt    8      90133.261 ±     3715.152  ops/s
MathsCompare.Qdivide            thrpt    8     115345.345 ±      969.319  ops/s
MathsCompare.QdivideDiff        thrpt    8     111242.839 ±     1792.577  ops/s
MathsCompare.Qmultiply          thrpt    8    4728669.524 ±    98207.041  ops/s
MathsCompare.QmultiplyDiff      thrpt    8    3981924.511 ±    48303.186  ops/s
MathsCompare.Qsub               thrpt    8     156030.585 ±     1662.277  ops/s
MathsCompare.QsubDiff           thrpt    8      86200.053 ±     4724.118  ops/s
MathsCompare.dAdd               thrpt    8  295998413.554 ±  2292698.727  ops/s
MathsCompare.dDiv               thrpt    8  148256050.523 ±  6942124.498  ops/s
MathsCompare.dMult              thrpt    8  296463362.210 ± 14284735.773  ops/s
MathsCompare.dSub               thrpt    8  305212214.628 ±  2534531.258  ops/s
QObjectActions.Qmake            thrpt    8   23915972.194 ±   143182.774  ops/s
QObjectActions.QmakeBig         thrpt    8    1639287.934 ±     7271.482  ops/s
QObjectActions.QnewUnit         thrpt    8    1343345.012 ±    66143.567  ops/s
QObjectActions.Qto              thrpt    8      93811.558 ±      898.560  ops/s
QObjectActions.QtoSame          thrpt    8  243009748.497 ± 14965781.711  ops/s
QObjectActions.REFERENCE_dMult  thrpt    8  295876004.416 ±  3322834.524  ops/s
* 
 */
public class MathsCompare {

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
        public Quantity<Power> q3;

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
            q3 = Quantities.getQuantity(1.2826, KILOWATT);
        }
    }

    /**
     * Quantity same units
     */
    @Benchmark
    public void Qadd(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.add(state.q2));
    }

    @Benchmark
    public void Qsub(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.subtract(state.q2));
    }

    @Benchmark
    public void Qmultiply(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.multiply(state.q2));
    }

    @Benchmark
    public void Qdivide(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.divide(state.q2));
    }

    /**
     * Quantity different units
     */
    @Benchmark
    public void QaddDiff(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.add(state.q3));
    }

    @Benchmark
    public void QsubDiff(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.subtract(state.q3));
    }

    @Benchmark
    public void QmultiplyDiff(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.multiply(state.q3));
    }

    @Benchmark
    public void QdivideDiff(AdditionalState state, Blackhole bh) {
        bh.consume(state.q1.divide(state.q3));
    }

    /**
     * long
     * @param state
     * @param bh 
     */
    @Benchmark
    public void Ladd(AdditionalState state, Blackhole bh) {
        bh.consume(state.longNumber1 + state.longNumber2);
    }

    @Benchmark
    public void Lsub(AdditionalState state, Blackhole bh) {
        bh.consume(state.longNumber1 - state.longNumber2);
    }

    @Benchmark
    public void Lmultiply(AdditionalState state, Blackhole bh) {
        bh.consume(state.longNumber1 * state.longNumber2);
    }

    @Benchmark
    public void Ldivide(AdditionalState state, Blackhole bh) {
        bh.consume(state.longNumber1 / state.longNumber2);
    }

    /**
     * BigDecimal
     */
    @Benchmark
    public void BigDecimaladd(AdditionalState state, Blackhole bh) {
        bh.consume(state.bdNumber1.add(state.bdNumber2));
    }

    @Benchmark
    public void BigDecimalsub(AdditionalState state, Blackhole bh) {
        bh.consume(state.bdNumber1.subtract(state.bdNumber2));
    }

    @Benchmark
    public void BigDecimalmult(AdditionalState state, Blackhole bh) {
        bh.consume(state.bdNumber1.multiply(state.bdNumber2));
    }

    @Benchmark
    public void BigDecimaldiv(AdditionalState state, Blackhole bh) {
        bh.consume(state.bdNumber1.divide(state.bdNumber2));
    }

    /**
     * double primitive
     */
    @Benchmark
    public void dAdd(AdditionalState state, Blackhole bh) {
        bh.consume(state.dNumber1 + state.dNumber2);
    }

    @Benchmark
    public void dSub(AdditionalState state, Blackhole bh) {
        bh.consume(state.dNumber1 - state.dNumber2);
    }

    @Benchmark
    public void dMult(AdditionalState state, Blackhole bh) {
        bh.consume(state.dNumber1 * state.dNumber2);
    }

    @Benchmark
    public void dDiv(AdditionalState state, Blackhole bh) {
        bh.consume(state.dNumber1 / state.dNumber2);
    }

    /**
     * Double object
     */
    @Benchmark
    public void DAdd(AdditionalState state, Blackhole bh) {
        bh.consume(state.DNumber1 + state.DNumber2);
    }

    @Benchmark
    public void DSub(AdditionalState state, Blackhole bh) {
        bh.consume(state.DNumber1 - state.DNumber2);
    }

    @Benchmark
    public void DMult(AdditionalState state, Blackhole bh) {
        bh.consume(state.DNumber1 * state.DNumber2);
    }

    @Benchmark
    public void DDiv(AdditionalState state, Blackhole bh) {
        bh.consume(state.DNumber1 / state.DNumber2);
    }
}
