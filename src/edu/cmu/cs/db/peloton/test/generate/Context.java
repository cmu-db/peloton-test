package edu.cmu.cs.db.peloton.test.generate;

/**
 * Created by Tianyu on 3/13/17.
 */
public class Context {
    // TODO change once actual implementation is done
    private static Context empty = new Context();

    public static Context empty() {
        return empty;
    }

    public Context union(Context other) {
        return empty;
    }
}
