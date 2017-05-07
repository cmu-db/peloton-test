package edu.cmu.cs.db.peloton.test.generate.defn.operators;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.function.Function;

import static com.google.common.base.Preconditions.*;
import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.*;

/**
 * Created by tianyuli on 5/1/17.
 */
public enum SqlUnaryOperator implements SqlOperator {
    POS(arg -> String.format("(+%s)", arg), NUMERIC, NUMERIC, ConstantOption.NO_CONSTANT),
    NEG(arg -> String.format("(-%s)", arg), NUMERIC, NUMERIC, ConstantOption.NO_CONSTANT),
    NULL_CHECK(arg -> String.format("(%s IS NULL)", arg), ANY, BOOLEAN, ConstantOption.NO_CONSTANT),
    NOT(arg -> String.format("(NOT %s)", arg), BOOLEAN, BOOLEAN, ConstantOption.NO_CONSTANT);


    private final Function<String, String> format;
    private final Ast.ExpressionType argType, resultType;
    private final ConstantOption allowConstant;

    SqlUnaryOperator(Function<String, String> format,
                     Ast.ExpressionType argType,
                     Ast.ExpressionType resultType,
                     ConstantOption allowConstant) {
        this.format = format;
        this.argType = argType;
        this.resultType = resultType;
        this.allowConstant = allowConstant;
    }

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public Ast.ExpressionType getArgType(int i) {
        if (i == 0) {
            return argType;
        }
        throw new IndexOutOfBoundsException("Operator only has " + getArity() + " argument(s)");
    }

    @Override
    public Ast.ExpressionType getResultType() {
        return resultType;
    }

    @Override
    public String format(String... args) {
        checkArgument(args.length == 1);
        return format.apply(args[0]);
    }

    @Override
    public ConstantOption getConstantOption() {
        return allowConstant;
    }
}
