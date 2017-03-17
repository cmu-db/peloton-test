package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Takes an Ast element and produces a list of that ast element type.
 * (e.g. column names can be formed into a list of column lists)
 * The ListElem class can generate all combinations of values of that type.
 */
public class ListElem implements Ast.Elem {
    private final Ast.Elem elemType;

    /**
     * Instantiates a new List elem.
     *
     * @param elemType the elem type
     */
    public ListElem(Ast.Elem elemType) {
        this.elemType = elemType;
    }

    @Override
    public Iterator<Ast.Clause> allClauses(Context context) {
        return Util.map(
                new Combinations<>(elemType.allClauses(context)),
                Util::fromList
        );
    }

    private static class Combinations<E> implements Iterator<List<E>>{
        private final long elements;
        private long curr = 0;
        private final List<E> target;
        private final List<E> result;

        Combinations(Iterator<E> target) {
            this.target = Util.toList(target);
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

