package edu.cmu.cs.db.peloton.test.generate.defn.stochastic;

import com.beust.jcommander.Parameter;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.defn.stochastic.operators.SqlBinaryOperator;
import edu.cmu.cs.db.peloton.test.generate.defn.stochastic.operators.SqlUnaryOperator;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

/**
 * Created by tianyuli on 4/1/17.
 */
public class SearchCondition implements Ast.StochasticElem{
    private final Set<SqlBinaryOperator> binops;
    private final Set<SqlUnaryOperator> unops;
    private final int limit;

    public SearchCondition(Set<SqlBinaryOperator> binops, Set<SqlUnaryOperator> unops, int limit) {
        checkArgument(limit > 0);
        this.binops = binops;
        this.unops = unops;
        this.limit = limit;
    }

    @Override
    public Ast.Clause generate(DatabaseDefinition db, Context context, Random random) {
        return new Ast.Clause(generate(db, context, random, Ast.ExpressionType.BOOLEAN, limit), context);
    }

    private String generate(DatabaseDefinition db, Context context, Random random, Ast.ExpressionType type, int depth) {
        if (depth == 0 || random.nextInt(limit) > depth) {
            return Ast.valueOf(db, context, type, random);
        }

        if (random.nextBoolean()) {
            List<SqlBinaryOperator> ops = binops.stream()
                    .filter(x -> x.getResultType().equals(type))
                    .collect(Collectors.toList());
            if (ops.isEmpty()) {
                return Ast.constantOf(type, random);
            }

            SqlBinaryOperator op = RandomUtils.randomElement(ops, random);
            return op.format(generate(db, context, random, op.getArg1Type(), depth - 1),
                                            generate(db, context, random, op.getArg2Type(), depth - 1));
        } else {
            List<SqlUnaryOperator> ops = unops.stream()
                    .filter(x -> x.getResultType().equals(type))
                    .collect(Collectors.toList());

            if (ops.isEmpty()) {
                return Ast.constantOf(type, random);
            }

            SqlUnaryOperator op = RandomUtils.randomElement(ops, random);
            return op.format(generate(db, context, random, op.getArgType(), depth - 1));
        }
    }
}
