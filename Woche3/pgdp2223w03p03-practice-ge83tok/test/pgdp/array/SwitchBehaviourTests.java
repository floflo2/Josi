package pgdp.array;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.PublicTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target/classes/pgdp/PinguLib.class")
public class SwitchBehaviourTests {

	/* ================================ Test locationOfLectureHall() ================================ */

	@PublicTest
	public void testLocationOfLectureHall() {
		assertAll(
				() -> testLocOfHall("MI HS 2", "Informatik"),
				() -> testLocOfHall("MW0001", "Maschinenwesen"),
				() -> testLocOfHall("MW2001", "Maschinenwesen"),
				() -> testLocOfHall("Interims I 1", "Informatik"),
				() -> testLocOfHall("Interims II 2", "Chemie"),
				() -> testLocOfHall("Carl-von-Linde", "Innenstadt"),
				() -> testLocOfHall("N1190", "Innenstadt"),

				() -> testLocOfHall("MI HS 1", "Unbekannter Hörsaal"),
				() -> testLocOfHall("MW 0001", "Unbekannter Hörsaal"),
				() -> testLocOfHall("MW 2001", "Unbekannter Hörsaal"),
				() -> testLocOfHall("Pingu-von-Weddell", "Unbekannter Hörsaal"),
				() -> testLocOfHall("", "Unbekannter Hörsaal")
		);
	}

	private static void testLocOfHall(String lectureHall, String expected) {
		String actual = Switch.locationOfLectureHall(lectureHall);
		String message = "Falscher Ort für Hörsaal '" + lectureHall + "' zurückgegeben.";
		assertEquals(expected, actual, message);
	}

	/* ================================ Test inclusions() ================================ */

	@PublicTest
	public void testInclusions() {
		assertAll(
				() -> testAllInclusions(
						new char[] {
								'C', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
								'L', 'M', 'N', 'S', 'T' ,'U', 'V', 'W',
								'X', 'Y', 'Z',
								'c', 'f', 'h', 'i', 'j', 'k', 'l', 'm',
								'n', 'r', 's', 't', 'u', 'v', 'w', 'x',
								'y', 'z',
								'1', '2', '3', '5', '7'
						},
						0
				),
				() -> testAllInclusions(
						new char[] {
								'A', 'D', 'O', 'P', 'Q', 'R',
								'a', 'b', 'd', 'e', 'o', 'p', 'q',
								'4', '6', '9'
						},
						1
				),
				() -> testAllInclusions(
						new char[] {
								'B',
								'g',
								'0', '8'
						},
						2
				),
				() -> testAllInclusions(
						new char[] {
								',', '.', '-', '_', '+', '*', '\'', '#',
								'?', '!', '"', '§', '$', '%', '&', '/',
								'\\', '(', ')', '{', '}', '[', ']', ' ',
								'企', '鵝', 'ペ', 'ン', 'ギ', 'ン', 'प', 'े',
								'ं', 'ग', 'ु', 'इ', 'न', 'п', 'и', 'н', 'г',
								'в', 'и', 'н'
						},
						-1
				)
		);
	}

	private static void testAllInclusions(char[] chars, int expected) {
		for(char c : chars) {
			int actual = Switch.inclusions(c);
			String message = "";
			if(expected == -1) {
				message = "Der übergebene Charakter ist weder lateinischer Buchstabe noch arabische Zahl. Es sollte also -1 zurückgegeben werden.";
			} else {
				message = "Falsche Anzahl an Einschlüssen für '" + c + "' berechnet."
						+ " Denke daran, dass die Einschlüsse im in der Aufgabenstellung verlinkten Font 'Consolas' gezählt werden müssen";
			}
			assertEquals(expected, actual, message);
		}
	}

	/* ================================ Test formatDate() ================================ */

