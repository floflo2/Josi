package pgdp.trains;

import pgdp.trains.connections.Station;
import pgdp.trains.connections.TrainConnection;
import pgdp.trains.connections.TrainStop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BehaviorOracle {

    public static Stream<TrainConnection> cleanDatasetOracle(Stream<TrainConnection> connections) {
        return connections.distinct()
                .sorted(Comparator.comparing(ts -> ts.getFirstStop().scheduled()))
                .map(c -> c.withUpdatedStops(c.stops().stream()
                        .filter(s -> s.kind() != TrainStop.Kind.CANCELLED).toList()));
    }

    public static TrainConnection worstDelayedTrainOracle(Stream<TrainConnection> connections) {
        return connections.max(Comparator.comparingInt(o ->
                o.stops().stream().mapToInt(TrainStop::getDelay).max().orElse(0))).orElse(null);
    }

    public static double percentOfKindStopsOracle(Stream<TrainConnection> connections, TrainStop.Kind kind) {
        return connections.flatMap(tr -> tr.stops().stream())
                .mapToInt(ts -> ts.kind() == kind ? 1 : 0).average().orElse(0.0) * 100;
    }

    public static double averageDelayAtOracle(Stream<TrainConnection> connections, Station station) {
        return connections.flatMap(tr -> tr.stops().stream()).filter(ts -> ts.station().equals(station))
                .mapToInt(TrainStop::getDelay).average().orElse(0.0);
    }

    public static Map<String, Double> delayComparedToTotalTravelTimeByTransportOracle(Stream<TrainConnection> connections) {
        return connections
                .collect(Collectors.groupingBy(TrainConnection::type))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, lt -> {
                    double totalScheduled = lt.getValue().stream()
                            .mapToInt(TrainConnection::totalTimeTraveledScheduled).sum();
                    double totalActual = lt.getValue().stream()
                            .mapToInt(tc -> Math.max(tc.totalTimeTraveledActual(), tc.totalTimeTraveledScheduled())).sum();
                    return ((totalActual - totalScheduled) * 100) / totalActual;
                }));
    }

    public static Map<Integer, Double> averageDelayByHourOracle(Stream<TrainConnection> connections) {
        return connections.flatMap(tc -> tc.stops().stream())
                .collect(Collectors.groupingBy(ts -> ts.actual().getHour()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, lt -> lt
                        .getValue().stream()
                        .mapToInt(TrainStop::getDelay)
                        .average().orElse(0.0)));
    }

    // Copied here to avoid changes in classes destroy tests.
    public static Stream<TrainConnection> loadFileOracle(String pathToFile) {
        try {
            String content = Files.readString(Path.of(pathToFile));
            return TrainConnection.parseFromString(content);
        } catch (IOException e) {
            System.err.println("Es konnte nicht aus der Datei gelesen werden!");
            return Stream.empty();
        }
    }

}
