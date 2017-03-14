package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Tianyu on 3/13/17.
 */
public class ListElem implements Ast.Elem {
    private static class Combinations<E> implements Iterator<List<E>>{
        private final long elements;
        private long curr = 0;
        private final List<E> target;
        private final List<E> result;

        Combinations(Iterator<E> target) {
            this.target = Stream.generate(target::next).collect(Collectors.toList());
            elements = ~(-1L << this.target.size());
            result = new ArrayList<>();
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

    private final Ast.Elem elemType;

    public ListElem(Ast.Elem elemType) {
        this.elemType = elemType;
    }

    public Iterator<Ast.Clause> allClauses(Context context) {
        return Util.map(
                new Combinations<>(elemType.allClauses(context)),
                Util::fromList
        );
    }
}

