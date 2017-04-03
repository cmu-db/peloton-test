package edu.cmu.cs.db.peloton.test.generate.defn.stochastic;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.stochastic.StochasticSumElem;

import java.util.List;

/**
 * Created by tianyuli on 4/1/17.
 */
public class SearchCondition extends StochasticSumElem {
    @Override
    protected List<Ast.StochasticElem> args() {
        return ImmutableList.of(new NullCheck());
    }
}
