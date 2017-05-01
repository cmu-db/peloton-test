package edu.cmu.cs.db.peloton.test.generate.defn.stochastic.operators;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.*;

/**
 * Created by tianyuli on 5/1/17.
 */
public enum SqlBinaryOperator {
    // TODO add more operators
    PLUS("+", NUMERIC, NUMERIC, NUMERIC),
    MINUS("-", NUMERIC, NUMERIC, NUMERIC),
    TIMES("*", NUMERIC, NUMERIC, NUMERIC),
    DIV("/", NUMERIC, NUMERIC, NUMERIC),
    CONCAT("||", VARCHAR, VARCHAR, VARCHAR),
    EQ_NUM("=", NUMERIC, NUMERIC, BOOLEAN),
    EQ_VARCHAR("=", VARCHAR, VARCHAR, BOOLEAN),
    NOT_EQ_NUM("!=", NUMERIC, NUMERIC, BOOLEAN),
    NOT_EQ_VARCHAR("!=", VARCHAR, VARCHAR, BOOLEAN),
    GT_NUM(">", NUMERIC, NUMERIC, BOOLEAN),
    GT_VARCHAR(">", VARCHAR, VARCHAR, BOOLEAN),
    AND("AND", BOOLEAN, BOOLEAN, BOOLEAN),
    OR("OR", BOOLEAN, BOOLEAN, BOOLEAN);


    private final String operator;
    private final Ast.ExpressionType arg1Type, arg2Type, resultType;

    SqlBinaryOperator(String operator,
                      Ast.ExpressionType arg1Type,
                      Ast.ExpressionType arg2Type,
                      Ast.ExpressionType resultType) {
        this.operator = operator;
        this.arg1Type = arg1Type;
        this.arg2Type = arg2Type;
        this.resultType = resultType;
    }

    public Ast.ExpressionType getArg1Type() {
        return arg1Type;
    }

    public Ast.ExpressionType getArg2Type() {
        return arg2Type;
    }

    public Ast.ExpressionType getResultType() {
        return resultType;
    }

    public String format(String arg1, String arg2) {
        return String.format("(%s %s %s)", arg1, operator, arg2);
    }
}
