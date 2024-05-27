package com.valdisdot.mycv.storage.impl;

import com.valdisdot.mycv.storage.Database;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseImplTest {
    private final Database database = new DatabaseImpl().init("test.db");

    @Test
    @Order(1)
    void getConnection() {
        assertDoesNotThrow(database::getConnection);
    }

    @Test
    @Order(2)
    void doSelect() {
        assertDoesNotThrow(() -> {
            try (Statement statement = database.getConnection().createStatement()) {
                statement.executeQuery("SELECT * FROM sqlite_sequence");
            }
        });
    }
}
