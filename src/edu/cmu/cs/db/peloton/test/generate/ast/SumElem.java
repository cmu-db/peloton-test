package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Util;

import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Tianyu on 3/13/17.
 */
public abstract class SumElem implements Ast.Elem {
    protected abstract ImmutableList<Ast.Elem> args();

    @Override
    public Iterator<Ast.Clause> allClauses(Context context) {
        return args().stream().map(e -> e.allClauses(context))
                .reduce(Collections.emptyIterator(), Util::chain);
    }
}
