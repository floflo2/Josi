package pgdp.files;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.PublicTest;

import static org.junit.jupiter.api.Assertions.fail;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target")
public class ConcatTest {
	@PublicTest
	public void test() {
		fail("Für diese Aufgabe gibt es keine Unit-Tests. Teste dein Programm selbst auf eigens geschriebenen Dateien!");
	}
}
