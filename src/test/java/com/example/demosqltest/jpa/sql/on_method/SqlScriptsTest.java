package com.example.demosqltest.jpa.sql.on_method;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class SqlScriptsTest {

    @Autowired
    HomeRepository homeRepository;

    @DisplayName("mixed statements and scripts")
    @Sql(statements = "insert into home_entity (id, name) values (1, 'Васька');")
    @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(separator = ";", commentPrefix = "//")
    )
    @Test
    public void findItMixStatementAndScript() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            assertEquals("Васька", byId.get().getName());
        }else{
            fail();
        }

    }


    @Sql(value = "classpath:insert-test-data.sql")
    @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(separator = ";", commentPrefix = "//")
    )
    @Test
    public void disJointNotFoundIfBothEqual() {
        Optional<HomeEntity> byDisjoint = homeRepository.findByDisjointFailed(1L, "Васька-1");
        assertTrue(byDisjoint.isEmpty());
    }

    @Sql(value = "classpath:insert-test-data.sql")
    @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            config = @SqlConfig(separator = ";", commentPrefix = "//")
    )
    @Test
    public void disJointFoundIfFirstEqualAndSecondNot() {
        Optional<HomeEntity> byDisjoint = homeRepository.findByDisjointFailed(1L, "Васька-2");
        assertTrue(byDisjoint.isEmpty());
    }


}