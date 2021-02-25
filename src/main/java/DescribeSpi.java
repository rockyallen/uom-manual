
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static javax.measure.MetricPrefix.KILO;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.QuantityFormat;
import javax.measure.format.UnitFormat;
import javax.measure.quantity.Length;
import javax.measure.spi.FormatService;
import javax.measure.spi.QuantityFactory;
import javax.measure.spi.ServiceProvider;
import javax.measure.spi.SystemOfUnits;
import tech.units.indriya.quantity.Quantities;
import static tech.units.indriya.unit.Units.HERTZ;
import static tech.units.indriya.unit.Units.KILOGRAM;
import static tech.units.indriya.unit.Units.METRE;
import static tech.units.indriya.unit.Units.NEWTON;
import static tech.units.indriya.unit.Units.SECOND;
import static tech.units.indriya.unit.Units.VOLT;

/**
 * Document what a spi can offer;
 *
 * exercise it as far as possible;
 *
 * output a description in asciidoc format.
 *
 * @author rocky
 */
public class DescribeSpi {

    final File fout = new File("src/asciidoc/userguide/spi.adoc");

    public void testSpi() throws IOException {
        sb = new StringBuilder();
        // List the available prodiders
        for (ServiceProvider sp : ServiceProvider.available()) {
            {
                describeServiceProvider(sp);
            }
        }
        System.out.println(sb.toString());
        BufferedWriter w = new BufferedWriter(new FileWriter(fout));
        w.append(sb);
        w.close();
        System.out.println(fout.getAbsolutePath());
    }

    StringBuilder sb = null;

    private void describeServiceProvider(ServiceProvider sp) {
        sb.append("\n\n\n= Service provider: ").append(sp).append("\n");

        FormatService formatService = sp.getFormatService();

        sb.append("\n== Unit Formatters").append("\n");
        for (String s : formatService.getAvailableFormatNames(FormatService.FormatType.UNIT_FORMAT)) {
            sb.append("- \"").append(s).append("\"\n");
        }
        for (String s : formatService.getAvailableFormatNames(FormatService.FormatType.UNIT_FORMAT)) {
            sb.append("\n=== \"").append(s).append("\"\n");
            describe(formatService.getUnitFormat(s));
        }

        sb.append("\n== Quantity Formatters").append("\n");
        for (String s : formatService.getAvailableFormatNames(FormatService.FormatType.QUANTITY_FORMAT)) {
            sb.append("- \"").append(s).append("\"\n");
        }
        for (String s : formatService.getAvailableFormatNames(FormatService.FormatType.QUANTITY_FORMAT)) {
            sb.append("\n=== \"").append(s).append("\"\n");
            describe(formatService.getQuantityFormat(s));
        }

        sb.append("\n== Systems of Units").append("\n");
        for (SystemOfUnits sou : sp.getSystemOfUnitsService().getAvailableSystemsOfUnits()) {
            sb.append("- ").append(sou.getName()).append("\n");
        }
        for (SystemOfUnits sou : sp.getSystemOfUnitsService().getAvailableSystemsOfUnits()) {
            describe(sou);
        }

        sb.append("\n== Quantity Factories").append("\n");
        describe("Length: ", sp.getQuantityFactory(Length.class));
        describe("Mass: ", sp.getQuantityFactory(Length.class));
        sb.append("etc").append("\n");
    }

    private void describe(SystemOfUnits sou) {

        sb.append("\n=== ").append(sou.getName()).append("\n\n");

        sb.append(".Units provided by SystemOfUnits\n");
        sb.append("[options=header]\n");
        sb.append("|===\n|Name |Symbol |Dimension |Base units |System unit\n\n");
        for (Unit u : sou.getUnits()) {
            sb.append("|").append(u.getName()).append("\n\n");
            sb.append("|").append(u.getSymbol()).append("\n\n");
            sb.append("|").append(u.getDimension()).append("\n\n");
            sb.append("|").append(u.getBaseUnits()).append("\n\n");
            sb.append("|").append(u.getSystemUnit()).append("\n\n");
        }
        sb.append("\n|===\n\n");
//        sou.getUnits(UnitDimension.TIME);
//
//        for (Field f : UnitDimension.class.getFields()) {
//            sb.append(f);
//            //sou.getUnits((Dimension)f.getType());
//        }
    }

    private void describe(UnitFormat uf) {
        this.testParse(uf);
    }

    private void describe(QuantityFormat qf) {
        this.testParse(qf);
    }

    private void describe(String qt, QuantityFactory qf) {
        sb.append(qt + ":" + qf).append("\n\n");
    }

    public static final String multdot = String.valueOf(Character.toChars(0x22C5));

    // tag::unicode[]
    public static final String interpunct = String.valueOf(Character.toChars(0x00B7));
    public static final String sup0 = String.valueOf(Character.toChars(0x2070));
    public static final String sup1 = String.valueOf(Character.toChars(0x00B9));
    public static final String sup2 = String.valueOf(Character.toChars(0x00B2));
    public static final String sup3 = String.valueOf(Character.toChars(0x00B3));
    public static final String sup4 = String.valueOf(Character.toChars(0x2074));
    public static final String sup5 = String.valueOf(Character.toChars(0x2075));
    public static final String sup6 = String.valueOf(Character.toChars(0x2076));
    public static final String sup7 = String.valueOf(Character.toChars(0x2077));
    public static final String sup8 = String.valueOf(Character.toChars(0x2078));
    public static final String sup9 = String.valueOf(Character.toChars(0x2079));
    public static final String supMinus = String.valueOf(Character.toChars(0x207B));
    public static final String omega = String.valueOf(Character.toChars(0x03A9));
    public static final String micro = String.valueOf(Character.toChars(0x00B5));
    // end::unicode[]

