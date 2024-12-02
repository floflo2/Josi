package pgdp.poly;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.PublicTest;
import pgdp.poly.HPCPoly.ASIC;
import pgdp.poly.HPCPoly.ASICBoard;
import pgdp.poly.HPCPoly.ComputeCluster;
import pgdp.poly.HPCPoly.Computer;
import pgdp.poly.HPCPoly.FPGA;
import pgdp.poly.HPCPoly.Master;
import pgdp.poly.HPCPoly.Monitor;
import pgdp.poly.HPCPoly.SuperMUC;

@TestClassAnnotation
public class BehaviourTest {

	private static boolean templateModified = true;
	private static ModificationChecker mc;

	@BeforeAll
	public static void checkForModifications() {
		mc = new ModificationChecker("./assignment/src/pgdp/poly/HPCPoly.java", "./test/pgdp/poly/OracleTMP.txt");

		// for local testing:
		// "../pgdp2223w08b01-solution/src/pgdp/poly/HPCPoly.java"
		// "./test/pgdp/poly/OracleTMP.txt"

		// for artemis testing:
		// "./assignment/src/pgdp/poly/HPCPoly.java"
		// "./test/pgdp/poly/OracleTMP.txt"

		if (mc.completed() && mc.passed()) {
			templateModified = false;
		}
	}

	private void assertNotModified() {
		if (templateModified) {
			fail("You modified the template in a prohibited section. The test won't be executed.\n" + mc.getDiff());
		}
	}

	/*
	 * getComputer
	 */

