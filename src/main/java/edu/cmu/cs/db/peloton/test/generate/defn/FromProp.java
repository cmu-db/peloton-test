package edu.cmu.cs.db.peloton.test.generate.defn;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.SumElem;

import java.util.List;

/**
 * Created by tianyuli on 5/7/17.
 */
public class FromProp extends SumElem {
    @Override
    protected List<Ast.Elem> args() {
        return ImmutableList.of(new Join(), new FromClass());
    }
}
