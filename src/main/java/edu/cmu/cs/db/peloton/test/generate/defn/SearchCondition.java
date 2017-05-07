package edu.cmu.cs.db.peloton.test.generate.defn;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.defn.operators.ConstantOption;
import edu.cmu.cs.db.peloton.test.generate.defn.operators.SqlOperator;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

/**
 * Created by tianyuli on 4/1/17.
 */
public class SearchCondition implements Ast.Elem {
    private static final double EARLY_TERMINATION_PROB = 0.1;
    private final Set<SqlOperator> allowedOperators;
    private final int limit;

    public SearchCondition(Set<SqlOperator> allowedOperators, int limit) {
        checkArgument(limit > 0);
        this.allowedOperators = allowedOperators;
        this.limit = limit;
    }

    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        return generate(db, context, random,
                Ast.ExpressionType.BOOLEAN,
                limit,
                ConstantOption.NO_RESTRICTION).map(s -> new Ast.Clause(s, context));
    }

    public Optional<String> generate(DatabaseDefinition db,
                                     Context context,
                                     Random random,
                                     Ast.ExpressionType type,
                                     int depth,
                                     ConstantOption constantOption) {
        Optional<SqlOperator> operatorOption = getRandomOperator(type, random);
        if (!operatorOption.isPresent()) {
            return Optional.empty();
        }
        SqlOperator operator = operatorOption.get();

        if (depth == 1 || random.nextDouble() < EARLY_TERMINATION_PROB) {
            return baseCase(operator, db, context, random, type, constantOption);
        }
        return generateOperation(operator, i -> generate(db, context, random,
                operator.getArgType(i), depth - 1, constantOption));

    }

    private Optional<String> baseCase(SqlOperator operator,
                                      DatabaseDefinition db,
                                      Context context,
                                      Random random,
                                      Ast.ExpressionType type,
                                      ConstantOption constantOption) {
        String[] args = new String[operator.getArity()];
        switch (ConstantOption.upperBound(constantOption, operator.getConstantOption())) {
            case NO_CONSTANT:
                return generateOperation(operator, i -> RandomUtils.noConstantOf(db, context, operator.getArgType(i), random));
            case NOT_ALL_CONSTANTS:
                boolean notConstant = false;
                for (int i = 0; i < operator.getArity(); i++) {
                    Optional<String> arg = RandomUtils.noConstantOf(db, context, operator.getArgType(i), random);
                    if (!arg.isPresent()) {
                        args[i] = RandomUtils.constantOf(operator.getArgType(i), random);
                    } else {
                        args[i] = arg.get();
                        notConstant = true;
                    }
                }
                if (!notConstant) {
                    return Optional.empty();
                }
                return Optional.of(operator.format(args));
            case NO_RESTRICTION:
                return generateOperation(operator, i -> Optional.of(RandomUtils.valueOf(db, context, operator.getArgType(i), random)));
            default:
                throw new IllegalArgumentException("Unrecognized enum element " + constantOption);
        }
    }

    private Optional<SqlOperator> getRandomOperator(Ast.ExpressionType type, Random random) {
        Set<SqlOperator> operatorsMatchingType = allowedOperators.stream()
                .filter(s -> s.getResultType().matches(type))
                .collect(Collectors.toSet());
        if (operatorsMatchingType.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(RandomUtils.randomElement(operatorsMatchingType, random));
        }
    }

    private Optional<String> generateOperation(SqlOperator operator, Function<Integer, Optional<String>> argGenerator) {
        String[] args = new String[operator.getArity()];
        for (int i = 0; i < operator.getArity(); i++) {
            Optional<String> arg = argGenerator.apply(i);
            if (!arg.isPresent()) {
                return Optional.empty();
            }
            args[i] = arg.get();
        }
        return Optional.of(operator.format(args));
    }

}
