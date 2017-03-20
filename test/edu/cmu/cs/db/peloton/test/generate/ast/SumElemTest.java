package edu.cmu.cs.db.peloton.test.generate.ast;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.Context;
import edu.cmu.cs.db.peloton.test.generate.Iterators;
import org.junit.Test;

import java.util.List;

import static edu.cmu.cs.db.peloton.test.generate.ast.TestUtil.clauseOf;
import static org.junit.Assert.*;

public class SumElemTest {
    @Test
    public void allClauses() throws Exception {
        Ast.Elem left = new StubElem("foo", "bar");
        Ast.Elem right = new StubElem("baz", "qux");
        Ast.Elem tested = new SumElem() {
            @Override
            protected ImmutableList<Ast.Elem> args() {
                return ImmutableList.of(left, right);
            }
        };

        List<Ast.Clause> values = Iterators.toList(tested.allClauses(Context.empty()));
        assertTrue(values.contains(clauseOf("foo")));
        assertTrue(values.contains(clauseOf("bar")));
        assertTrue(values.contains(clauseOf("baz")));
        assertTrue(values.contains(clauseOf("qux")));
        assertEquals(4, values.size());
    }

}