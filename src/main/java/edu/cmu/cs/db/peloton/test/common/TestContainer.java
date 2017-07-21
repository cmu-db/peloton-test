package edu.cmu.cs.db.peloton.test.common;

import com.google.common.collect.Iterators;
import edu.cmu.cs.db.peloton.test.app.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
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
        for (int i = 0; i < Main.batchSize; i++) {
            if (!Main.queryProvider.hasNext()) {
                break;
            }
            queries.add(Main.queryProvider.next());
        }
        return queries;
    }

    @Parameter
    public String query;

    @Test
    public void test() throws SQLException {
        if (query.toUpperCase().startsWith("SELECT")) {
            testEqual(Main.testDb.getConnection().createStatement().executeQuery(query),
                    Main.truthDb.getConnection().createStatement().executeQuery(query));
        }
        else {
	    Main.testDb.getConnection().createStatement().executeUpdate(query);
	    Main.truthDb.getConnection().createStatement().executeUpdate(query);
        }
    }

    private static void testEqual(ResultSet truth, ResultSet target) throws SQLException {
        Set<List<Object>> truthResults = new HashSet<>();
        while (truth.next()) {
            truthResults.add(getRow(truth, truth.getMetaData().getColumnCount()));
        }

        Set<List<Object>> targetResults = new HashSet<>();
        while (target.next()) {
            targetResults.add(getRow(target, target.getMetaData().getColumnCount()));
        }

        assertEquals(truthResults, targetResults);
    }

    private static List<Object> getRow(ResultSet resultSet, int columnCount) throws SQLException {
        List<Object> row = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            Object obj = resultSet.getObject(i);
            if (obj instanceof Double) {
                row.add(BigDecimal.valueOf((Double)obj));
            }
            else if (obj instanceof Float) {
                row.add(BigDecimal.valueOf((Float)obj));
            }
            else {
                row.add(obj);
            }
        }
        return row;
    }
}
