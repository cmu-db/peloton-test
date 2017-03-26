package edu.cmu.cs.db.peloton.test.generate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;

// TODO change to mutable if performance becomes an issue
/**
 * Context for SQL clauses
 */
public final class Context {
    // type -> variable name
    private final Map<String, String> variables;
    private final Set<String> tableScope;

    public static final Context EMPTY = new Context(Collections.emptyMap(), Collections.emptySet());

    private Context(Map<String, String> variables,
                    Set<String> scope) {
        this.variables = variables;
        this.tableScope = scope;
    }

    private static <K, V> Map<K, V> mergeImmutable(Map<K, V> one, Map<K, V> other) {
        Map<K, V> result = new HashMap<>();
        result.putAll(one);
        result.putAll(other);
        return ImmutableMap.copyOf(result);
    }

    /**
     * Union context.
     *
     * @param other the other
     * @return the context
     */
    public Context union(Context other) {
        return new Context(
                mergeImmutable(variables, other.variables),
                Sets.union(tableScope, other.tableScope)
        );
    }

    /**
     * Add to scope context.
     *
     * @param tableName the tables name
     * @return the context
     */
    public Context addToScope(String tableName) {
        return new Context(
                variables,
                ImmutableSet.<String>builder()
                        .addAll(tableScope)
                        .add(tableName)
                        .build()
        );
    }

    /**
     * Gets tables in scope.
     *
     * @return the tables in scope
     */
    public Set<String> getTablesInScope() {
        return tableScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Context context = (Context) o;

        return variables.equals(context.variables) && tableScope.equals(context.tableScope);
    }

    @Override
    public int hashCode() {
        int result = variables.hashCode();
        result = 31 * result + tableScope.hashCode();
        return result;
    }
}
