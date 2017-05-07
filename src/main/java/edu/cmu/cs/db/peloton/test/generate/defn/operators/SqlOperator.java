package edu.cmu.cs.db.peloton.test.generate.defn.operators;

import edu.cmu.cs.db.peloton.test.generate.ast.Ast;

/**
 * Created by tianyuli on 5/1/17.
 */
public interface SqlOperator {
    int getArity();

    Ast.ExpressionType getArgType(int i);

    Ast.ExpressionType getResultType();

    String format(String... args);

    ConstantOption getConstantOption();
}
