package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Tianyu on 3/13/17.
 */
public abstract class ProductElem implements Ast.Elem {
    protected abstract ImmutableList<Ast.Elem> args();

    protected abstract String format(List<String> args);

    @Override
    public Iterator<Ast.Clause> allClauses(Context context) {
        return Util.map(
                allClausesHelper(context, args(), 0),
                a -> new Ast.Clause(format(a.clause), context));
    }

    private static final class AlmostClause {
        private final List<String> clause;
        private final Context context;

        public AlmostClause(String clause, Context context) {
            this.clause = new ArrayList<>();
            this.clause.add(clause);
            this.context = context;
        }
    }

    private static Iterator<AlmostClause> allClausesHelper(
            Context context, List<Ast.Elem> toIterate, int currentIndex) {

        checkArgument(!toIterate.isEmpty());
        checkArgument(currentIndex >= 0 && currentIndex < toIterate.size());

        Ast.Elem curr = toIterate.get(currentIndex);
        if (currentIndex == toIterate.size()) {
            return Util.map(curr.allClauses(context), a -> new AlmostClause(a.getClause(), a.getContext()));
        }
        Iterator<Iterator<AlmostClause>>  recursive = Util.map(curr.allClauses(context),
                a -> allClausesHelper(a.getContext(), toIterate, currentIndex + 1));

        return Util.foldLeft(recursive, Collections.emptyIterator(), Util::chain);
    }
}
