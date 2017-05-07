package edu.cmu.cs.db.peloton.test.generate.util;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.BOOLEAN;
import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.NUMERIC;
import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.VARCHAR;

/**
 * Created by tianyuli on 4/1/17.
 */
public final class RandomUtils {
    private static final double CONSTANT_PROB = 0.2;

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

    public static String constantOf(Ast.ExpressionType type, Random random) {
        switch (type) {
            case NUMERIC:
                return Double.toString(random.nextDouble());
            case VARCHAR:
                int length = random.nextInt(10);
                StringBuilder result = new StringBuilder("\'");
                for (int i = 0; i < length; i++) {
                    result.append((char) (random.nextInt(26) + 'a'));
                }
                result.append("\'");
                return result.toString();
            case BOOLEAN:
                return random.nextBoolean() ? "TRUE" : "FALSE";
            case ANY:
                return constantOf(randomElement(Arrays.asList(NUMERIC, VARCHAR, BOOLEAN), random), random);
            default:
                throw new IllegalArgumentException("Unimplemented");
        }
    }

    public static Optional<String> noConstantOf(DatabaseDefinition db,
                                                Context context,
                                                Ast.ExpressionType type,
                                                Random random) {
        List<String> columns = context.valuesOf(Ast.Sort.TABLE).stream()
                .flatMap(t -> db.getTable(t).entrySet().stream()
                        .filter(e -> Ast.ExpressionType.fromJDBCType(e.getValue().getType()).matches(type))
                        .map(e -> t + "." + e.getKey()))
                .collect(Collectors.toList());
        return columns.isEmpty() ? Optional.empty() : Optional.of(randomElement(columns, random));
    }


    public static String valueOf(DatabaseDefinition db, Context context, Ast.ExpressionType type, Random random) {
        if (random.nextDouble() < CONSTANT_PROB) {
            return constantOf(type, random);
        }
        return noConstantOf(db, context, type, random).orElseGet(() -> constantOf(type, random));
    }
}
