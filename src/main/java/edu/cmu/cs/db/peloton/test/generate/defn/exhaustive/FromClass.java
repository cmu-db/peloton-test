package edu.cmu.cs.db.peloton.test.generate.defn.exhaustive;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.Iterator;

/**
 * Created by tianyuli on 3/20/17.
 */
public class FromClass implements Ast.Elem {
    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        return db.tables()
                .map(a -> new Ast.Clause(a, context.addToScope(Ast.Sort.TABLE, a)))
                .iterator();
    }
}
