package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.storage.Database;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DatabaseImpl implements Database {
    private static final String CREATE_IF_NOT_EXISTS
            =
            "CREATE TABLE IF NOT EXISTS \"content_item\" (\n" +
                    "\t\"id\"\tINTEGER UNIQUE,\n" +
                    "\t\"icon\"\tBLOB,\n" +
                    "\t\"title\"\tTEXT NOT NULL,\n" +
                    "\t\"subtitle\"\tTEXT,\n" +
                    "\t\"content\"\tTEXT,\n" +
                    "\t\"list_id\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                    ");" +
                    "CREATE TABLE IF NOT EXISTS \"image_item\" (\n" +
                    "\t\"id\"\tINTEGER UNIQUE,\n" +
                    "\t\"image\"\tBLOB NOT NULL,\n" +
                    "\t\"list_id\"\tINTEGER,\n" +
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                    ");" +
                    "CREATE TABLE IF NOT EXISTS \"page\" (\n" +
                    "\t\"id\"\tINTEGER UNIQUE,\n" +
                    "\t\"avatar_id\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT,\n" +
                    "\t\"quote\"\tTEXT,\n" +
                    "\t\"top_content_title\"\tINTEGER,\n" +
                    "\t\"top_content\"\tTEXT,\n" +
                    "\t\"main_list_title\"\tTEXT,\n" +
                    "\t\"main_list_id\"\tINTEGER,\n" +
                    "\t\"sub_list_title\"\tTEXT,\n" +
                    "\t\"sub_list_id\"\tINTEGER,\n" +
                    "\t\"contacts_list_title\"\tTEXT,\n" +
                    "\t\"contacts_list_id\"\tINTEGER,\n" +
                    "\t\"mini_list_title\"\tTEXT,\n" +
                    "\t\"mini_list_id\"\tINTEGER,\n" +
                    "\t\"gallery_list_id\"\tINTEGER,\n" +
                    "\t\"dog\"\tTEXT,\n" +
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                    ");" +
                    "CREATE TABLE IF NOT EXISTS \"offer\" (\n" +
                    "\t\"id\"\tINTEGER UNIQUE,\n" +
                    "\t\"first_name\"\tTEXT NOT NULL,\n" +
                    "\t\"middle_name\"\tTEXT,\n" +
                    "\t\"last_name\"\tTEXT,\n" +
                    "\t\"company\"\tTEXT,\n" +
                    "\t\"phone\"\tTEXT NOT NULL,\n" +
                    "\t\"email\"\tTEXT,\n" +
                    "\t\"offer\"\tTEXT NOT NULL,\n" +
                    "\t\"created_at\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                    ");" +
                    "CREATE TABLE IF NOT EXISTS \"visit\" (\n" +
                    "\t\"id\"\tINTEGER UNIQUE,\n" +
                    "\t\"visited_at\"\tINTEGER NOT NULL,\n" +
                    "\t\"identifier\"\tTEXT NOT NULL,\n" +
                    "\t\"offer_id\"\tINTEGER,\n" +
                    "\tPRIMARY KEY(\"id\" AUTOINCREMENT)\n" +
                    ");";

    private Connection instance;

    public DatabaseImpl init(String databaseName) {

        try {
            instance = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
        } catch (SQLException e) {
            Logger.getLogger(DatabaseImpl.class.getName()).log(Level.WARNING, e, e::getLocalizedMessage);
        }

        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(CREATE_IF_NOT_EXISTS);
        } catch (SQLException e) {
            Logger.getLogger(DatabaseImpl.class.getName()).log(Level.WARNING, e, e::getLocalizedMessage);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                instance.close();
            } catch (SQLException e) {
                Logger.getLogger(DatabaseImpl.class.getName()).log(Level.WARNING, "Can't close a real database connection");
            }
        }));
        return this;
    }

    @PostConstruct
    public void init() {
        init("database.db");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new ConnectionWrapper();
    }

    @Override
    public Long getLastPrimaryKeyFor(String tableName) throws SQLException {
        PreparedStatement statement = instance.prepareStatement("SELECT seq FROM sqlite_sequence WHERE name = ?");
        statement.setString(1, tableName);
        ResultSet rs = statement.executeQuery();
        Long res = rs.getLong(1);
        rs.close();
        statement.close();
        return res;
    }

    private class ConnectionWrapper implements Connection {
        @Override
        public void close() {
            Logger.getLogger(ConnectionWrapper.class.getName()).log(Level.FINEST, "Connection-wrapper released");
        }

        @Override
        public Statement createStatement() throws SQLException {
            return instance.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return instance.prepareStatement(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return instance.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return instance.nativeSQL(sql);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return instance.getAutoCommit();
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            instance.setAutoCommit(autoCommit);
        }

        @Override
        public void commit() throws SQLException {
            instance.commit();
        }

        @Override
        public void rollback() throws SQLException {
            instance.rollback();
        }

        @Override
        public boolean isClosed() throws SQLException {
            return instance.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return instance.getMetaData();
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return instance.isReadOnly();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            instance.setReadOnly(readOnly);
        }

        @Override
        public String getCatalog() throws SQLException {
            return instance.getCatalog();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            instance.setCatalog(catalog);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return instance.getTransactionIsolation();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            instance.setTransactionIsolation(level);
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return instance.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            instance.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return instance.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return instance.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return instance.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return instance.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            instance.setTypeMap(map);
        }

        @Override
        public int getHoldability() throws SQLException {
            return instance.getHoldability();
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            instance.setHoldability(holdability);
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return instance.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return instance.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            instance.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            instance.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return instance.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return instance.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return instance.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return instance.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return instance.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return instance.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return instance.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return instance.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return instance.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return instance.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return instance.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            instance.setClientInfo(name, value);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return instance.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return instance.getClientInfo();
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            instance.setClientInfo(properties);
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return instance.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return instance.createStruct(typeName, attributes);
        }

        @Override
        public String getSchema() throws SQLException {
            return instance.getSchema();
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            instance.setSchema(schema);
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            instance.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            instance.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return instance.getNetworkTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return instance.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return instance.isWrapperFor(iface);
        }
    }
}
