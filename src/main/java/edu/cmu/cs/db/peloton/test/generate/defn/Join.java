package edu.cmu.cs.db.peloton.test.generate.defn;

import com.google.common.collect.Sets;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.defn.operators.SqlBinaryOperator;
import edu.cmu.cs.db.peloton.test.generate.defn.operators.SqlUnaryOperator;
import edu.cmu.cs.db.peloton.test.generate.util.RandomUtils;

import java.util.*;

/**
 * Created by tianyuli on 5/7/17.
 */
public class Join implements Ast.Elem {
    private static List<String> joins = Arrays.asList("JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL JOIN");

    @Override
    public Optional<Ast.Clause> generate(DatabaseDefinition db, Context context, Random random) {
        String leftTable = RandomUtils.randomElement(db.tables(), random);
        String rightTable = RandomUtils.randomElement(db.tables(), random);

        if (leftTable.equals(rightTable)) {
            return Optional.empty();
        }

        Optional<String> condition = new SearchCondition(Sets.union(EnumSet.allOf(SqlBinaryOperator.class),
                EnumSet.allOf(SqlUnaryOperator.class)), 2)
                .generate(db, context, random)
                .map(Ast.Clause::getClause);
        if (!condition.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new Ast.Clause(formatJoin(random, leftTable, rightTable, condition.get()),
                          context.addToScope(Ast.Sort.TABLE, leftTable).addToScope(Ast.Sort.TABLE, rightTable)));
    }

    private String formatJoin(Random random, String left, String right, String condition) {
        return String.format("%s %s %s ON %s", left, RandomUtils.randomElement(joins, random), right, condition);
    }
}
