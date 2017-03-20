package edu.cmu.cs.db.peloton.test.generate;

/**
 * Context for SQL clauses
 */
public class Context {
    // TODO change once actual implementation is done
    private static Context empty = new Context();

    /**
     * Empty context.
     *
     * @return the context
     */
    public static Context empty() {
        return empty;
    }

    /**
     * Union context.
     *
     * @param other the other
     * @return the context
     */
    public Context union(Context other) {
        return empty;
    }
}
