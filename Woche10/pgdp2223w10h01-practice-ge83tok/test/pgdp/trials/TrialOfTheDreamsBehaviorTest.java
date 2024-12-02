package pgdp.trials;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.jupiter.HiddenTest;

@TestClassAnnotation
public class TrialOfTheDreamsBehaviorTest {

	@HiddenTest
	@DisplayName(value = "lockPick - Simple")
	@Order(1)
	void testLockPickSimple() {
		Function<byte[], Boolean> lock = in -> {
			return Arrays.equals(in, new byte[] { Byte.MIN_VALUE, Byte.MIN_VALUE, Byte.MIN_VALUE });
		};
		byte[] out = TrialOfTheDreams.lockPick(lock);
		assertTrue(lock.apply(out));
	}

	@HiddenTest
	@DisplayName(value = "lockPick - Hard")
	@Order(2)
	void testLockPickHard() {
		Function<byte[], Boolean> lock = in -> {
			return Arrays.equals(in, new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE, 0 });
		};
		byte[] out = TrialOfTheDreams.lockPick(lock);
		assertTrue(lock.apply(out));
	}

	@HiddenTest
	@DisplayName(value = "lockPick - Empty")
	@Order(3)
	void testLockPickEmpty() {
		Function<byte[], Boolean> lock = in -> {
			return Arrays.equals(in, new byte[] {});
		};
		byte[] out = TrialOfTheDreams.lockPick(lock);
		assertTrue(lock.apply(out));
	}

}
