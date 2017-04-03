package edu.cmu.cs.db.peloton.test.generate.ast.stochastic;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.*;

/**
 * Created by tianyuli on 4/1/17.
 */
public class StochasticListElem implements Ast.StochasticElem {
    private final Ast.StochasticElem elem;
    private final int limit;
    private final List<String> delimiters;

    public StochasticListElem(Ast.StochasticElem elem, int limit, String... delimiters) {
        this.elem = elem;
        this.limit = limit;
        this.delimiters = Arrays.asList(delimiters);
    }

    @Override
    public Ast.Clause generate(DatabaseDefinition db, Context context, Random random) {
        int length = random.nextInt(limit) + 1;
        Set<Ast.Clause> result = new HashSet<>();
        for (int i = 0; i < length; i++) {
            result.add(elem.generate(db, context, random));
        }
        return formatList(new ArrayList<>(result), delimiters, random);

    }

    private static Ast.Clause formatList(List<Ast.Clause> elems, List<String> delimiters, Random random) {
        StringBuilder builder = new StringBuilder();
        Context context = Context.EMPTY;
        for (int i = 0; i < elems.size(); i++) {
            Ast.Clause clause = elems.get(i);
            builder.append(clause.getClause());
            if (i + 1 < elems.size()) {
                builder.append(RandomUtils.randomElement(delimiters, random));
            }
            context = context.union(clause.getContext());
        }
        return new Ast.Clause(builder.toString(), context);
    }
}
