package edu.cmu.cs.db.peloton.test.generate.defn;

import edu.cmu.cs.db.peloton.test.common.AggregateType;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.Iterators;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Aggregate functions for SELECT statement.
 */
public class AggregateFunc implements Ast.Elem {

    private final Ast.Elem prop;

    AggregateFunc(Ast.Elem prop) {
        checkNotNull(prop);
        this.prop = prop;
    }

    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        Iterator<Ast.Clause> res = Collections.emptyIterator();
        Iterator<Ast.Clause> clauses = prop.allClauses(db, context, depth);
        while(clauses.hasNext()) {
            String clause = clauses.next().getClause();
            res = Iterators.chain(res, getAggregateProps(db, context, clause));
        }
        return res;
    }

    private static Iterator<Ast.Clause> getAggregateProps(DatabaseDefinition db,
                                                          Context context,
                                                          String clause) {
        return Arrays.stream(AggregateType.values())
                .map(type -> applyAggregateFunc(db, type, clause))
                .filter(Objects::nonNull)
                .map(a -> new Ast.Clause(a, context))
                .iterator();
    }

    /**
     * Apply a aggregate function to a clause.
     * Return null is not applicable.
     *
     * TODO: check if aggregate function is applicable to the clause column type
     * EG: AVG, SUM can only be applied to numeric type
     *     MIN, MAX : numeric type + date/time
     *     COUNT can be applied to everything (basic types, *)
     */
    private static String applyAggregateFunc(DatabaseDefinition db,
                                             AggregateType type,
                                             String clause) {
        return type + "(" + clause + ")";
    }
}
