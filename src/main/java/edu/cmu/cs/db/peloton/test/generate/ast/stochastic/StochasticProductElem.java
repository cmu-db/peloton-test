package edu.cmu.cs.db.peloton.test.generate.ast.stochastic;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public abstract class StochasticProductElem implements Ast.StochasticElem {
    protected abstract List<Ast.StochasticElem> args();

    protected abstract String format(List<String> args);

    @Override
    public Ast.Clause generate(DatabaseDefinition db, Context context, Random random) {
        Context gamma = context;
        List<String> result = new ArrayList<>();
        for (Ast.StochasticElem e : args()) {
            Ast.Clause clause = e.generate(db, gamma, random);
            gamma = clause.getContext();
            result.add(clause.getClause());
        }
        return new Ast.Clause(format(result), gamma);
    }

}
