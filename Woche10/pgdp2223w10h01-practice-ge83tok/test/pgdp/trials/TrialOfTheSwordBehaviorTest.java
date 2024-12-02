package pgdp.trials;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.jupiter.HiddenTest;
import pgdp.trials.TrialOfTheSword.FlatArray;

@TestClassAnnotation
public class TrialOfTheSwordBehaviorTest {

	@HiddenTest
	@DisplayName(value = "FlatArray 1")
	@Order(1)
	void testFlatArr1() {
		FlatArray<Integer> farr = new FlatArray<>(Integer.class, 2, 3, 4);
		int expected = 0;
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 4; z++) {
					assertEquals(expected++, farr.computeIndex(x, y, z));
				}
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "FlatArray 2")
	@Order(2)
	void testFlatArr2() {
		FlatArray<Integer> farr = new FlatArray<>(Integer.class, 6, 3, 5, 9);
		int expected = 0;
		for (int w = 0; w < 6; w++) {
			for (int x = 0; x < 3; x++) {
				for (int y = 0; y < 5; y++) {
					for (int z = 0; z < 9; z++) {
						assertEquals(expected++, farr.computeIndex(w, x, y, z));
					}
				}
			}
		}
	}

}
