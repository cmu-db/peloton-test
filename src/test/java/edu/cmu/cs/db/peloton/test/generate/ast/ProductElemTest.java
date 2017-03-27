package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.Iterators;
import org.junit.Test;

import java.util.List;

import static edu.cmu.cs.db.peloton.test.generate.ast.TestUtil.clauseOf;
import static org.junit.Assert.*;

public class ProductElemTest {
    @Test
    public void allClauses() throws Exception {
        Ast.Elem left = new StubElem("foo", "bar");
        Ast.Elem right = new StubElem("1", "2");
        Ast.Elem tested = new ProductElem() {
            @Override
            protected ImmutableList<Ast.Elem> args() {
                return ImmutableList.of(left, right);
            }

            @Override
            protected String format(List<String> args) {
                return String.format("%s op %s", args.get(0), args.get(1));
            }
        };

        List<Ast.Clause> values = Iterators.toList(tested.allClauses(null, Context.EMPTY, 0));
        assertTrue(values.contains(clauseOf("foo op 1")));
        assertTrue(values.contains(clauseOf("foo op 2")));
        assertTrue(values.contains(clauseOf("bar op 1")));
        assertTrue(values.contains(clauseOf("bar op 2")));
        assertEquals(4, values.size());
    }

}