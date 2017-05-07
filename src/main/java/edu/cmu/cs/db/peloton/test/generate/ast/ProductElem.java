package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public abstract class ProductElem implements Ast.Elem {
    protected abstract List<Ast.Elem> args();

    protected abstract String format(List<String> args);

    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        Context gamma = context;
        List<String> result = new ArrayList<>();
        for (Ast.Elem e : args()) {
            Optional<Ast.Clause> clause = e.generate(db, gamma, random);
            if (!clause.isPresent()) {
                return Optional.empty();
            }
            gamma = clause.get().getContext();
            result.add(clause.get().getClause());
        }
        return Optional.of(new Ast.Clause(format(result), gamma));
    }

}
