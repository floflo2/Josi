package pgdp.pingutrip;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BehaviorOracle {

    public static Stream<WayPoint> readWayPointsOracle(String pathToWayPoints) {
        try {
            return Files.lines(Path.of(pathToWayPoints))
                    .takeWhile(line -> !line.equals("---"))
                    .filter(line -> !line.startsWith("//"))
                    .map(BehaviorOracle::ofString);
        } catch (IOException ioe) {
            return Stream.empty();
        }
    }

    public static Stream<OneWay> transformToWaysOracle(List<WayPoint> wayPoints) {
        return IntStream.range(0, wayPoints.size() - 1)
                .mapToObj(i -> new OneWay(wayPoints.get(i), wayPoints.get(i + 1)));
    }

    public static double pathLengthOracle(Stream<OneWay> oneWays) {
        return oneWays.mapToDouble(BehaviorOracle::getLength).sum();
    }

    public static List<OneWay> kidFriendlyTripOracle(List<OneWay> oneWays) {
        double average = oneWays.stream().mapToDouble(BehaviorOracle::getLength).average().orElse(0.0);
        return oneWays.stream().takeWhile(way -> getLength(way) <= average).toList();
    }

    public static WayPoint furthestAwayFromHomeOracle(Stream<WayPoint> wayPoints, WayPoint home) {
        return wayPoints.max(Comparator.comparingDouble(wayOne -> distanceTo(wayOne, home))).orElse(home);
    }

    public static boolean onTheWayOracle(Stream<OneWay> oneWays, WayPoint visit) {
        return oneWays.anyMatch(oneWay -> isOnPath(visit, oneWay));
    }

    public static String prettyDirectionsOracle(Stream<OneWay> oneWays) {
        return oneWays.map(BehaviorOracle::prettyPrint).collect(Collectors.joining("\n"));
    }

    // Copied here so changes on the OneWay and WayPoint classes won't affect the tests.

    public static double getLength(OneWay way) {
        return way.from().distanceTo(way.to());
    }

    public static int getDirection(OneWay way) {
        return (int) ((Math.atan2(way.to().y() - way.from().y(), way.to().x() - way.from().x())
                * (180 / Math.PI) + 360) % 360);
    }

    public static String prettyPrint(OneWay way) {
        return Math.round((getLength(way) / 0.7)) + " Schritte Richtung " + getDirection(way) + " Grad.";
    }

    public static boolean isOnPath(WayPoint visit, OneWay way) {
        double fromToPoint = way.from().distanceTo(visit);
        double toToPoint = way.to().distanceTo(visit);
        return Math.abs(fromToPoint + toToPoint - getLength(way)) < 0.001;
    }

    public static WayPoint ofString(String string) {
        String[] split = string.split("([;:])");
        double x = Double.parseDouble(split[0].trim());
        double y = Double.parseDouble(split[1].trim());
        return new WayPoint(x, y);
    }

    public static double distanceTo(WayPoint from, WayPoint to) {
        return Math.sqrt(Math.pow((from.x() - to.x()), 2) + Math.pow((from.y() - to.y()), 2));
    }


}
