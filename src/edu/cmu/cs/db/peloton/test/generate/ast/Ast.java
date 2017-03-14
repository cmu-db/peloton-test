package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.generate.Context;

import java.util.*;

/**
 * Created by Tianyu on 3/13/17.
 */
public final class Ast {
    public final static class Clause {
        private final String clause;
        private final Context context;

        public Clause(String clause, Context context) {
            this.clause = clause;
            this.context = context;
        }

        public String getClause() {
            return clause;
        }

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

    public interface Elem {
        Iterator<Clause> allClauses(Context context);
    }

    private Ast() {
        // Should not instantiate
    }
}
