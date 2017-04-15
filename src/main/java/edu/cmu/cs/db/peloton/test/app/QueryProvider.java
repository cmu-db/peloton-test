package edu.cmu.cs.db.peloton.test.app;

/**
 * Created by tianyuli on 4/14/17.
 */
@FunctionalInterface
public interface QueryProvider {
    Iterable<String> queries();
}
