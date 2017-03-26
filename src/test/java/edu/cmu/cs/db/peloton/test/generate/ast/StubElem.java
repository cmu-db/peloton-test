package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.Context;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Tianyu on 3/15/17.
 */
public class StubElem implements Ast.Elem {
    private final List<String> values;

    public StubElem(String... values) {
        this.values = Arrays.asList(values);
    }

    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        return values.stream().map(s -> new Ast.Clause(s, context)).iterator();
    }
}
