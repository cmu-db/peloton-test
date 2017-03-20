package edu.cmu.cs.db.peloton.test.generate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

// TODO change to mutable if performance becomes an issue
/**
 * Context for SQL clauses
 */
public final class Context {
    // TODO find a better representation for types
    // tables name -> (column name -> type name)
    private final Map<String, Map<String, String>> tables;
    // type -> variable name
    private final Map<String, String> variables;
    private final Set<String> tableScope;

    private Context(Map<String, Map<String, String>> tables,
                    Map<String, String> variables,
                    Set<String> tableScope) {
        this.tables = tables;
        this.variables = variables;
        this.tableScope = tableScope;
    }

    /**
     * A Builder object for Context
     */
    public static class Builder {
        private ImmutableMap.Builder<String, Map<String, String>> tableBuilder = ImmutableMap.builder();
        private ImmutableMap.Builder<String, String> columnsBuilder;
        private String currTable;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
            tableBuilder = ImmutableMap.builder();
        }

        /**
         * Table builder.
         *
         * @param name the name
         * @return the builder
         */
        public Builder table(String name) {
            if (currTable != null) {
                tableBuilder.put(currTable, columnsBuilder.build());
            }
            currTable = name;
            columnsBuilder = ImmutableMap.builder();
            return this;
        }

        /**
         * Column builder.
         *
         * @param name the name
         * @param type the type
         * @return the builder
         */
        public Builder column(String name, String type) {
            columnsBuilder.put(name, type);
            return this;
        }

        /**
         * Build context.
         *
         * @return the context
         */
        public Context build() {
            if (currTable != null) {
                tableBuilder.put(currTable, columnsBuilder.build());
            }
            return new Context(tableBuilder.build(), Collections.emptyMap(), Collections.emptySet());
        }
    }

    /**
     * Empty context.
     *
     * @return the context
     */
    public static Context empty() {
        return new Context(Collections.emptyMap(), Collections.emptyMap(), Collections.emptySet());
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
                mergeImmutable(tables, other.tables),
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
        checkArgument(tables.containsKey(tableName));
        return new Context(
                tables,
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

    /**
     * Gets columns.
     *
     * @param tableName the tables name
     * @return the columns
     */
    public Map<String, String> getColumns(String tableName) {
        return tables.get(tableName);
    }

    /**
     * Gets tables.
     *
     * @return the tables
     */
    public Set<String> getTables() {
        return tables.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Context context = (Context) o;

        return tables.equals(context.tables) && variables.equals(context.variables);
    }

    @Override
    public int hashCode() {
        int result = tables.hashCode();
        result = 31 * result + variables.hashCode();
        return result;
    }
}
