package edu.cmu.cs.db.peloton.test.generate.defn.stochastic.operators;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import java.util.function.Function;

import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.*;

/**
 * Created by tianyuli on 5/1/17.
 */
public enum SqlUnaryOperator {
    POS(arg -> String.format("(+%s)", arg), NUMERIC, NUMERIC),
    NEG(arg -> String.format("(-%s)", arg), NUMERIC, NUMERIC),
    NULL_CHECK(arg -> String.format("(%s IS NULL)", arg), ANY, BOOLEAN),
    NOT(arg -> String.format("(NOT %s)", arg), BOOLEAN, BOOLEAN);


    private final Function<String, String> format;
    private final Ast.ExpressionType argType, resultType;

    SqlUnaryOperator(Function<String, String> format,
                     Ast.ExpressionType argType,
                     Ast.ExpressionType resultType) {
        this.format = format;
        this.argType = argType;
        this.resultType = resultType;
    }

    public Ast.ExpressionType getArgType() {
        return argType;
    }

    public Ast.ExpressionType getResultType() {
        return resultType;
    }

    public String format(String arg) {
        return format.apply(arg);
    }
}
