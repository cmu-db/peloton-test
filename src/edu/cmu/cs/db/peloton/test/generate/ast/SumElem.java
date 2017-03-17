package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Util;

import java.util.Collections;
import java.util.Iterator;

/**
 * Combines Ast elements into an Ast element in an "or" relationship.
 * (e.g. SELECT [] takes either * or a valid column name)
 * The SumElem class uses a template method pattern that relies on the
 * user specifying the elements to be combined in the method args, and knows
 * how to generate all values given how to generate values of the argument
 * ast elements.
 */
public abstract class SumElem implements Ast.Elem {

    /**
     * the arguments to this ast element
     *
     * @return a list of elements that make up this element
     */
    protected abstract ImmutableList<Ast.Elem> args();

    @Override
    public Iterator<Ast.Clause> allClauses(Context context) {
        return args().stream().map(e -> e.allClauses(context))
                .reduce(Collections.emptyIterator(), Util::chain);
    }
}
