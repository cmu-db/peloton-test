package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.generate.Context;

import java.util.*;

/**
 * Static Utility class that groups together various definitions in the
 * sql syntax tree implementation
 */
public final class Ast {
    /**
     * An Ast Clause consists of a sql string and the context this clause is in
     */
    public static final class Clause {
        private final String clause;
        private final Context context;

        /**
         * Instantiates a new Clause.
         *
         * @param clause  the clause string
         * @param context the context
         */
        public Clause(String clause, Context context) {
            this.clause = clause;
            this.context = context;
        }

        /**
         * Gets clause.
         *
         * @return the clause
         */
        public String getClause() {
            return clause;
        }

        /**
         * Gets context.
         *
         * @return the context
         */
        public Context getContext() {
            return context;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Clause clause1 = (Clause) o;

            return clause.equals(clause1.clause) && context.equals(clause1.context);
        }

        @Override
        public int hashCode() {
            int result = clause.hashCode();
            result = 31 * result + context.hashCode();
            return result;
        }
    }

    /**
     * The interface that represents an element of the syntax tree.
     */
    public interface Elem {
        /**
         * Iterates through all values of this element given a context and depth. The depth
         * limits the depth of generated ast to guard against infinite trees. The depth will
         * only be applied to infinite types.
         *
         * @param context the context
         * @param depth the limit to the depth of the ast we are generating for infinite types
         * @return the iterator of all possible values of this type given the context
         */
        Iterator<Clause> allClauses(Context context, int depth);
    }

    private Ast() {
        // Should not instantiate
    }
}