	@PublicTest
	public void testFormatDate() {
		assertAll(
				() -> testFormatDate(1, 1, 1, "Montag, den 1. Januar"),
				() -> testFormatDate(1, 2, 1, "Montag, den 1. Februar"),
				() -> testFormatDate(1, 3, 1, "Montag, den 1. März"),
				() -> testFormatDate(1, 4, 1, "Montag, den 1. April"),
				() -> testFormatDate(1, 5, 1, "Montag, den 1. Mai"),
				() -> testFormatDate(1, 6, 1, "Montag, den 1. Juni"),
				() -> testFormatDate(1, 7, 1, "Montag, den 1. Juli"),
				() -> testFormatDate(1, 8, 1, "Montag, den 1. August"),
				() -> testFormatDate(1, 9, 1, "Montag, den 1. September"),
				() -> testFormatDate(1, 10, 1, "Montag, den 1. Oktober"),
				() -> testFormatDate(1, 11, 1, "Montag, den 1. November"),
				() -> testFormatDate(1, 12, 1, "Montag, den 1. Dezember"),

				() -> testFormatDate(1, 1, 1, "Montag, den 1. Januar"),
				() -> testFormatDate(1, 1, 2, "Dienstag, den 1. Januar"),
				() -> testFormatDate(1, 1, 3, "Mittwoch, den 1. Januar"),
				() -> testFormatDate(1, 1, 4, "Donnerstag, den 1. Januar"),
				() -> testFormatDate(1, 1, 5, "Freitag, den 1. Januar"),
				() -> testFormatDate(1, 1, 6, "Samstag, den 1. Januar"),
				() -> testFormatDate(1, 1, 7, "Sonntag, den 1. Januar"),

				() -> testFormatDate(13, 11, 5, "Freitag, den 13. November"),
				() -> testFormatDate(27, 8, 4, "Donnerstag, den 27. August"),
				() -> testFormatDate(29, 2, 7, "Sonntag, den 29. Februar"),

				() -> testFormatDate(25, 5, 8, "Undefiniertes Datum"),
				() -> testFormatDate(10, 9, -7, "Undefiniertes Datum"),
				() -> testFormatDate(8, 12, 0, "Undefiniertes Datum"),
				() -> testFormatDate(20, 13, 2, "Undefiniertes Datum"),
				() -> testFormatDate(22, -1, 5, "Undefiniertes Datum"),
				() -> testFormatDate(4, 0, 7, "Undefiniertes Datum")
		);
	}

	private static void testFormatDate(int day, int month, int weekday, String expected) {
		String actual = Switch.formatDate(day, month, weekday);
		String message = "Falsches Datumsformat für Tag " + day + ", Monat " + month
				+" und Wochentag " + weekday + ".";
		assertEquals(expected, actual, message);
	}

	/* ================================ Test daysInFebruary() ================================ */

	@PublicTest
	public void testDaysInFebruary() {
		for(int i = 1; i <= 10_000; i++) {
			testDaysInFebruary(i, (i % 400 == 0 || (i % 4 == 0 && i % 100 != 0) ? 29 : 28));
		}

		testDaysInFebruary(Integer.MAX_VALUE, 28);
		testDaysInFebruary(2_147_000_000, 29);
		testDaysInFebruary(2_147_000_500, 28);
		testDaysInFebruary(2_147_000_504, 29);
		testDaysInFebruary(2_147_000_550, 28);
	}

	private static void testDaysInFebruary(int year, int expected) {
		int actual = Switch.daysInFebruary(year);
		String message = "Falsche Anzahl an Tagen im Februar für das Jahr " + year + ".";
		if(year % 400 == 0) {
			message += " Durch 400 teilbare Jahre sind alle Schaltjahre.";
		} else if(year % 100 == 0) {
			message += " Durch 100 aber nicht durch 400 teilbare Jahre sind keine Schaltjahre.";
		} else if(year % 4 == 0) {
			message += " Durch 4 teilbare Jahre sind Schaltjahre, es sei denn sie sind durch 100 aber nicht 400 teilbar.";
		} else {
			message += " Nicht durch 4 teilbare Jahre sind nie Schaltjahre.";
		}
		assertEquals(expected, actual, message);
	}

	/* ================================ Test daysLeftInYearAfter() ================================ */

