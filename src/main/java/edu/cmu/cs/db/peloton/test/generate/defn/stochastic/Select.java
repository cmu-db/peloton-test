package edu.cmu.cs.db.peloton.test.generate.defn.stochastic;

import com.google.common.collect.ImmutableList;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.ast.stochastic.StochasticListElem;
import edu.cmu.cs.db.peloton.test.generate.ast.stochastic.StochasticProductElem;
import edu.cmu.cs.db.peloton.test.generate.defn.stochastic.operators.SqlBinaryOperator;
import edu.cmu.cs.db.peloton.test.generate.defn.stochastic.operators.SqlUnaryOperator;

import javax.xml.crypto.Data;
import java.sql.JDBCType;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public class Select extends StochasticProductElem {

    public static void main(String[] args) {
        DatabaseDefinition db = new DatabaseDefinition.Builder()
                .table("foo")
                .column("id", JDBCType.INTEGER, null)
                .column("value", JDBCType.VARCHAR, null)
                .table("bar")
                .column("id", JDBCType.INTEGER, null)
                .column("value", JDBCType.DOUBLE, null)
                .build();
        Random random = new Random();
        Select select = new Select();
        for (int i = 0; i < 20; i++) {
            System.out.println(select.generate(db, Context.EMPTY, random).getClause());
        }
    }

    @Override
    protected ImmutableList<Ast.StochasticElem> args() {
        return ImmutableList.of(
                new StochasticListElem(new FromClass(), 10, ","),
                new SelectProp(),
                new SearchCondition(EnumSet.allOf(SqlBinaryOperator.class),
                        EnumSet.allOf(SqlUnaryOperator.class), 4));
    }

    @Override
    protected String format(List<String> args) {
        return String.format("SELECT %s FROM %s WHERE %s;", args.get(1), args.get(0), args.get(2));
    }
}
