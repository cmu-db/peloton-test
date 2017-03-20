package edu.cmu.cs.db.peloton.test.generate.defn;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.ListElem;
import edu.cmu.cs.db.peloton.test.generate.ast.SumElem;

/**
 * Created by tianyuli on 3/20/17.
 */
public class SelectProp extends SumElem {
    @Override
    protected ImmutableList<Ast.Elem> args() {
        return ImmutableList.of(new ListElem(new PropertySpec()), new ListElem(new ClassAliasStar()), new Star());
    }
}
