package edu.cmu.cs.db.peloton.test.app;

import com.beust.jcommander.JCommander;
import edu.cmu.cs.db.peloton.test.common.DatabaseWrapper;
import edu.cmu.cs.db.peloton.test.generate.defn.Select;
import edu.cmu.cs.db.peloton.test.generate.util.Iterators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by tianyuli on 3/20/17.
 */
public class Main {
    public static DatabaseWrapper testDb;
    public static DatabaseWrapper truthDb;
    public static Iterator<String> queryProvider;
    public static int batchSize;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String... args) throws SQLException, IOException{
        Args parsedArgs = new Args();
        new JCommander(parsedArgs, args);

        try (BufferedReader config = new BufferedReader(new FileReader(parsedArgs.getConfigFile()))) {
            testDb = new DatabaseWrapper(config.readLine(), config.readLine(), config.readLine());
            truthDb = new DatabaseWrapper(config.readLine(), config.readLine(), config.readLine());
        }

        TestInitialization.runSetupTraceIfExists(parsedArgs.getSetupTraceFile());

        batchSize = parsedArgs.getBatchSize();
        queryProvider = parsedArgs.getTraceFile() == null
                ? Iterators.fromAst(new Select(), parsedArgs.getLimit(),truthDb.getDatabaseDefinition(), new Random())
                : Files.lines(Paths.get(parsedArgs.getTraceFile())).iterator();

        while (queryProvider.hasNext()) {
            new JUnitRunner().runTests(parsedArgs.getOutDir());
        }
    }
}
