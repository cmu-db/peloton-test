package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.*;

/**
 * Created by tianyuli on 4/3/17.
 */
@RunWith(Parameterized.class)
public class TestContainer {
    @Parameters(name = "{index}: {0}")
    public static Iterable<?> data() {
        List<String> queries = new ArrayList<>();
        DatabaseDefinition definition = Main.testDb.getDatabaseDefinition();
        for (int i = 0; i < 20; i++) {
            queries.add(Main.tested.generate(definition, Context.EMPTY, Main.random).getClause());
        }
        return queries;
    }
    @Parameter
    public String query;

    @Test
    public void test() throws SQLException {
        testEqual(Main.testDb.getConnection().createStatement().executeQuery(query),
                Main.truthDb.getConnection().createStatement().executeQuery(query));
    }

    private static void testEqual(ResultSet truth, ResultSet target) throws SQLException {
        Set<List<Object>> truthResults = new HashSet<>();
        while (truth.next()) {
            truthResults.add(getRow(truth, truth.getMetaData().getColumnCount()));
        }

        Set<List<Object>> targetResults = new HashSet<>();
        while (target.next()) {
            targetResults.remove(getRow(target, target.getMetaData().getColumnCount()));
        }

        assertEquals(truthResults, targetResults);
    }

    private static List<Object> getRow(ResultSet resultSet, int columnCount) throws SQLException {
        List<Object> row = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            row.add(resultSet.getObject(i));
        }
        return row;
    }
}
