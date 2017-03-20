package edu.cmu.cs.db.peloton.test.generate.defn;

import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.Iterator;

/**
 * Created by tianyuli on 3/20/17.
 */
public class ClassAliasStar implements Ast.Elem {

    @Override
    public Iterator<Ast.Clause> allClauses(Context context, int depth) {
        return context.getTablesInScope().stream()
                .map(a -> String.format("%s.*", a))
                .map(a -> new Ast.Clause(a, context))
                .iterator();
    }
}
