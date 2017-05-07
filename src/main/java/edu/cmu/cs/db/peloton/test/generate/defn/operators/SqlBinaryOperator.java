package edu.cmu.cs.db.peloton.test.generate.defn.operators;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

import static com.google.common.base.Preconditions.*;
import static edu.cmu.cs.db.peloton.test.generate.ast.Ast.ExpressionType.*;

/**
 * Created by tianyuli on 5/1/17.
 */
public enum SqlBinaryOperator implements SqlOperator {
    // TODO add more operators
    PLUS("+", NUMERIC, NUMERIC, NUMERIC, ConstantOption.NO_RESTRICTION),
    MINUS("-", NUMERIC, NUMERIC, NUMERIC, ConstantOption.NO_RESTRICTION),
    TIMES("*", NUMERIC, NUMERIC, NUMERIC, ConstantOption.NO_RESTRICTION),
    DIV("/", NUMERIC, NUMERIC, NUMERIC, ConstantOption.NO_RESTRICTION),
    CONCAT("||", VARCHAR, VARCHAR, VARCHAR, ConstantOption.NOT_ALL_CONSTANTS),
    EQ_NUM("=", NUMERIC, NUMERIC, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    EQ_VARCHAR("=", VARCHAR, VARCHAR, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    NOT_EQ_NUM("!=", NUMERIC, NUMERIC, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    NOT_EQ_VARCHAR("!=", VARCHAR, VARCHAR, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    GT_NUM(">", NUMERIC, NUMERIC, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    GT_VARCHAR(">", VARCHAR, VARCHAR, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    LT_NUM("<", NUMERIC, NUMERIC, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    LT_VARCHAR("<", VARCHAR, VARCHAR, BOOLEAN, ConstantOption.NOT_ALL_CONSTANTS),
    AND("AND", BOOLEAN, BOOLEAN, BOOLEAN, ConstantOption.NO_CONSTANT),
    OR("OR", BOOLEAN, BOOLEAN, BOOLEAN, ConstantOption.NO_CONSTANT);


    private final String op;
    private final Ast.ExpressionType arg1, arg2, result;
    private final ConstantOption allowConstant;


    SqlBinaryOperator(String op,
                      Ast.ExpressionType arg1,
                      Ast.ExpressionType arg2,
                      Ast.ExpressionType result,
                      ConstantOption allowConstant) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
        this.allowConstant = allowConstant;
    }

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public Ast.ExpressionType getArgType(int i) {
        switch (i) {
            case 0:
                return arg1;
            case 1:
                return arg2;
            default:
                throw new IndexOutOfBoundsException("Operator only has " + getArity() + " argument(s)");
        }
    }

    @Override
    public Ast.ExpressionType getResultType() {
        return result;
    }

    @Override
    public String format(String... args) {
        checkArgument(args.length == 2);
        return String.format("(%s %s %s)", args[0], op, args[1]);
    }

    @Override
    public ConstantOption getConstantOption() {
        return allowConstant;
    }


}
