package edu.cmu.cs.db.peloton.test.generate.defn;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.SumElem;
import edu.cmu.cs.db.peloton.test.generate.defn.where.NullTest;

/**
 * Created by tianyuli on 3/27/17.
 */
public class SearchCondition extends SumElem {
    @Override
    protected ImmutableList<Ast.Elem> args() {
        return ImmutableList.of(new NullTest());
    }
}
