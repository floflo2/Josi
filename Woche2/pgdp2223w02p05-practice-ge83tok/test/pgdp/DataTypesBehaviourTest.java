package pgdp;

import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.jupiter.PublicTest;

@MirrorOutput
@StrictTimeout(2)
public class DataTypesBehaviourTest {

	private static Stream<Arguments> smallestTypePossibleArguments() {
		return Stream.of(Arguments.of(Double.MAX_VALUE), Arguments.of(Long.MAX_VALUE), Arguments.of(Integer.MAX_VALUE),
				Arguments.of(Short.MAX_VALUE), Arguments.of(Byte.MAX_VALUE), Arguments.of(0));
	}

	private static Function<Double, String> sTPA = (i) -> {
		double in = i;
		if (in > Long.MAX_VALUE) {
			// only double
			return in + " lässt sich nicht als reine Ganzzahl mit einem primitiven Datentyp speichern!";
		} else if (in > Integer.MAX_VALUE) {
			// only long
			return in + " lässt sich in einem long speichern: " + (long) in;
		} else if (in > Short.MAX_VALUE) {
			// int
			return in + " lässt sich in einem int speichern: " + (int) in;
		} else if (in > Byte.MAX_VALUE) {
			// short
			return in + " lässt sich in einem short speichern: " + (short) in;
		} else {
			// byte
			return in + " lässt sich in einem byte speichern: " + (byte) in;
		}
	};

	@Public
	@ParameterizedTest
	@MethodSource("smallestTypePossibleArguments")
	public void smallestTypePossibleTest(double in, IOTester iot) {
		DataTypes.smallestTypePossible(in);
		if (!iot.out().getOutputAsString().equals(sTPA.apply(in))) {
			TestUtils.privilegedFail("Test failed: smallestTypePossible produziert falsche Ausgabe für " + in
					+ ".\nExpected: [" + sTPA.apply(in) + "]\nbut got: " + iot.out().getLinesAsString());
		}
	}

	@PublicTest
	public void calculateBitsForLongTest(IOTester iot) {
		DataTypes.calculateBitsForLong();
		if (!iot.out().getOutputAsString().equals("Einem long stehen 64 Bits zur Verfügung.")) {
			TestUtils.privilegedFail(
					"Test failed: calculateBitsForLong produziert falsche Ausgabe.\nExpected: [Einem long stehen 64 Bits zur Verfügung.]\nbut got: "
							+ iot.out().getLinesAsString());
		}
	}
}
