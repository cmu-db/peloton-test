package edu.cmu.cs.db.peloton.test.app;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by tianyuli on 5/7/17.
 */
public class TestInitialization {

    /**
     * Given a setup/initialization trace file via the 'setup_trace' optional argument
     * this function runs all DDL/Insert/Update/Delete statements to build the table.
     * Alternatively, an option to randomly create/populate tables will be added in
     * future versions. Any 'SELECT' statements will be ignored.
     * @param setupTraceFile
     * @throws SQLException
     */
    public static void runSetupTraceIfExists(String setupTraceFile)
        throws SQLException, IOException {
        if(setupTraceFile == null) return;
        try (BufferedReader setupTrace = new BufferedReader(new FileReader(setupTraceFile))) {
            String query;
            while ((query = setupTrace.readLine()) != null) {
                if (isValidQuery(query)) {
                    Main.testDb.getConnection().createStatement().executeUpdate(query);
                    Main.truthDb.getConnection().createStatement().executeUpdate(query);
                }
            }
        }
    }

    public static boolean isValidQuery(String query) {
        return query.length() != 0 && !query.toUpperCase().startsWith("SELECT");
    }


}
