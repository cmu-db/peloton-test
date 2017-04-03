package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.defn.stochastic.Select;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.sql.*;
import java.util.Random;

/**
 * Created by tianyuli on 3/20/17.
 */
public class Main {
    public static DatabaseWrapper db;
    public static Ast.StochasticElem tested;
    public static Random random;
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String[] args) throws SQLException {
        db = new DatabaseWrapper(null, args[0], Integer.parseInt(args[1]), args[2]){
            @Override
            protected Connection initiateConnection(String hostname, int port, String dbName) throws SQLException {
                return DriverManager.getConnection(
                        String.format("jdbc:postgresql://%s:%d/%s", hostname, port, dbName));
            }
        };
        tested = new Select();
        random = new Random();

        Result result = JUnitCore.runClasses(TestQuery.class);
        for (Failure f : result.getFailures()) {
            System.out.println(f.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
