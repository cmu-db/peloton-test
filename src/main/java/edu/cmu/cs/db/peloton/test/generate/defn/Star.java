package edu.cmu.cs.db.peloton.test.generate.defn;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;

import java.util.Optional;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public class Star implements Ast.Elem {
    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        return Optional.of(new Ast.Clause("*", getNewContext(db, context)));
    }

    private static Context getNewContext(DatabaseDefinition db, Context context) {
        Context.Builder result = new Context.Builder();
        result.putAll(context);
        for (String table : context.valuesOf(Ast.Sort.TABLE)) {
            for (String column : db.getTable(table).keySet()) {
                result.put(Ast.Sort.COLUMN, String.format("%s.%s", table, column));
            }
        }
        return result.build();
    }
}
