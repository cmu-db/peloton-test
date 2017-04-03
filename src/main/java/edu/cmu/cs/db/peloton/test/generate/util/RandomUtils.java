package edu.cmu.cs.db.peloton.test.generate.util;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tianyuli on 4/1/17.
 */
public final class RandomUtils {
    public static <E> E randomElement(List<E> elems, Random random) {
        return elems.get(random.nextInt(elems.size()));
    }

    public static <E> E randomElement(Set<E> elems, Random random) {
        return randomElement(elems.stream(), elems.size(), random);
    }

    public static <E> E randomElement(Stream<E> elems, int count, Random random) {
        return elems.skip(random.nextInt(count))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static <E> E randomElement(Stream<E> elems, Random random) {
        return randomElement(elems.collect(Collectors.toList()), random);
    }
}
