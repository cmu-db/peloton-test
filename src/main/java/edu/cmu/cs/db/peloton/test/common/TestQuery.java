package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyuli on 4/3/17.
 */
@RunWith(Parameterized.class)
public class TestQuery {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<?> data() {
        List<String> queries = new ArrayList<>();
        DatabaseDefinition definition =
                Main.db.getDatabaseDefinition(
                        Main.args[0],
                        Integer.parseInt(Main.args[1]),
                        Main.args[2]
                );
        for (int i = 0; i < 20; i++) {
            queries.add(Main.tested.generate(definition, Context.EMPTY, Main.random).getClause());
        }
        return queries;
    }

    @Parameterized.Parameter
    public String query;

    @Test
    public void test() {
        System.out.println(query);
    }
}
