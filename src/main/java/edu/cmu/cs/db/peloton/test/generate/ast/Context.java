package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Stream;

// TODO change to mutable if performance becomes an issue

/**
 * Context for SQL clauses
 */
public final class Context {
    private final Map<Ast.Type, Set<String>> values;

    /**
     * The constant EMPTY.
     */
    public static final Context EMPTY = new Context(Collections.emptyMap());

    private Context(Map<Ast.Type, Set<String>> values) {
        this.values = values;
    }

    /**
     * Union context.
     *
     * @param other the other
     * @return the context
     */
    public Context union(Context other) {
        return new Context(Stream.concat(values.entrySet().stream(), other.values.entrySet().stream())
                .collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue, Sets::union)));
    }

    /**
     * Add to scope context. Convenient method for when there is only one value to be added.
     * Otherwise, use a builder for better performance.
     *
     * @param type  the type
     * @param value the value
     * @return the context
     */
    public Context addToScope(Ast.Type type, String value) {
        return new Builder().putAll(this).put(type, value).build();
    }

    /**
     * Gets all values currently in scope for a given type
     *
     * @param type the type of interest
     * @return all string values in scope of that type
     */
    public Set<String> valuesOf(Ast.Type type) {
        return values.getOrDefault(type, Collections.emptySet());
    }

    /**
     * The type Builder.
     */
    // TODO: fix redundant copying
    // Currently the copying happens twice when we construct a new context out of existing ones.
    // We should only need one.
    public static class Builder {
        private Map<Ast.Type, Set<String>> builder = new HashMap<>();

        /**
         * Put value into builder.
         *
         * @param type  the type
         * @param value the value
         * @return the builder
         */
        public Builder put(Ast.Type type, String value) {
            builder.computeIfAbsent(type, k -> new HashSet<>()).add(value);
            return this;
        }

        /**
         * Put all values into builder.
         *
         * @param context the context
         * @return the builder
         */
        public Builder putAll(Context context) {
            builder.putAll(context.values);
            return this;
        }

        /**
         * Build context.
         *
         * @return the context
         */
        public Context build() {
            return new Context(ImmutableMap.copyOf(builder));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Context context = (Context) o;

        return values.equals(context.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
