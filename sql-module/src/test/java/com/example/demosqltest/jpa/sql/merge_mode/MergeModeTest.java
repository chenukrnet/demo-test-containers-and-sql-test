package com.example.demosqltest.jpa.sql.merge_mode;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test-containers")
@SpringBootTest
@Slf4j
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(statements = "insert into home_entity (id, name) values (1, 'Васька');")
class MergeModeTest {

    @Autowired
    HomeRepository homeRepository;

    @DisplayName("found because merge class level @Sql with method, configured at class level @SqlMergeMode")
    @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(separator = ";", commentPrefix = "//")
    )
    @Test
    public void testFindIt() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            assertEquals("Васька", byId.get().getName());
        }

    }


    @DisplayName("found because merge class level override in method level, note found other value than provided by class level @Sql")
    @SqlMergeMode(SqlMergeMode.MergeMode.OVERRIDE)
    @Sql(statements = "insert into home_entity (id, name) values (1, 'Васька-2');")
    @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(separator = ";", commentPrefix = "//")
    )
    @Test
    public void testFindItOverride() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            assertEquals("Васька-2", byId.get().getName());
        }

    }


}