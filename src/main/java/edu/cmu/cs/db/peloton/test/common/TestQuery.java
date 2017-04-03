package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.runners.Parameterized.*;

/**
 * Created by tianyuli on 4/3/17.
 */
@RunWith(Parameterized.class)
public class TestQuery {
    @Parameters(name = "{index}: {0}")
    public static Iterable<?> data() {
        List<String> queries = new ArrayList<>();
        DatabaseDefinition definition = Main.db.getDatabaseDefinition();
        for (int i = 0; i < 20; i++) {
            queries.add(Main.tested.generate(definition, Context.EMPTY, Main.random).getClause());
        }
        return queries;
    }
    @Parameter
    public String query;

    @Test
    public void test() throws SQLException {
        System.out.println(query);
        Main.db.getConnection().createStatement().execute(query);
    }
}
