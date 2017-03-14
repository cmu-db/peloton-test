package edu.cmu.cs.db.peloton.test.generate;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Tianyu on 3/13/17.
 */
public final class Util {
    public static <T> Iterator<T> chain(Iterator<T> one, Iterator<T> two) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return one.hasNext() || two.hasNext();
            }

            @Override
            public T next() {
                return one.hasNext() ? one.next() : two.next();
            }
        };
    }

    public static <T, E> Iterator<E> map(Iterator<T> one, Function<T, E> mapper) {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return one.hasNext();
            }

            @Override
            public E next() {
                return mapper.apply(one.next());
            }
        };
    }

    public static <T, E> E foldLeft(Iterator<T> it, E id, BiFunction<T, E, E> combiner) {
        E result = id;
        while (it.hasNext()) {
            result = combiner.apply(it.next(), result);
        }
        return result;
    }

    public static Ast.Clause fromList(List<Ast.Clause> elems) {
        StringBuilder builder = new StringBuilder();
        Context context = Context.empty();
        for (Ast.Clause clause : elems) {
            builder.append(clause.getClause()).append(",");
            context = context.union(clause.getContext());
        }
        builder.deleteCharAt(builder.length() - 1);
        return new Ast.Clause(builder.toString(), context);
    }

    private Util() {
        // should not instantiate
    }
}
