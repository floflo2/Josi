package pgdp.threads;

import de.tum.in.test.api.jupiter.PublicTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.tum.in.test.api.BlacklistPath;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.PathType;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;

@MirrorOutput
@StrictTimeout(20)
@BlacklistPath(value = "**Test.{java,class}", type = PathType.GLOB)
@WhitelistPath("")
public class SynchronizedListBehaviorTest {
	static boolean error = false;

	@PublicTest
	public void test() {
		try {
			for (int c = 0; c < 100; c++) {
				SynchronizedList<String> list = new SynchronizedList<>();
				for (int i = 0; i < 5000; i++) {
					list.add(i, "" + i);
				}

				Thread[] readers = new Thread[5];
				for (int i = 0; i < readers.length; i++) {
					final int icopy = i * 1000;
					readers[i] = new Thread(() -> {
						for (int j = 0; j < 10000; j++) {
							try {
								list.get(icopy);
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (Exception e) {
								error = true;
								e.printStackTrace();
							}
						}
					});
				}
				Thread writer = new Thread(() -> {
					for (int j = 0; j < 10000; j++) {
						try {
							list.add(4, "4");
							list.remove(4);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (Exception e) {
							error = true;
							e.printStackTrace();
						}
					}
				});

				long startTime = System.currentTimeMillis();
				for (int i = 0; i < readers.length; i++) {
					readers[i].start();
				}
				writer.start();
				for (int i = 0; i < readers.length; i++) {
					readers[i].join();
				}
				writer.join();
				long duration = System.currentTimeMillis() - startTime;
				assertTrue(duration < 250, "Lock too slow");
			}
		} catch (Exception e) {
			fail("Not thread-safe");
		}
		assertFalse(error, "Not thread-safe");
	}

}