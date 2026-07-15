package com.swyp.voiceshield;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class FlywayPostgreSqlSupportTest {

    @Test
    void includesPostgreSqlDatabaseSupport() {
        assertDoesNotThrow(() -> Class.forName("org.flywaydb.database.postgresql.PostgreSQLDatabaseType"));
    }
}
