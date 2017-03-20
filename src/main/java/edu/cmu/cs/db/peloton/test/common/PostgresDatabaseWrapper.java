package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.Context;

import java.sql.*;

/**
 * Created by tianyuli on 3/20/17.
 */
public class PostgresDatabaseWrapper implements DatabaseWrapper{
    @Override
    public Context getContext(String hostname, int port, String dbName) {
            Context.Builder result = new Context.Builder();
        try (Connection conn = DriverManager.getConnection(
                 String.format("jdbc:postgresql://%s:%d/%s", hostname, port, dbName))) {
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                result.table(tableName);
                addTable(metaData, tableName, result);
            }
            return result.build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Context.Builder addTable(DatabaseMetaData metaData, String table, Context.Builder builder) throws SQLException {
        ResultSet columns = metaData.getColumns(null, null, table, null);
        while (columns.next()) {
            builder.column(columns.getString("COLUMN_NAME"),
                    columns.getString("TYPE_NAME"));
        }
        return builder;
    }
}
