package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.Context;

/**
 * Created by tianyuli on 3/20/17.
 */
public interface DatabaseWrapper {
    Context getContext(String hostname, int port, String dbName);
}
