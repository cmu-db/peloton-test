package edu.cmu.cs.db.peloton.test.generate.defn.where;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;

import java.util.Iterator;

/**
 * Created by tianyuli on 3/27/17.
 */
public class NullTest implements Ast.Elem {
    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        return context.valuesOf(Ast.Sort.COLUMN).stream()
                .map(a -> new Ast.Clause(a + " IS NOT NULL", context))
                .iterator();
    }
}
