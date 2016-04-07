package ru.spbau.bibaev.streams;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public final class FirstPartTasks {

    private FirstPartTasks() {
    }

    // Список названий альбомов
    public static List<String> allNames(Stream<Album> albums) {
        return albums.map(Album::getName).collect(Collectors.toList());
    }

    // Список названий альбомов, отсортированный лексикографически по названию
    public static List<String> allNamesSorted(Stream<Album> albums) {
        return albums.map(Album::getName).sorted().collect(Collectors.toList());
    }

    // Список треков, отсортированный лексикографически по названию, включающий все треки альбомов из 'albums'
    public static List<String> allTracksSorted(Stream<Album> albums) {
        Stream<Track> trackStream = albums.map(Album::getTracks).flatMap(Collection::stream);
        return trackStream.map(Track::getName).sorted().collect(Collectors.toList());
    }

    // Список альбомов, в которых есть хотя бы один трек с рейтингом более 95, отсортированный по названию
    public static List<Album> sortedFavorites(Stream<Album> albums) {
        Stream<Album> topAlbums = albums.filter(x -> x.getTracks().stream().anyMatch(y -> y.getRating() > 95));
        return topAlbums
                .sorted((l, r) -> l
                        .getName()
                        .compareTo(r.getName()))
                .collect(Collectors.toList());
    }

    // Сгруппировать альбомы по артистам
    public static Map<Artist, List<Album>> groupByArtist(Stream<Album> albums) {
        return albums.collect(Collectors.groupingBy(Album::getArtist));
    }

    // Сгруппировать альбомы по артистам (в качестве значения вместо объекта 'Album' использовать его имя)
    public static Map<Artist, List<String>> groupByArtistMapName(Stream<Album> albums) {
        return albums
                .collect(
                        Collectors.groupingBy(
                                Album::getArtist,
                                Collectors.mapping(
                                        Album::getName,
                                        Collectors.toList())));
    }

    // Число повторяющихся альбомов в потоке
    public static long countAlbumDuplicates(Stream<Album> albums) {
        // TODO: Try write it more pretty.
        int[] count = new int[1];
        count[0] = 0;
        long distinctCount = albums.peek(x -> count[0]++).distinct().count();

        return count[0] - distinctCount;
    }

    // Альбом, в котором максимум рейтинга минимален
    // (если в альбоме нет ни одного трека, считать, что максимум рейтинга в нем --- 0)
    public static Optional<Album> minMaxRating(Stream<Album> albums) {
        // TODO: make it more effective.
        Comparator<Album> maxRatingComparator = (left, right) -> Integer.compare(
                left.getTracks()
                        .stream()
                        .map(Track::getRating)
                        .collect(Collectors.maxBy(Integer::compare))
                        .orElse(0),
                right.getTracks()
                        .stream()
                        .map(Track::getRating)
                        .collect(Collectors.maxBy(Integer::compare))
                        .orElse(0));

        return albums.collect(Collectors.minBy(maxRatingComparator));
    }

    // Список альбомов, отсортированный по убыванию среднего рейтинга его треков (0, если треков нет)
    public static List<Album> sortByAverageRating(Stream<Album> albums) {
        // TODO: make more pretty code
        return albums.sorted((l, r) -> -Double.compare(
                l.getTracks()
                        .stream()
                        .mapToInt(Track::getRating)
                        .average()
                        .orElse(0.0),
                r.getTracks()
                        .stream()
                        .mapToInt(Track::getRating)
                        .average()
                        .orElse(0.0)))
                .collect(Collectors.toList());
    }

    // Произведение всех чисел потока по модулю 'modulo'
    // (все числа от 0 до 10000)
    public static int moduloProduction(IntStream stream, int modulo) {
        return stream.reduce(1, (left, right) -> (right * left) % modulo);
    }

    // Вернуть строку, состояющую из конкатенаций переданного массива, и окруженную строками "<", ">"
    // см. тесты
    public static String joinTo(String... strings) {
        return Arrays.stream(strings).collect(Collectors.joining(", ", "<", ">"));
    }

    // Вернуть поток из объектов класса 'clazz'
    public static <R> Stream<R> filterIsInstance(Stream<?> s, Class<R> clazz) {
        return s.filter(clazz::isInstance).map(clazz::cast);
    }
}
