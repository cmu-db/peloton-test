package edu.cmu.cs.db.peloton.test.common;

import java.sql.*;
import java.util.Map;

/**
 * Provide access to a database for the application
 */
public class DatabaseWrapper implements AutoCloseable {
    private Map<SQLType, Hint> valuePopulationHints;
    private Connection conn;
    private DatabaseDefinition definition;
    private final String dbString, username, password;


    public DatabaseWrapper(Map<SQLType, Hint> hints, String dbString, String username, String password) {
        this.valuePopulationHints = hints;
        conn = null;
        this.dbString = dbString;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets connection.
     *
     * @return the connection to specified database. The connection is established the first
     * time this method is called
     * @throws SQLException the sql exception
     */
    public Connection getConnection() throws SQLException {
        if (conn == null) {
            if (username.equals("")) {
                conn = DriverManager.getConnection(dbString);
            } else {
                conn = DriverManager.getConnection(dbString, username, password);
            }
        }
        return conn;
    }

    /**
     * Gets database definition.
     *
     * @return the database definition
     */
    public DatabaseDefinition getDatabaseDefinition() {
        if (definition != null) {
            return definition;
        }

        DatabaseDefinition.Builder result = new DatabaseDefinition.Builder();
        try {
            Connection conn = getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                result.table(tableName);
                addTable(metaData, tableName, result);
            }
            definition = result.build();
            return definition;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    private void addTable(DatabaseMetaData metaData,
                                 String table,
                                 DatabaseDefinition.Builder builder) throws SQLException {
        ResultSet columns = metaData.getColumns(null, null, table, null);
        while (columns.next()) {
            JDBCType type = JDBCType.valueOf(columns.getInt("DATA_TYPE"));
            builder.column(columns.getString("COLUMN_NAME"), type, valuePopulationHints.get(type));
        }
    }
}
