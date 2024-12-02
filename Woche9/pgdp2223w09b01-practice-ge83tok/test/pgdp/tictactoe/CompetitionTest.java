package pgdp.tictactoe;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.jupiter.PublicTest;

import static org.junit.jupiter.api.Assertions.*;

@W09B01
@AllowThreads
@TestMethodOrder(OrderAnnotation.class)
public class CompetitionTest {
    private static long shortestTimeFirst = Long.MAX_VALUE;
    private static long longestTimeFirst;
    private static long completeTimeFirst;
    private static long shortestTimeSecond = Long.MAX_VALUE;
    private static long longestTimeSecond;
    private static long completeTimeSecond;
    private static int numberOfGamesPlayed;

    @StrictTimeout(20)
    @PublicTest
    @Order(1)
    public void testCompetition() {
        DynamicClass<?> competitionClass = DynamicClass.toDynamic("pgdp.tictactoe.ai.CompetitionAI");
        if (!competitionClass.exists()) {
            fail("Die CompetitionAI wurde nicht gefunden. Die Pinguine sind traurig, dass du nicht mit ihnen spielen willst :(");
        } else if (!PenguAI.class.isAssignableFrom(competitionClass.toClass())) {
            fail("Die CompetitionAI ist keine Unterklasse von PenguAI");
        }

        int wins = 0;
        for (int i = 0; i < 10; i++) {
            PenguAI competitionAI = (PenguAI) competitionClass.constructor().newInstance();
            GameForTesting gameFirst = new GameForTesting(competitionAI, new SimpleAIForCompetition(), 9);
            GameForTesting gameSecond = new GameForTesting(new SimpleAIForCompetition(), competitionAI, 9);
            playGameFirst(gameFirst);
            playGameSecond(gameSecond);
            if (gameFirst.getWinner() == competitionAI) {
                wins++;
            }
            if (gameSecond.getWinner() == competitionAI) {
                wins++;
            }
            numberOfGamesPlayed++;
        }

        assertTrue(wins >= 15, "Deine KI hat nur " + wins + " Spiele gewonnen.\n"
                + "Dieser Test gibt keine Punkte, du musst also nicht am Wettbewerb teilnehmen für die Punkte!");
    }

    @PublicTest
    @Order(2)
    public void printTime() {
        assertNotEquals(0, numberOfGamesPlayed, "Du hast keine Spiele für den Wettbewerb gespielt");
        fail("Dieser Test wird immer failen, er ist dafür da, damit du weißt, wie schnell deine KI auf Artemis ist.\n"
                + "Jede Zeit ist in Millisekunden angegeben.\n" + "Statistiken, wenn du der erste Spieler bist:\n"
                + "Kürzeste Zeit für ein Spiel: " + shortestTimeFirst + "\n" + "Längste Zeit für ein Spiel: "
                + longestTimeFirst + "\n" + "Gesamte Zeit für 10 Spiele: " + completeTimeFirst + " (Durchschnitt: "
                + completeTimeFirst / numberOfGamesPlayed + ")\n" + "Statistiken, wenn du der zweite Spieler bist:\n"
                + "Kürzeste Zeit für ein Spiel: " + shortestTimeSecond + "\n" + "Längste Zeit für ein Spiel: "
                + longestTimeSecond + "\n" + "Gesamte Zeit für 10 Spiele: " + completeTimeSecond + " (Durchschnitt: "
                + completeTimeSecond / numberOfGamesPlayed + ")\nInsgesamt wurden " + numberOfGamesPlayed * 2 + " Spiele gespielt\n");
    }

    private void playGameFirst(GameForTesting game) {
        long startTime = System.currentTimeMillis();
        game.playGame();
        long time = System.currentTimeMillis() - startTime;
        shortestTimeFirst = Math.min(shortestTimeFirst, time);
        longestTimeFirst = Math.max(longestTimeFirst, time);
        completeTimeFirst += time;
    }

    private void playGameSecond(GameForTesting game) {
        long startTime = System.currentTimeMillis();
        game.playGame();
        long time = System.currentTimeMillis() - startTime;
        shortestTimeSecond = Math.min(shortestTimeSecond, time);
        longestTimeSecond = Math.max(longestTimeSecond, time);
        completeTimeSecond += time;
    }
}
