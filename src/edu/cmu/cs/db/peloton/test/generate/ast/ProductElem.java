package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Util;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

// TODO currently immutable, potentially change to a more efficient solution if
// performance becomes a problem

/**
 * Combines Ast elements into an Ast element in an "and" relationship.
 * (e.g. + takes two integer expressions)
 * The ProductElem class uses a template method pattern that relies on the
 * user specifying the elements to be combined in the method args, and knows
 * how to generate all values given how to generate values of the argument
 * ast elements.
 */
public abstract class ProductElem implements Ast.Elem {
    /**
     * the arguments to this ast element
     *
     * @return a list of elements that make up this element
     */
    protected abstract ImmutableList<Ast.Elem> args();

    /**
     * Given values of the argument types in the order specified in args, produce
     * a value of this type (e.g. given * and foo, produce SELECT * FROM foo)
     *
     * @param args values of the arguments
     * @return the resulting value of this type
     */
    protected abstract String format(List<String> args);

    @Override
    public Iterator<Ast.Clause> allClauses(Context context) {
        return Util.map(allClausesHelper(context, args(), 0),
                a -> new Ast.Clause(format(a.clauses), a.context));
    }

    private static final class AlmostClause {
        private final List<String> clauses;
        private final Context context;

        AlmostClause(String clause, Context context) {
            this.clauses = ImmutableList.of(clause);
            this.context = context;
        }

        AlmostClause(ImmutableList<String> clauses, Context context) {
            this.clauses = clauses;
            this.context = context;
        }
    }

    private static Iterator<AlmostClause> allClausesHelper(
            Context context, List<Ast.Elem> toIterate, int currentIndex) {

        checkArgument(!toIterate.isEmpty());
        checkArgument(currentIndex >= 0 && currentIndex < toIterate.size());

        Ast.Elem curr = toIterate.get(currentIndex);
        if (currentIndex == toIterate.size() - 1) {
            return Util.map(curr.allClauses(context), a -> new AlmostClause(a.getClause(), a.getContext()));
        }

        Iterator<Iterator<AlmostClause>>  recursive =
                Util.map(curr.allClauses(context),
                         a -> Util.map(
                                allClausesHelper(a.getContext(), toIterate, currentIndex + 1),
                                b -> merge(b, a)
                         )
                );

        return Util.foldLeft(recursive, Collections.emptyIterator(), Util::chain);
    }

    private static AlmostClause merge(AlmostClause one, Ast.Clause other) {
        return new AlmostClause(
                ImmutableList.<String>builder()
                        .add(other.getClause())
                        .addAll(one.clauses)
                        .build(),
                one.context.union(other.getContext())
        );
    }
}
