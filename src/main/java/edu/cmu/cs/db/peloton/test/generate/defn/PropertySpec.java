package edu.cmu.cs.db.peloton.test.generate.defn;

import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Iterators;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by tianyuli on 3/20/17.
 */
public class PropertySpec implements Ast.Elem {

    @Override
    public Iterator<Ast.Clause> allClauses(Context context, int depth) {
        Iterator<Ast.Clause> result = Collections.emptyIterator();
        for (String table : context.getTablesInScope()) {
            result = Iterators.chain(result, forTable(context, table));
        }
        return result;
    }

    private static Iterator<Ast.Clause> forTable(Context context, String table) {
        Map<String, String> columns = context.getColumns(table);
        return columns.keySet().stream()
                .map(a -> table + "." + a)
                .map(a -> new Ast.Clause(a, context))
                .iterator();
    }
}