    public void testParse(QuantityFormat f) {
        System.out.println("testParse");
        sb.append(".Test results\n");
        sb.append("[options=header]\n");
        sb.append("|===\n");
        sb.append("\n|Case |Input |Output\n\n");
        quantityFormatHelper(f, "Simple", "6 m", null, 6, METRE, "6 m");
        quantityFormatHelper(f, "Extra space", "6       m", null, 6, METRE, "6 m");
        quantityFormatHelper(f, "Prefix", "11.3 kV", null, 11, KILO(VOLT), "11.3 kV");
        quantityFormatHelper(f, "Rational number", "-5÷3 m", null, -5.0 / 3.0, METRE, "-1.666666666666666666666666666666667 m");
        quantityFormatHelper(f, "Compound", "11 N" + interpunct + "m", null, 11, NEWTON.multiply(METRE), "11 N·m");
        quantityFormatHelper(f, "Inverted", "6 m/s", null, 6, METRE.divide(SECOND), "6 m");
        quantityFormatHelper(f, "Inverted powers", "6 m/s" + sup2, null, 6, METRE.divide(SECOND).divide(SECOND), "6 m/s2");
        quantityFormatHelper(f, "Repeated units", "1013 kg/m·m·m", null, 1013, KILOGRAM.divide(METRE).divide(METRE).divide(METRE), "6 kg/m3");
        quantityFormatHelper(f, "Scientific", "6 m" + interpunct + "s" + supMinus + sup2, null, 6, METRE.divide(SECOND).divide(SECOND), "6 m/s2");
        quantityFormatHelper(f, "Leading 1", "6 1/s", null, 6, HERTZ, "6 Hz");
        sb.append("\n\n|===\n");
    }

    public void testParse(UnitFormat f) {
        System.out.println("testParse");
        sb.append(".Test results\n");
        sb.append("[options=header]\n");
        sb.append("|===\n");
        sb.append("\n|Case |Input |Output\n\n");
        unitFormatHelper(f, "Simple", "m", null, 6, METRE, "m");
        unitFormatHelper(f, "Prefix", "kV", null, 11, KILO(VOLT), "kV");
        unitFormatHelper(f, "Compound", "N" + interpunct + "m", null, 11, NEWTON.multiply(METRE), "11 N·m");
        unitFormatHelper(f, "Inverted", "m/s", null, 6, METRE.divide(SECOND), "6 m");
        unitFormatHelper(f, "Inverted powers", "m/s" + sup2, null, 6, METRE.divide(SECOND).divide(SECOND), "6 m/s2");
        unitFormatHelper(f, "Repeated units", "kg/m·m·m", null, 1013, KILOGRAM.divide(METRE).divide(METRE).divide(METRE), "6 kg/m3");
        unitFormatHelper(f, "Scientific", "m" + interpunct + "s" + supMinus + sup2, null, 6, METRE.divide(SECOND).divide(SECOND), "6 m/s2");
        unitFormatHelper(f, "Leading 1", "1/s", null, 6, HERTZ, "6 Hz");
        unitFormatHelper(f, "Parentheses", "k(W" + interpunct + "h)", null, 0, null, "kWh");
        unitFormatHelper(f, "Ohm", omega, null, 6, HERTZ, "6 Hz");
        unitFormatHelper(f, "mOhm", "m"+omega, null, 6, HERTZ, "6 Hz");
        unitFormatHelper(f, "Micro", micro+"V", null, 6, HERTZ, "uV");
        unitFormatHelper(f, "Ascii", "N m", null, 0, null, "Nm");
        unitFormatHelper(f, "Ascii", "Ohm", null, 0, null, "Nm");
        unitFormatHelper(f, "Ascii", "micro Ohm", null, 0, null, "Nm");
        sb.append("\n\n|===\n");
    }

    public void quantityFormatHelper(QuantityFormat f, String caseLabel, String input, String fmt, double expectVal, Unit expectUnit, String expectOut) {

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

        System.out.println("// exp=" + expectedQuantity + " equal=" + equal + " equivalent=" + equivalent + " reparse=" + reparsed);

        sb.append("\n|" + caseLabel);
        sb.append("\n|[`" + input + "`]");
        if (eParse == null) {
            sb.append("\n|" + parsed);
        } else {
            sb.append("\n|" + eParse);
        }
    }

    public void unitFormatHelper(UnitFormat f, String caseLabel, String input, String fmt, double expectVal, Unit expectUnit, String expectOut) {

        Unit expectedQuantity = null;
        Unit parsed = null;
        Unit reparsed = null;
        boolean equivalent = false;
        boolean equal = false;
        String restringed = null;
        Exception eParse = null;
        Exception eReparse = null;

        try {
            parsed = f.parse(input);
            if (expectUnit != null) {
                //               expectedQuantity = Quantities.getQuantity(expectVal, expectUnit);
                //             equivalent = expectedQuantity.isEquivalentTo(parsed);
                //           equal = expectedQuantity.equals(parsed);
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

        System.out.println("// exp=" + expectedQuantity + " equal=" + equal + " equivalent=" + equivalent + " reparse=" + reparsed);

        sb.append("\n|" + caseLabel);
        sb.append("\n|[`" + input + "`]");
        if (eParse == null) {
            sb.append("\n|" + parsed);
        } else {
            sb.append("\n|" + eParse);
        }
    }
}
