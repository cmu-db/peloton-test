package edu.cmu.cs.db.peloton.test.generate.defn.exhaustive;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.exhaustive.ListElem;
import edu.cmu.cs.db.peloton.test.generate.ast.exhaustive.SumElem;

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
                // TODO: temporarily changed to keep down combination size
                // .map(prop -> new ListElem(new AggregateFunc(prop)))
                .map(prop -> new AggregateFunc(prop))
                .collect(Collectors.toList());

        return new ImmutableList.Builder<Ast.Elem>()
                .addAll(baseProps)
                .addAll(aggregateProps)
                .build();
    }
}
