package com.valdisdot.mycv.storage;

import java.sql.Connection;
import java.sql.SQLException;

public interface Database {
    Connection getConnection() throws SQLException;

    Long getLastPrimaryKeyFor(String tableName) throws SQLException;
}
