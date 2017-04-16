package edu.cmu.cs.db.peloton.test.app;

import com.beust.jcommander.JCommander;
import edu.cmu.cs.db.peloton.test.common.DatabaseWrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Iterator;

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
            testDb = new DatabaseWrapper(null, config.readLine(), config.readLine(), config.readLine());
            truthDb = new DatabaseWrapper(null, config.readLine(), config.readLine(), config.readLine());
        }
        batchSize = parsedArgs.getBatchSize();
        queryProvider = Files.lines(Paths.get(parsedArgs.getTraceFile())).iterator();

        while (queryProvider.hasNext()) {
            new JUnitRunner().runTests(parsedArgs.getOutDir());
            return;
        }
    }
}
