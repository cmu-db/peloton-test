package edu.cmu.cs.db.peloton.test.common;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by Tianyu on 3/26/17.
 */
public final class DatabaseDefinition {
    private final Map<String, Map<String, SqlType>> tables;

    private DatabaseDefinition(Map<String, Map<String, SqlType>> tables) {
        this.tables = tables;
    }

    /**
     * A Builder object for DatabaseDefinition
     */
    public static class Builder {
        private ImmutableMap.Builder<String, Map<String, SqlType>> tableBuilder = ImmutableMap.builder();
        private ImmutableMap.Builder<String, SqlType> columnsBuilder;
        private String currTable;

        /**
         * Instantiates a new Builder.
         */
        public Builder() {
            tableBuilder = ImmutableMap.builder();
        }

        /**
         * Start definition for a new table in the database. If a table is
         * currently being defined, that table definition is finalized
         *
         * @param name the name of the table
         * @return self reference for method chaining
         */
        public DatabaseDefinition.Builder table(String name) {
            if (currTable != null) {
                tableBuilder.put(currTable, columnsBuilder.build());
            }
            currTable = name;
            columnsBuilder = ImmutableMap.builder();
            return this;
        }

        /**
         * Defines a column for the current table being defined.
         *
         * @param name the name of the column
         * @param type the type of values stored in the column
         * @return self reference for method chaining
         * @throws IllegalStateException if no table is being defined
         */
        public DatabaseDefinition.Builder column(String name, String type) {
            if (currTable == null) {
                throw new IllegalStateException("No table being defined");
            }
            columnsBuilder.put(name, SqlType.valueOf(type));
            return this;
        }

        /**
         * Builds database definition.
         *
         * @return the database definition
         */
        public DatabaseDefinition build() {
            tableBuilder.put(currTable, columnsBuilder.build());
            return new DatabaseDefinition(tableBuilder.build());
        }
    }

    /**
     * Tables stream.
     *
     * @return the stream
     */
    public Stream<String> tables() {
        return tables.keySet().stream();
    }

    /**
     * All columns stream.
     *
     * @return the stream
     */
    public Stream<Map.Entry<String, SqlType>> allColumns() {
        return tables.values().stream().flatMap(a -> a.entrySet().stream());
    }

    /**
     * Gets table.
     *
     * @param tableName the table name
     * @return the table
     */
    public Map<String, SqlType> getTable(String tableName) {
        return tables.get(tableName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseDefinition that = (DatabaseDefinition) o;

        return tables.equals(that.tables);
    }

    @Override
    public int hashCode() {
        return tables.hashCode();
    }
}
