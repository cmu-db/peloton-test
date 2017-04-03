package edu.cmu.cs.db.peloton.test.generate.ast.exhaustive;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.util.Iterators;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;

import java.util.Collections;
import java.util.Iterator;

/**
 * Combines Ast elements into an Ast element in an "or" relationship.
 * (e.g. SELECT [] takes either * or a valid column name)
 * The SumElem class uses a template method pattern that relies on the
 * user specifying the elements to be combined in the method args, and knows
 * how to generate all valuesOf given how to generate valuesOf of the argument
 * ast elements.
 */
public abstract class SumElem implements Ast.Elem {

    /**
     * the arguments to this ast element, the result list must not be empty.
     *
     * @return a list of elements that make up this element
     */
    protected abstract ImmutableList<Ast.Elem> args();

    @Override
    public Iterator<Ast.Clause> allClauses(DatabaseDefinition db, Context context, int depth) {
        return args().stream().map(e -> e.allClauses(db, context, depth))
                .reduce(Collections.emptyIterator(), Iterators::chain);
    }
}
