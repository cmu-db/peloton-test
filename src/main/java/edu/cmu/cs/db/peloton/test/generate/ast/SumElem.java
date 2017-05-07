package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public abstract class SumElem implements Ast.Elem {
    protected abstract List<Ast.Elem> args();

    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        return RandomUtils.randomElement(args(), random)
                .generate(db, context, random);
    }

}
