package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.sql.JDBCType;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;
import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.NUMERIC;
import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.VARCHAR;
import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.BOOLEAN;

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
         * Iterates through all valuesOf of this element given a context and depth. The depth
         * limits the depth of generated ast to guard against infinite trees. The depth will
         * only be applied to infinite types.
         *
         * @param db      The database that queries will run on
         * @param context the context
         * @param depth   the limit to the depth of the ast we are generating for infinite types
         * @return the iterator of all possible valuesOf of this type given the context
         */
        Iterator<Clause> allClauses(DatabaseDefinition db, Context context, int depth);
    }

    public interface StochasticElem {
        Clause generate(DatabaseDefinition db, Context context, Random random);
    }

    /**
     * Type of values in the ast
     */
    public interface Type {
        // Marker interface
    }

    public enum ExpressionType implements Type {
        NUMERIC, VARCHAR, BOOLEAN, ANY, SET;

        public boolean matches(ExpressionType type) {
            if (type == ANY) {
                return true;
            } else {
                return equals(type);
            }
        }
    }

    public static ExpressionType toExpressionType(JDBCType type) {
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

    public static String constantOf(ExpressionType type, Random random) {
        switch (type) {
            case NUMERIC:
                return Double.toString(random.nextDouble());
            case VARCHAR:
                int length = random.nextInt(10);
                StringBuilder result = new StringBuilder("\"");
                for (int i = 0; i < length; i++) {
                    result.append((char) random.nextInt(26) + 'a');
                }
                result.append("\"");
                return result.toString();
            case BOOLEAN:
                return random.nextBoolean() ? "TRUE" : "FALSE";
            case ANY:
                return constantOf(RandomUtils.randomElement(Arrays.asList(NUMERIC, VARCHAR, BOOLEAN), random), random);
            default:
                throw new IllegalArgumentException("Unimplemented");
        }
    }

    public static String valueOf(DatabaseDefinition db, Context context, ExpressionType type, Random random) {
        List<String> columns = context.valuesOf(Sort.TABLE).stream()
                    .flatMap(t -> db.getTable(t).entrySet().stream()
                            .filter(e -> toExpressionType(e.getValue().getType()).matches(type))
                            .map(e -> t + "." + e.getKey()))
                            .collect(Collectors.toList());
            if (columns.isEmpty()) {
                return constantOf(type, random);
            }
            return RandomUtils.randomElement(columns, random);
    }

    /**
     * Sql language constructs
     */
    public enum Sort implements Type {
        TABLE, COLUMN
    }


    public static Iterator<String> fromAst(Ast.StochasticElem elem, int limit, DatabaseDefinition db, Random random) {
        return new Iterator<String>() {
            private int i = 0;
            @Override
            public boolean hasNext() {
                return i < limit;
            }

            @Override
            public String next() {
                i++;
                return elem.generate(db, Context.EMPTY, random).getClause();
            }
        };
    }

    private Ast() {
        // Should not instantiate
    }
}
