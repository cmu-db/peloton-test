package edu.cmu.cs.db.peloton.test.generate.defn;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Iterators;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.sql.SQLType;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by tianyuli on 3/20/17.
 */
public class PropertySpec implements Ast.Elem {

    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        Iterator<Ast.Clause> result = Collections.emptyIterator();
        for (String table : context.getTablesInScope()) {
            result = Iterators.chain(result, forTable(db, context, table));
        }
        return result;
    }

    private static Iterator<Ast.Clause> forTable(DatabaseDefinition db, Context context, String table) {
        Map<String, SQLType> columns = db.getTable(table);
        return columns.keySet().stream()
                .map(a -> table + "." + a)
                .map(a -> new Ast.Clause(a, context))
                .iterator();
    }
}
