package pgdp.pingutrip;

import java.util.List;
import java.util.stream.Stream;

final public class PinguTrip {

    // To hide constructor in utility class.
    private PinguTrip() {}

    public static Stream<WayPoint> readWayPoints(String pathToWayPoints) {
        // TODO: Task 1
        return null;
    }

    public static Stream<OneWay> transformToWays(List<WayPoint> wayPoints) {
        // TODO: Task 2
        return null;
    }

    public static double pathLength(Stream<OneWay> oneWays) {
        // TODO: Task 3
        return 0.0;
    }

    public static List<OneWay> kidFriendlyTrip(List<OneWay> oneWays) {
        // TODO: Task 4
        return null;
    }

    public static WayPoint furthestAwayFromHome(Stream<WayPoint> wayPoints, WayPoint home) {
        // TODO: Task 5
        return null;
    }

    public static boolean onTheWay(Stream<OneWay> oneWays, WayPoint visit) {
        // TODO: Task 6
        return false;
    }

    public static String prettyDirections(Stream<OneWay> oneWays) {
        // TODO: Task 7
        return "";
    }

    public static void main(String[] args) {
        List<WayPoint> wayPoints = readWayPoints("test_paths/path.txt").toList();
        // List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2));

        List<OneWay> oneWays = transformToWays(wayPoints).toList();
        // List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)));

        double length = pathLength(oneWays.stream());
        // 17.230 ...

        List<OneWay> kidFriendly = kidFriendlyTrip(oneWays);
        // List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)));

        WayPoint furthest = furthestAwayFromHome(wayPoints.stream(), wayPoints.get(0));
        // new WayPoint(19.1, 3.2);

        boolean onTheWay = onTheWay(oneWays.stream(), new WayPoint(0.0, 0.0));
        // false

        onTheWay = onTheWay(oneWays.stream(), new WayPoint(19.1, 3.2));
        // true

        String directions = prettyDirections(oneWays.stream());
        // "25 Schritte Richtung 331 Grad."
    }

}
