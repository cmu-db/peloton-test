package edu.cmu.cs.db.peloton.test.generate.defn.exhaustive;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.exhaustive.ListElem;
import edu.cmu.cs.db.peloton.test.generate.ast.exhaustive.ProductElem;
import edu.cmu.cs.db.peloton.test.generate.defn.exhaustive.where.SearchCondition;

import java.util.List;

/**
 * Created by tianyuli on 3/20/17.
 */
public class Select extends ProductElem {
    @Override
    protected ImmutableList<Ast.Elem> args() {
        // FROM ... SELECT ... WHERE...
        return ImmutableList.of(new ListElem(new FromClass()), new SelectProp(), new ListElem(new SearchCondition()));
    }

    @Override
    protected String format(List<String> args) {
        return String.format("SELECT %s FROM %s WHERE %s;", args.get(1), args.get(0), args.get(2));
    }
}
