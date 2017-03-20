package edu.cmu.cs.db.peloton.test.generate;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Static utility class for various convenient methods on iterators
 */
public final class Iterators {
    /**
     * Concatenates two iterators together to form a new iterator
     *
     * @param <T> the type of elements in the two iterators
     * @param one the first iterator
     * @param two the second iterator
     * @return the resulting concatenated iterator in the order given
     */
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

    /**
     * Takes an iterator and lazily applies an operation to it to form a new iterator
     *
     * @param <T>    the type of elements in the original iterator
     * @param <E>    the type of elements in the resulting iterator
     * @param one    the iterator to be mapped
     * @param mapper the mapper
     * @return mapped iterator
     */
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

    /**
     * Fold left on the given iterator, combining values using the given function and identity
     *
     * @param <T>      the type of elements in the original iterator
     * @param <E>      the resulting type of this operation
     * @param it       the iterator
     * @param id       the identity value to be used for type E
     * @param f the function that combines T and E into a E
     * @return f(xn, ... f(x2, f(x1, id))) assuming the iterator yields x1, x2, ... xn
     */
    public static <T, E> E foldLeft(Iterator<T> it, E id, BiFunction<T, E, E> f) {
        E result = id;
        while (it.hasNext()) {
            result = f.apply(it.next(), result);
        }
        return result;
    }

    /**
     * Collects an iterator into a list
     *
     * @param <E> the type of elements in the iterator
     * @param it  the iterator
     * @return the list
     */
    public static <E> List<E> toList(Iterator<E> it) {
        List<E> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    /**
     * Creates a clause from a list of clauses, connecting all contexts in a union and
     * put string clauses into a sql style list, i.e. (x, c1 and y c2) goes to "x, y", c1 union c2
     *
     * @param elems the clauses to be joined together
     * @return the resulting clause
     */
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

    /**
     * Convenient method for constructing an iterator.
     *
     * @param elems the elements in the iterator, in order
     * @param <E> the type if elements in the iterator
     * @return an iterator that returns the elements given in that order
     */
    public static <E> Iterator<E> of(E... elems) {
        return Arrays.asList(elems).iterator();
    }

    private Iterators() {
        // should not instantiate
    }
}
