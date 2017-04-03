package edu.cmu.cs.db.peloton.test.generate.defn.exhaustive.where;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.exhaustive.SumElem;

/**
 * Created by tianyuli on 3/27/17.
 */
public class SearchCondition extends SumElem {
    @Override
    protected ImmutableList<Ast.Elem> args() {
        return ImmutableList.of(new NullTest());
    }
}
