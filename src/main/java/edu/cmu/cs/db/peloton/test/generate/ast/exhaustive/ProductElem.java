package edu.cmu.cs.db.peloton.test.generate.ast.exhaustive;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.util.Iterators;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

// TODO benchmark and potentially optimize for efficiency
// Currently this implementation uses the most straightforward
// recursive solution and immutable data structures to ensure
// functional correctness. If this proves too slow, we can
// write more efficient solutions. However, it is possible that
// for the shallow sql queries we target, this cost will be
// negligible to the database operation itself

/**
 * Combines Ast elements into an Ast element in an "and" relationship.
 * (e.g. + takes two integer expressions)
 * The ProductElem class uses a template method pattern that relies on the
 * user specifying the elements to be combined in the method args, and knows
 * how to generate all valuesOf given how to generate valuesOf of the argument
 * ast elements.
 */
public abstract class ProductElem implements Ast.Elem {
    /**
     * the arguments to this ast element, the list must not be empty.
     *
     * @return a list of elements that make up this element
     */
    protected abstract ImmutableList<Ast.Elem> args();

    /**
     * Given valuesOf of the argument types in the order specified in args, produce
     * a value of this type (e.g. given * and foo, produce SELECT * FROM foo)
     *
     * @param args valuesOf of the arguments
     * @return the resulting value of this type
     */
    protected abstract String format(List<String> args);

    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        return Iterators.map(allClausesHelper(db, context, depth, args(), 0),
                a -> new Ast.Clause(format(a.clauses), a.context));
    }

    /**
     * Since Strings are immutable, this class provides a mutable version of clause
     * that helps make the recursive calls
     */
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
            DatabaseDefinition db, Context context, int depth, List<Ast.Elem> toIterate, int currentIndex) {
        Ast.Elem curr = toIterate.get(currentIndex);
        if (currentIndex == toIterate.size() - 1) {
            return Iterators.map(curr.allClauses(db, context, depth),
                    a -> new AlmostClause(a.getClause(), a.getContext()));
        }

        Iterator<Iterator<AlmostClause>> recursive = Iterators.map(
                curr.allClauses(db, context, depth),
                a -> recurse(db, a, depth, toIterate, currentIndex)
        );

        return Iterators.foldLeft(recursive, Collections.emptyIterator(), Iterators::chain);
    }

    // Recursively gets all product valuesOf for a smaller list given a value for the head of
    // the current list
    private static Iterator<AlmostClause> recurse(DatabaseDefinition db, Ast.Clause one, int depth,
                                                  List<Ast.Elem> toIterate, int currentIndex) {
        return Iterators.map(
                allClausesHelper(db, one.getContext(), depth, toIterate, currentIndex + 1),
                a -> merge(a, one)
        );
    }

    // Merges a clause with an almost clause by taking the union of the two contexts and
    // adding the string value of other to the list of valuesOf in one
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
