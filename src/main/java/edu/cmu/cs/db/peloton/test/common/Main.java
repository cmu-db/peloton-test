package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.defn.Select;

import java.sql.*;

/**
 * Created by tianyuli on 3/20/17.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws SQLException the sql exception
     */
    public static void main(String[] args) throws SQLException {
        DatabaseDefinition db = new DatabaseWrapper(){
            @Override
            public Connection getConnection(String hostname, int port, String dbName) throws SQLException {
                return DriverManager.getConnection(
                        String.format("jdbc:postgresql://%s:%d/%s", hostname, port, dbName));
            }
        }.getDatabaseDefinition(args[0], Integer.parseInt(args[1]), args[2]);
        new Select().allClauses(db, Context.EMPTY, 0)
                .forEachRemaining(a -> System.out.println(a.getClause()));
    }
}
