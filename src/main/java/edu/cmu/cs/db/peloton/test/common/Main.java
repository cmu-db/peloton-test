package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.defn.stochastic.Select;

import java.sql.*;
import java.util.Random;

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
        DatabaseDefinition db = new DatabaseWrapper(null){
            @Override
            public Connection getConnection(String hostname, int port, String dbName) throws SQLException {
                return DriverManager.getConnection(
                        String.format("jdbc:postgresql://%s:%d/%s", hostname, port, dbName));
            }
        }.getDatabaseDefinition(args[0], Integer.parseInt(args[1]), args[2]);
        Ast.StochasticElem select = new Select();
        for (int i = 0; i < 20; i++) {
            System.out.println(select.generate(db, Context.EMPTY, new Random()).getClause());
        }
    }
}
