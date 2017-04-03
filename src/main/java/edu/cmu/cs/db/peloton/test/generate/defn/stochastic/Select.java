package edu.cmu.cs.db.peloton.test.generate.defn.stochastic;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.stochastic.StochasticListElem;
import edu.cmu.cs.db.peloton.test.generate.ast.stochastic.StochasticProductElem;

import java.util.List;

/**
 * Created by tianyuli on 4/1/17.
 */
public class Select extends StochasticProductElem {

    @Override
    protected ImmutableList<Ast.StochasticElem> args() {
        return ImmutableList.of(
                new StochasticListElem(new FromClass(), 10, ","),
                new SelectProp(),
                new StochasticListElem(new SearchCondition(), 10, " AND ", " OR "));
    }

    @Override
    protected String format(List<String> args) {
        return String.format("SELECT %s FROM %s WHERE %s;", args.get(1), args.get(0), args.get(2));
    }
}
