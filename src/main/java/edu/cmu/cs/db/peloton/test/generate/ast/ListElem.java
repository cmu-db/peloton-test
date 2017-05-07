package edu.cmu.cs.db.peloton.test.generate.ast;

import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.*;

/**
 * Created by tianyuli on 4/1/17.
 */
public class ListElem implements Ast.Elem {
    private final Ast.Elem elem;
    private final int limit;
    private final List<String> delimiters;

    public ListElem(Ast.Elem elem, int limit, String... delimiters) {
        this.elem = elem;
        this.limit = limit;
        this.delimiters = Arrays.asList(delimiters);
    }

    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        int length = random.nextInt(limit) + 1;
        Set<Ast.Clause> result = new HashSet<>();
        for (int i = 0; i < length; i++) {
            Optional<Ast.Clause> sub = elem.generate(db, context, random);
            if (sub.isPresent()) {
                result.add(sub.get());
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(formatList(new ArrayList<>(result), delimiters, random));

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
