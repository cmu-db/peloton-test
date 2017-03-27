package edu.cmu.cs.db.peloton.test.generate.defn;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.ListElem;
import edu.cmu.cs.db.peloton.test.generate.ast.SumElem;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SelectProp - SQL SELECT statement.
 * baseProp:
 *  - table_name.column_name
 *  - table_name.*
 *  - *
 * aggregateProp:
 *  - aggregate_function(prop)
 *
 * Not supporting (yet):
 * - AS
 * - DISTINCT
 * - CONCAT
 */
public class SelectProp extends SumElem {
    @Override
    protected ImmutableList<Ast.Elem> args() {
        ImmutableList<Ast.Elem> baseProps = ImmutableList.of(
                new ListElem(new PropertySpec()),
                new ListElem(new ClassAliasStar()),
                new Star()
        );

        List<Ast.Elem> aggregateProps = baseProps.stream()
                .map(prop -> new ListElem(new AggregateFunc(prop)))
                .collect(Collectors.toList());

        return new ImmutableList.Builder<Ast.Elem>()
                .addAll(baseProps)
                .addAll(aggregateProps)
                .build();
    }
}
