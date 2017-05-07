package edu.cmu.cs.db.peloton.test.generate.defn;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.Optional;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public class PropertySpec implements Ast.Elem {

    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        String table = RandomUtils.randomElement(context.valuesOf(Ast.Sort.TABLE), random);
        String column = String.format("%s.%s", table,
                RandomUtils.randomElement(db.getTable(table).entrySet(), random).getKey());
        return Optional.of(new Ast.Clause(column, context.addToScope(Ast.Sort.COLUMN, column)));
    }
}
