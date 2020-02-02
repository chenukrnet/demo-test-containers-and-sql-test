package com.example.demosqltest.jpa.sqlconfig;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import com.example.demosqltest.spy6.CustomSingleEventListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test-containers")
@SpringBootTest
@Slf4j
/**
 * Используется как глобальная настройка если не переопределно в   @Sql( config =... )
 * */
@SqlConfig(commentPrefix = "//", blockCommentStartDelimiter = "/*", blockCommentEndDelimiter = "*/", errorMode = SqlConfig.ErrorMode.IGNORE_FAILED_DROPS)
class SqlConfigTest {

    @Autowired
    HomeRepository homeRepository;

    @Autowired
    CustomSingleEventListener customSingleEventListener;





    @Sql(statements = "insert into home_entity (id, name) values (1, 'Васька');")
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

        while (customSingleEventListener.isEventPresent()){
        Optional<String> sqlEvent = customSingleEventListener.getSqlEvent();
            if(sqlEvent.isPresent()){
                log.info(">>>>SQL:{}", sqlEvent.get());
            }
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





}