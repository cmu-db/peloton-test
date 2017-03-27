package edu.cmu.cs.db.peloton.test.generate.ast;


public class TestUtil {
    public static Ast.Clause clauseOf(String s) {
        return new Ast.Clause(s, Context.EMPTY);
    }
}
