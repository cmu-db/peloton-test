package edu.cmu.cs.db.peloton.test.generate.defn.exhaustive;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.util.Iterators;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.Iterator;

/**
 * Created by tianyuli on 3/20/17.
 */
public class Star implements Ast.Elem {
    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        return Iterators.of(new Ast.Clause("*", context));
    }
}