	@PublicTest
	@DisplayName(value = "getComputer - not null")
	@Order(1)
	public void testGetComputerNotNull() {
		assertNotModified();
		final Computer c = HPCPoly.getComputer();
		assertNotNull(c, "Computer should not be null.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - special message")
	@Order(2)
	public void testGetComputerMsg(IOTester iot) {
		assertNotModified();
		final Computer student = HPCPoly.getComputer();
		assertNotNull(student);
		final Computer c = new Computer() {
			@Override
			public void connectByCopper(Computer c) {
				fail("The Computer invoked a other method. This behavior is not to specs.");
			}
		};
		student.connectByCopper(c);
		iot.out().assertLinesMatch("The output doesn't match.", "Computer connected to Computer by copper.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - cast to ComputeCluster")
	@Order(3)
	public void testGetComputerCastComputeCluster() {
		assertNotModified();
		final Computer c = HPCPoly.getComputer();
		assertNotNull(c);
		assertThrows(ClassCastException.class, () -> {
			@SuppressWarnings("unused")
			final ComputeCluster<?> cc = (ComputeCluster<?>) c;
		}, "This instance of Computer should not be castable to ComputeCluster.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - cast to ASICBoard")
	@Order(4)
	public void testGetComputerCastASICBoard() {
		assertNotModified();
		final Computer c = HPCPoly.getComputer();
		assertNotNull(c);
		assertThrows(ClassCastException.class, () -> {
			@SuppressWarnings("unused")
			final ASICBoard<?> ab = (ASICBoard<?>) c;
		}, "This instance of Computer should not be castable to ASICBoard.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - cast to FPGA")
	@Order(5)
	public void testGetComputerCastFPGA() {
		assertNotModified();
		final Computer c = HPCPoly.getComputer();
		assertNotNull(c);
		assertThrows(ClassCastException.class, () -> {
			@SuppressWarnings("unused")
			final FPGA<?, ?> f = (FPGA<?, ?>) c;
		}, "This instance of Computer should not be castable to FPGA.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - cast to Monitor")
	@Order(6)
	public void testGetComputerCastMonitor() {
		assertNotModified();
		final Computer c = HPCPoly.getComputer();
		assertNotNull(c);
		assertThrows(ClassCastException.class, () -> {
			@SuppressWarnings("unused")
			final Monitor<?> mo = (Monitor<?>) c;
		}, "This instance of Computer should not be castable to Monitor.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - cast to Master")
	@Order(7)
	public void testGetComputerCastMaster() {
		assertNotModified();
		final Computer c = HPCPoly.getComputer();
		assertNotNull(c);
		assertThrows(ClassCastException.class, () -> {
			@SuppressWarnings("unused")
			final Master ma = (Master) c;
		}, "This instance of Computer should not be castable to Master.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - cast to SuperMUC")
	@Order(8)
	public void testGetComputerCastSuperMUC() {
		assertNotModified();
		final Computer c = HPCPoly.getComputer();
		assertNotNull(c);
		assertThrows(ClassCastException.class, () -> {
			@SuppressWarnings("unused")
			final SuperMUC cc = (SuperMUC) c;
		}, "This instance of Computer should not be castable to SuperMUC.");
	}

	@PublicTest
	@DisplayName(value = "getComputer - grading")
	@Order(9)
	public void testGetComputerGrading(IOTester iot) {
		try {
			testGetComputerNotNull();
			iot.reset();
			testGetComputerMsg(iot);
			testGetComputerCastComputeCluster();
			testGetComputerCastASICBoard();
			testGetComputerCastFPGA();
			testGetComputerCastMonitor();
			testGetComputerCastMaster();
			testGetComputerCastSuperMUC();
		} catch (AssertionError ae) {
			fail("At least one of the required tests failed.");
		}
	}

	/*
	 * getSpecialSuperMUC
	 */

	@PublicTest
	@DisplayName(value = "getSpecialSuperMUC - not null")
	@Order(10)
	public void testGetSpecialSuperMUCNotNull() {
		assertNotModified();
		final Computer c = new Computer() {
			@Override
			public void connectByCopper(Computer c) {
				fail("The SuperMUC invoked a other method. This behavior is not to specs.");
			}
		};
		final SuperMUC s = HPCPoly.getSpecialSuperMUC(c);
		assertNotNull(s, "Special SuperMUC should not be null");
	}

	@PublicTest
	@DisplayName(value = "getSpecialSuperMUC - special message")
	@Order(11)
	public void testGetSpecialSuperMUCMsgSpecial(IOTester iot) {
		assertNotModified();
		final String rstr = "Output of the given instance of Computer." + new Random().nextInt();
		final Computer c = new Computer() {
			@Override
			public void connectByCopper(Computer c) {
				System.out.println(rstr);
			}
		};
		final SuperMUC s = HPCPoly.getSpecialSuperMUC(c);
		assertNotNull(s);
		final Monitor<Master> mo = new Monitor<Master>();
		s.connectByFiber(mo);
		iot.out().assertLinesMatch("The output doesn't match for argument type Monitor<Master>.",
				"Special SuperMUC connected to Monitor by fiber.", rstr);
		iot.reset();
		final Master ma = new Master();
		s.connectByFiber(ma);
		iot.out().assertLinesMatch("The output doesn't match for argument type Master.",
				"Special SuperMUC connected to Monitor by fiber.", rstr);
		iot.reset();
		final SuperMUC su = new SuperMUC();
		s.connectByFiber(su);
		iot.out().assertLinesMatch("The output doesn't match for argument type SuperMUC.",
				"Special SuperMUC connected to Monitor by fiber.", rstr);
	}

	@PublicTest
	@DisplayName(value = "getSpecialSuperMUC - normal message")
	@Order(12)
	public void testGetSpecialSuperMUCMsgNormal(IOTester iot) {
		assertNotModified();
		final SuperMUC s = new SuperMUC();
		final Monitor<Master> mo = new Monitor<>();
		s.connectByFiber(mo);
		iot.out().assertLinesMatch("The output doesn't match for argument type Monitor<Master>.",
				"SuperMUC connected to Monitor by fiber.");
		iot.reset();
		final String rstr = "Output of the given instance of Computer." + new Random().nextInt();
		final SuperMUC ss = HPCPoly.getSpecialSuperMUC(new Computer() {
			@Override
			public void connectByCopper(Computer c) {
				System.out.println(rstr);
			}
		});
		assertNotNull(ss);
		final Monitor<?> m = new Monitor<>();
		ss.connectByFiber(m);
		iot.out().assertLinesMatch("The output doesn't match for argument type Monitor<?>.",
				"Monitor connected to ComputeCluster by fiber.");
		iot.reset();
		final Monitor<?> ma = new Master();
		ss.connectByFiber(ma);
		iot.out().assertLinesMatch("The output doesn't match for argument type Monitor<?>.",
				"Monitor connected to ComputeCluster by fiber.");
		iot.reset();
		final Monitor<?> su = new SuperMUC();
		ss.connectByFiber(su);
		iot.out().assertLinesMatch("The output doesn't match for argument type Monitor<?>.",
				"Monitor connected to ComputeCluster by fiber.");
	}

	@PublicTest
	@DisplayName(value = "getSpecialSuperMUC - grading")
	@Order(13)
	public void testGetSpecialSuperMUCGrading(IOTester iot) {
		try {
			testGetSpecialSuperMUCNotNull();
			iot.reset();
			testGetSpecialSuperMUCMsgSpecial(iot);
			iot.reset();
			testGetSpecialSuperMUCMsgNormal(iot);
		} catch (AssertionError ae) {
			fail("At least one of the required tests failed.");
		}
	}

	/*
	 * getFPGAasASIC
	 */

	@PublicTest
	@DisplayName(value = "getFPGAasASIC - not null")
	@Order(14)
	public void testGetFPGAasASICNotNull() {
		assertNotModified();
		final ASIC<?> a = HPCPoly.getFPGAasASIC();
		assertNotNull(a, "The ASIC should not be null.");
	}

	@PublicTest
	@DisplayName(value = "getFPGAasASIC - cast to FPGA")
	@Order(15)
	public void testGetFPGAasASICCastFPGA() {
		assertNotModified();
		final ASIC<?> a = HPCPoly.getFPGAasASIC();
		assertNotNull(a);
		try {
			@SuppressWarnings({ "unused", "unchecked" })
			final FPGA<?, ASICBoard<?>> f = (FPGA<?, ASICBoard<?>>) a;
		} catch (final ClassCastException cce) {
			fail("This instance of ASIC should be castable to FPGA.");
		}
	}

	@PublicTest
	@DisplayName(value = "getFPGAasASIC - grading")
	@Order(16)
	public void testGetFPGAasASICGrading() {
		try {
			testGetFPGAasASICNotNull();
			testGetFPGAasASICCastFPGA();
		} catch (AssertionError ae) {
			fail("At least one of the required tests failed.");
		}
	}

	/*
	 * Ambiguous connection
	 */

	@PublicTest
	@DisplayName(value = "Ambiguous connection - ASICBoard.connectByFiber(ASICBoard, ASIC)")
	@Order(17)
	public void testAmbiguousASICBoardASIC(IOTester iot) {
		assertNotModified();
		final ASIC<ComputeCluster<Computer>> a = new ASIC<>();
		final ASICBoard<Computer> ab = new ASICBoard<>(null);
		ab.connectByFiber(ab, a);
		iot.out().assertLinesMatch(
				"The behaviour of ASICBoard.connectByFiber(ASICBoard, ASIC) should not have been modified.",
				"ASICBoard connected to ASICBoard and ASIC by fiber.");
	}

	@PublicTest
	@DisplayName(value = "Ambiguous connection - ASICBoard.connectByFiber(ASIC, ASICBoard)")
	@Order(18)
	public void testAmbiguousASICASICBoard(IOTester iot) {
		assertNotModified();
		final ASIC<ComputeCluster<Computer>> a = new ASIC<>();
		final ASICBoard<Computer> ab = new ASICBoard<>(null);
		final DynamicClass<?> clazz = DynamicClass.toDynamic(HPCPoly.ASIC.class);
		final DynamicMethod<?> method = clazz.method(void.class, "connectByFiber", HPCPoly.ASIC.class,
				HPCPoly.ASICBoard.class);
		if (method.exists()) { // check if ab.connectByFiber(a, ab) can be called
			method.invokeOn(ab, a, ab);
		} else {
			fail("Method not found.");
		}
		iot.out().assertLinesMatch("The behaviour of ASICBoard.connectByFiber(ASIC, ASICBoard) is not to specs.",
				"ASIC connected to ASIC and ASICBoard by fiber.");
	}

	@PublicTest
	@DisplayName(value = "Ambiguous connection - grading")
	@Order(19)
	public void testAmbiguousGrading(IOTester iot) {
		try {
			iot.reset();
			testAmbiguousASICBoardASIC(iot);
			iot.reset();
			testAmbiguousASICASICBoard(iot);
		} catch (AssertionError ae) {
			fail("At least one of the required tests failed.");
		}
	}

	/*
	 * Breaking infinity
	 */

	@PublicTest
	@DisplayName(value = "Breaking infinity - SuperMUC.connectByCopper(SuperMUC)")
	@Order(20)
	public void testInfinity1(IOTester iot) {
		assertNotModified();
		final SuperMUC s = new SuperMUC();
		try {
			s.connectByCopper(s);
		} catch (final StackOverflowError e) { // only useful for local testing
			fail("The behaviour of SuperMUC.connectByCopper(SuperMUC) is not to specs. (StackOverflow)");
		}
		iot.out().assertLinesMatch("The behaviour of SuperMUC.connectByCopper(SuperMUC) is not to specs.",
				"SuperMUC connected to SuperMUC by copper.", "Monitor connected to Computer by copper.");
	}

	@PublicTest
	@DisplayName(value = "Breaking infinity - SuperMUC.connectByCopper(Master)")
	@Order(21)
	public void testInfinity2(IOTester iot) {
		assertNotModified();
		final Master m = new Master() {
			@Override
			public void connectByCopper(Master ma) {
				fail("The behaviour of SuperMUC.connectByCopper(Master) is not to specs. (Infinite Loop)");
			}
		};
		final SuperMUC s = new SuperMUC();
		s.connectByCopper(m);
		iot.out().assertLinesMatch("The behaviour of SuperMUC.connectByCopper(Master) is not to specs.",
				"SuperMUC connected to Master by copper.");
	}

	@PublicTest
	@DisplayName(value = "Breaking infinity - Master.connectByCopper(SuperMUC)")
	@Order(22)
	public void testInfinity3(IOTester iot) {
		assertNotModified();
		final Master m = new Master() {
			int[] calls = new int[1];

			@Override
			public void connectByCopper(Master ma) {
				if (calls[0] > 0) {
					fail("The behaviour of SuperMUC.connectByCopper(Master) is not to specs. (Infinite Loop)");
				}
				super.connectByCopper(ma);
			}
		};
		final SuperMUC s = new SuperMUC();
		m.connectByCopper(s);
		iot.out().assertLinesMatch("The behaviour of Master.connectByCopper(SuperMUC) is not to specs.",
				"Master connected to Master by copper.", "SuperMUC connected to Master by copper.");
	}

	@PublicTest
	@DisplayName(value = "Breaking infinity - Master.connectByCopper(Master)")
	@Order(23)
	public void testInfinity4(IOTester iot) {
		assertNotModified();
		final String rstr = "Correct method called." + new Random().nextInt();
		final Master m = new Master();
		final Master mockedMaster = new Master() {
			@Override
			public void connectByCopper(Master ma) {
				System.out.println(rstr);
			}
		};
		m.connectByCopper(mockedMaster);
		iot.out().assertLinesMatch("The behaviour of Master.connectByCopper(Master) is not to specs.",
				"Master connected to Master by copper.", rstr);
	}

	@PublicTest
	@DisplayName(value = "Breaking infinity - grading")
	@Order(24)
	public void testInfinityGrading(IOTester iot) {
		try {
			iot.reset();
			testInfinity1(iot);
			iot.reset();
			testInfinity2(iot);
			iot.reset();
			testInfinity3(iot);
			iot.reset();
			testInfinity4(iot);
		} catch (AssertionError ae) {
			fail("At least one of the required tests failed.");
		}
	}

	/*
	 * There will be no Exceptions
	 */

	@PublicTest
	@DisplayName(value = "No Exceptions - FPGA.connectByCopper(FPGA)")
	@Order(25)
	public void testNoException(IOTester iot) {
		assertNotModified();
		final String rstr = "Output of the given instance of Computer." + new Random().nextInt();
		final ComputeCluster<ASICBoard<?>> y = new ComputeCluster<>() {

			@Override
			public void connectByCopper(Computer x) {
				System.out.println(rstr);
			}
		};
		final FPGA<?, ASICBoard<?>> f = new FPGA<>(y);
		f.set(null);
		try {
			f.connectByCopper(f);
		} catch (final NullPointerException npe) {
			fail("The behaviour of FPGA.connectByCopper(FPGA) is not to specs. (NullPointerException)");
		}
		iot.out().assertLinesMatch("The behaviour of FPGA.connectByCopper(FPGA) is not to specs. (Output)",
				"FPGA connected to FPGA by copper.", rstr);
	}

	@PublicTest
	@DisplayName(value = "No Exceptions - grading")
	@Order(26)
	public void testNoExceptionGrading(IOTester iot) {
		try {
			iot.reset();
			testNoException(iot);
		} catch (AssertionError ae) {
			fail("At least one of the required tests failed.");
		}
	}

}
