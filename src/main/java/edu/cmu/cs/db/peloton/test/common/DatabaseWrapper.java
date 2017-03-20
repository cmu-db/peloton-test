package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.Context;

/**
 * Created by tianyuli on 3/20/17.
 */
public interface DatabaseWrapper {
    /**
     * Gets context.
     *
     * @param hostname the hostname
     * @param port     the port
     * @param dbName   the db name
     * @return the context
     */
    Context getContext(String hostname, int port, String dbName);
}
