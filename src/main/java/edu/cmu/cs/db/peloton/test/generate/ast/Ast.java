package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.base.Preconditions;
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

    /**
     * Sql language constructs
     */
    public enum Sort implements Type {
        TABLE, COLUMN
    }

    /**
     * Sql types for variables
     */
    public static final class VarType implements Type {
        private static final Map<JDBCType, VarType> INSTANCES = new EnumMap<>(JDBCType.class);

        static {
            for (JDBCType type : JDBCType.values()) {
                INSTANCES.put(type, new VarType(type));
            }
        }

        private final JDBCType type;

        private VarType(JDBCType type) {
            this.type = type;
        }

        /**
         * returns a instance representing a variable of type.
         *
         * @param type the type
         * @return the var type
         */
        public static VarType of(JDBCType type) {
            return INSTANCES.get(type);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            VarType sqlType = (VarType) o;

            return type == sqlType.type;
        }

        @Override
        public int hashCode() {
            return type.hashCode();
        }
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
