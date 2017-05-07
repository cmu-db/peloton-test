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
public class FromClass implements Ast.Elem {

    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        String table = RandomUtils.randomElement(db.tables(), db.tableCount(), random);
        return Optional.of(new Ast.Clause(table, context.addToScope(Ast.Sort.TABLE, table)));
    }
}
