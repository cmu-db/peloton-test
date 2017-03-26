package edu.cmu.cs.db.peloton.test.common;

import edu.cmu.cs.db.peloton.test.generate.Context;

import java.sql.*;

/**
 * Provide access to a database for the application
 */
public abstract class DatabaseWrapper {
    /**
     * Gets connection.
     *
     * @param hostname the hostname
     * @param port     the port
     * @param dbName   the db name
     * @return the connection
     * @throws SQLException the sql exception
     */
    public abstract Connection getConnection(String hostname, int port, String dbName) throws SQLException;

    /**
     * Gets database definition.
     *
     * @param hostname the hostname
     * @param port     the port
     * @param dbName   the db name
     * @return the database definition
     */
    public DatabaseDefinition getDatabaseDefinition(String hostname, int port, String dbName) {
            DatabaseDefinition.Builder result = new DatabaseDefinition.Builder();
        try (Connection conn = getConnection(hostname, port, dbName)) {
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

    private static void addTable(DatabaseMetaData metaData,
                                            String table,
                                            DatabaseDefinition.Builder builder) throws SQLException {
        ResultSet columns = metaData.getColumns(null, null, table, null);
        while (columns.next()) {
            builder.column(columns.getString("COLUMN_NAME"),
                    columns.getString("TYPE_NAME"));
        }
    }
}
