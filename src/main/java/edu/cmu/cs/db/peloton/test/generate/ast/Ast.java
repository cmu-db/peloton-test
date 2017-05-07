package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import java.sql.JDBCType;
import java.util.*;

import static com.google.common.base.Preconditions.*;

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
            checkNotNull(clause);
            checkNotNull(context);
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
         * Randomly generates a value of this type using the given random generator. If the generation failed because
         * there is no value for the random walk down the tree, None is returned.
         *
         * @param db      The database that queries will run on
         * @param context the context
         * @param random The random generator to be used
         * @return an Optional clause of this elem
         */
        Optional<Clause> generate(DatabaseDefinition db, Context context, Random random);

        default Clause generateUntilPresent(DatabaseDefinition db, Context context, Random random) {
            while (true) {
                Optional<Clause> generated = generate(db, context, random);
                if (generated.isPresent()) {
                    return generated.get();
                }
            }
        }
    }

    /**
     * Type of values in the ast, used as a lookup key type in context
     */
    public interface Type {
        // Marker interface
    }

    public enum ExpressionType implements Type {
        NUMERIC, VARCHAR, BOOLEAN, ANY;

        public static ExpressionType fromJDBCType(JDBCType type) {
            // TODO research SQL types
            switch (type) {
                case DOUBLE:
                case FLOAT:
                case INTEGER:
                case NUMERIC:
                case DECIMAL:
                    return NUMERIC;
                case VARCHAR:
                case LONGNVARCHAR:
                    return VARCHAR;
                case BOOLEAN:
                    return BOOLEAN;
                default:
                    throw new IllegalArgumentException("Unimplemented");
            }
        }

        public boolean matches(ExpressionType type) {
            if (type == ANY) {
                return true;
            } else {
                return equals(type);
            }
        }
    }

    /**
     * Sql language constructs
     */
    public enum Sort implements Type {
        TABLE, COLUMN
    }


    private Ast() {
        // Should not instantiate
    }
}
