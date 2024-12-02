package pgdp.threads.start;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.DisplayName;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target")
@AllowThreads
public class BusinessPenguinBehaviorTest {
	DynamicMethod<?> run = new DynamicClass<>("pgdp.threads.start.Customer")
			.method(void.class, "run");

	@PublicTest
	@DisplayName(value = "Test Sell Start")
	public void testSellFish() {
		BusinessPenguin peter = new BusinessPenguin("Peter");
		BusinessPenguin paul = new BusinessPenguin("Paul");

		peter.setPartner(paul);
		paul.setPartner(peter);

		peter.sellFish(10);
		assertEquals(5, paul.getBalance(), "Sell Fish wrong");
		assertEquals(5, peter.getBalance(), "Sell Fish wrong");

		paul.sellFish(4);
		assertEquals(7, paul.getBalance(), "Sell Fish wrong");
		assertEquals(7, peter.getBalance(), "Sell Fish wrong");
	}

	@PublicTest
	@DisplayName(value = "Test Run Start")
	public void testRun() {
		BusinessPenguin peter = new BusinessPenguin("Peter");
		BusinessPenguin paul = new BusinessPenguin("Paul");

		peter.setPartner(paul);
		paul.setPartner(peter);

		Customer petersCustomer = new Customer(peter);
		Customer paulsCustomer = new Customer(paul);

		run.invokeOn(petersCustomer);
		assertEquals(5000, paul.getBalance(), "Run wrong");
		assertEquals(5000, peter.getBalance(), "Run wrong");

		run.invokeOn(paulsCustomer);
		assertEquals(10000, paul.getBalance(), "Run wrong");
		assertEquals(10000, peter.getBalance(), "Run wrong");
	}
}