	@PublicTest
	public void testDaysInYearAfter() {
		assertAll(
				// Kein Schaltjahr
				() -> testDaysInYearAfter(1, 1, 2022, 364),
				() -> testDaysInYearAfter(1, 2, 2022, 333),
				() -> testDaysInYearAfter(1, 3, 2022, 305),
				() -> testDaysInYearAfter(1, 4, 2022, 274),
				() -> testDaysInYearAfter(1, 5, 2022, 244),
				() -> testDaysInYearAfter(1, 6, 2022, 213),
				() -> testDaysInYearAfter(1, 7, 2022, 183),
				() -> testDaysInYearAfter(1, 8, 2022, 152),
				() -> testDaysInYearAfter(1, 9, 2022, 121),
				() -> testDaysInYearAfter(1, 10, 2022, 91),
				() -> testDaysInYearAfter(1, 11, 2022, 60),
				() -> testDaysInYearAfter(1, 12, 2022, 30),

				() -> testDaysInYearAfter(10, 1, 2022, 355),
				() -> testDaysInYearAfter(10, 2, 2022, 324),
				() -> testDaysInYearAfter(10, 3, 2022, 296),
				() -> testDaysInYearAfter(10, 4, 2022, 265),
				() -> testDaysInYearAfter(10, 5, 2022, 235),
				() -> testDaysInYearAfter(10, 6, 2022, 204),
				() -> testDaysInYearAfter(10, 7, 2022, 174),
				() -> testDaysInYearAfter(10, 8, 2022, 143),
				() -> testDaysInYearAfter(10, 9, 2022, 112),
				() -> testDaysInYearAfter(10, 10, 2022, 82),
				() -> testDaysInYearAfter(10, 11, 2022, 51),
				() -> testDaysInYearAfter(10, 12, 2022, 21),

				() -> testDaysInYearAfter(31, 1, 2022, 334),
				() -> testDaysInYearAfter(28, 2, 2022, 306),
				() -> testDaysInYearAfter(31, 3, 2022, 275),
				() -> testDaysInYearAfter(30, 4, 2022, 245),
				() -> testDaysInYearAfter(31, 5, 2022, 214),
				() -> testDaysInYearAfter(30, 6, 2022, 184),
				() -> testDaysInYearAfter(31, 7, 2022, 153),
				() -> testDaysInYearAfter(31, 8, 2022, 122),
				() -> testDaysInYearAfter(30, 9, 2022, 92),
				() -> testDaysInYearAfter(31, 10, 2022, 61),
				() -> testDaysInYearAfter(30, 11, 2022, 31),
				() -> testDaysInYearAfter(31, 12, 2022, 0),

				// Schaltjahr
				() -> testDaysInYearAfter(1, 1, 2020, 365),
				() -> testDaysInYearAfter(1, 2, 2020, 334),
				() -> testDaysInYearAfter(1, 3, 2020, 305),

				() -> testDaysInYearAfter(10, 1, 2020, 356),
				() -> testDaysInYearAfter(10, 2, 2020, 325),
				() -> testDaysInYearAfter(10, 3, 2020, 296),

				() -> testDaysInYearAfter(31, 1, 2020, 335),
				() -> testDaysInYearAfter(28, 2, 2020, 307),
				() -> testDaysInYearAfter(31, 3, 2022, 275),

				// Modulo 100 == 0
				() -> testDaysInYearAfter(1, 1, 2100, 364),
				() -> testDaysInYearAfter(1, 2, 2100, 333),
				() -> testDaysInYearAfter(1, 3, 2100, 305),

				() -> testDaysInYearAfter(10, 1, 2100, 355),
				() -> testDaysInYearAfter(10, 2, 2100, 324),
				() -> testDaysInYearAfter(10, 3, 2100, 296),

				() -> testDaysInYearAfter(31, 1, 2100, 334),
				() -> testDaysInYearAfter(28, 2, 2100, 306),
				() -> testDaysInYearAfter(31, 3, 2100, 275),

				// Modulo 400 == 0
				() -> testDaysInYearAfter(1, 1, 2000, 365),
				() -> testDaysInYearAfter(1, 2, 2000, 334),
				() -> testDaysInYearAfter(1, 3, 2000, 305),

				() -> testDaysInYearAfter(10, 1, 2000, 356),
				() -> testDaysInYearAfter(10, 2, 2000, 325),
				() -> testDaysInYearAfter(10, 3, 2000, 296),

				() -> testDaysInYearAfter(31, 1, 2000, 335),
				() -> testDaysInYearAfter(28, 2, 2000, 307),
				() -> testDaysInYearAfter(31, 3, 2002, 275),

				// Falscher Monat
				() -> testDaysInYearAfter(31, 13, 2000, -1),
				() -> testDaysInYearAfter(28, -1, 2000, -1),
				() -> testDaysInYearAfter(31, 0, 2002, -1)
		);
	}

	private static void testDaysInYearAfter(int day, int month, int year, int expected) {
		int actual = Switch.daysLeftInYearAfter(day, month, year);
		String message = "Falsches Output für den " + day + "." + month + "." + year + " !";
		assertEquals(expected, actual, message);
	}

}
