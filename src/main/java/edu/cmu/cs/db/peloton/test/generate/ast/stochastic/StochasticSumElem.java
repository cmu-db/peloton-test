package edu.cmu.cs.db.peloton.test.generate.ast.stochastic;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public abstract class StochasticSumElem implements Ast.StochasticElem {
    protected abstract List<Ast.StochasticElem> args();

    @Override
    public Ast.Clause generate(DatabaseDefinition db, Context context, Random random) {
        return RandomUtils.randomElement(args(), random)
                .generate(db, context, random);
    }

}
