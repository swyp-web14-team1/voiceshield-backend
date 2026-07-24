package com.swyp.voiceshield;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CaseVariantMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void separatesCaseScenariosFromMessageAndVoiceVariants() {
        assertEquals(1, tableCount("case_scenarios"));
        assertEquals(1, tableCount("case_variants"));
        assertEquals(1, columnCount("case_variants", "channel"));
        assertEquals(1, columnCount("case_variants", "scenario_id"));
    }

    private int tableCount(String tableName) {
        return count("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME = ?", tableName);
    }

    private int columnCount(String tableName, String columnName) {
        return count("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE TABLE_SCHEMA = 'public' AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                tableName, columnName);
    }

    private int count(String sql, String... parameters) {
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, (Object[]) parameters);
        return count == null ? 0 : count;
    }
}
