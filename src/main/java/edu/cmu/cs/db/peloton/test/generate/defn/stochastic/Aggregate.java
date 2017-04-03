package edu.cmu.cs.db.peloton.test.generate.defn.stochastic;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.ast.stochastic.StochasticSumElem;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.sql.SQLType;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Created by tianyuli on 4/2/17.
 */
public enum Aggregate implements Ast.StochasticElem {
    ;
    private final String name;
    private final Predicate<SQLType> applicability;

    Aggregate(String name, Predicate<SQLType> applicability) {
        this.name = name;
        this.applicability = applicability;
    }

    @Override
    public Ast.Clause generate(DatabaseDefinition db, Context context, Random random) {
        String table = RandomUtils.randomElement(context.valuesOf(Ast.Sort.TABLE), random);
        return null;

    }

    public static Ast.StochasticElem all() {
        return new StochasticSumElem() {
            @Override
            protected List<Ast.StochasticElem> args() {
                return Arrays.asList(Aggregate.values());
            }
        };
    }

}
