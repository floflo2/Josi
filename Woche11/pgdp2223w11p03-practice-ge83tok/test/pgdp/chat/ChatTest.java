package pgdp.chat;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.PublicTest;

import static org.junit.jupiter.api.Assertions.fail;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target")
public class ChatTest {
	@PublicTest
	public void test1() {
		fail("Für diese Aufgabe gibt es keine automatischen Tests! Teste wie in der Aufgabe beschrieben selbst!");
	}
}
