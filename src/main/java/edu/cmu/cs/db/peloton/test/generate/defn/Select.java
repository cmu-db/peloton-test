package edu.cmu.cs.db.peloton.test.generate.defn;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import edu.cmu.cs.db.peloton.test.common.DatabaseDefinition;
import edu.cmu.cs.db.peloton.test.generate.ast.Ast;
import edu.cmu.cs.db.peloton.test.generate.ast.Context;
import edu.cmu.cs.db.peloton.test.generate.ast.ListElem;
import edu.cmu.cs.db.peloton.test.generate.ast.ProductElem;
import edu.cmu.cs.db.peloton.test.generate.defn.operators.SqlBinaryOperator;
import edu.cmu.cs.db.peloton.test.generate.defn.operators.SqlUnaryOperator;

import java.sql.JDBCType;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by tianyuli on 4/1/17.
 */
public class Select extends ProductElem {

    public static void main(String[] args) {
        DatabaseDefinition db = new DatabaseDefinition.Builder()
                .table("foo")
                .column("id", JDBCType.INTEGER)
                .column("value", JDBCType.VARCHAR)
                .table("bar")
                .column("id", JDBCType.INTEGER)
                .column("value", JDBCType.DOUBLE)
                .build();
        Random random = new Random();
        Select select = new Select();
        for (int i = 0; i < 20; i++) {
            System.out.println(select.generateUntilPresent(db, Context.EMPTY, random).getClause());
        }
    }

    @Override
    protected ImmutableList<Ast.Elem> args() {
        return ImmutableList.of(
                new FromProp(),
                new SelectProp(),
                new SearchCondition(Sets.union(EnumSet.allOf(SqlBinaryOperator.class),
                        EnumSet.allOf(SqlUnaryOperator.class)), 3));
    }

    @Override
    protected String format(List<String> args) {
        return String.format("SELECT %s FROM %s WHERE %s;", args.get(1), args.get(0), args.get(2));
    }
}
