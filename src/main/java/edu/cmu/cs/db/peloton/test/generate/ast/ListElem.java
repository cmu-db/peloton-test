package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.Iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Takes an Ast element and produces a list of that ast element type.
 * (e.g. column names can be formed into a list of column lists)
 * The ListElem class can generate all combinations of valuesOf of that type.
 */
public class ListElem implements Ast.Elem {
    private final Ast.Elem elemType;

    /**
     * Instantiates a new List elem.
     *
     * @param elemType the elem type, cannot be null
     */
    public ListElem(Ast.Elem elemType) {
        checkNotNull(elemType);
        this.elemType = elemType;
    }

    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        checkNotNull(context);
        return Iterators.map(
                new Combinations<>(elemType.allClauses(db, context, depth)),
                Iterators::fromList
        );
    }

    /**
     * Utility class for generating combinations
     * @param <E> the type of elements in the combination
     */
    private static class Combinations<E> implements Iterator<List<E>>{
        private final long elements;
        private long curr = 0;
        private final List<E> target;
        private final List<E> result;

        Combinations(Iterator<E> target) {
            this.target = Iterators.toList(target);

            // Only supports combinations up to 64 elements. This is not a problem because
            // if we ever need to do anything above 64 it will take way too long anyway.
            checkArgument(this.target.size() <= 64);

            elements = ~(-1L << this.target.size());
            result = new ArrayList<>(this.target);
        }

        @Override
        public boolean hasNext() {
            return curr < elements;
        }

        @Override
        public List<E> next() {
            curr++;
            int combinationSize = Long.bitCount(curr);
            for (int i = 0,j = 0; i < target.size(); i++) {
                if (inCombination(i)) {
                    result.set(j, target.get(i));
                    j++;
                }
            }
            return result.subList(0, combinationSize);
        }

        private boolean inCombination(int index) {
            return ((curr >> index) & 1L) == 1L;
        }
    }
}

