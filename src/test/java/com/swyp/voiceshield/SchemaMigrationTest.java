package com.swyp.voiceshield;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SchemaMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void createsCategoryAndCaseTables() {
        assertEquals(1, tableCount("categories"));
        assertEquals(1, tableCount("case_scenarios"));
        assertEquals(1, tableCount("case_variants"));
    }

    private int tableCount(String tableName) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME = ?",
                Integer.class,
                tableName.toUpperCase()
        );
        return count == null ? 0 : count;
    }
}
