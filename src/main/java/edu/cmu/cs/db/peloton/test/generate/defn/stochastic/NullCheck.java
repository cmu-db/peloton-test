package edu.cmu.cs.db.peloton.test.generate.defn.stochastic;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.ast.stochastic.StochasticSumElem;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public class NullCheck extends StochasticSumElem {
    @Override
    protected List<Ast.StochasticElem> args() {
        return ImmutableList.of(
                (db, ctx, r) -> generate(db, ctx, r, "%s IS NOT NULL"),
                (db, ctx, r) -> generate(db, ctx, r, "%s IS NULL")
        );
    }

    public Ast.Clause generate(DatabaseDefinition db, Context context, Random random, String format) {
        return new Ast.Clause(
                String.format(format, RandomUtils.randomElement(context.valuesOf(Ast.Sort.COLUMN), random)),
                context
        );
    }
}
