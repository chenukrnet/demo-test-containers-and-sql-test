package com.example.demosqltest.jpa.sql.on_class;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
@Sql(scripts = "classpath:insert-test-data.sql")
class HomeRepositoryTest {

    @Autowired
    HomeRepository homeRepository;

    @DisplayName("not found because override class level @Sql by method")
    @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(separator = ";", commentPrefix = "//")
    )
    @Test
    public void failBecauseMergeMode() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);

        if (byId.isPresent()) {
            assertEquals("Васька-1", byId.get().getName());
            fail();
        } else {
            Assertions.assertTrue(true);
        }


    }

    @DisplayName("found because merge class level @Sql by method")
    @SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
    @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(separator = ";", commentPrefix = "//")
    )
    @Test
    public void testFindIt2() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            assertEquals("Васька-1", byId.get().getName());
        } else {
            fail();
        }


    }


}