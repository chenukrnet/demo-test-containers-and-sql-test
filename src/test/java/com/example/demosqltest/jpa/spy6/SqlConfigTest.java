package com.example.demosqltest.jpa.spy6;

import com.example.demosqltest.jpa.repositories.HomeRepository;
import com.example.demosqltest.spy6.CustomSingleEventListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@ActiveProfiles("spy6")
@SpringBootTest
@Slf4j
/**
 * Используется как глобальная настройка если не переопределно в   @Sql( config =... )
 * */
class SqlConfigTest {

    @Autowired
    HomeRepository homeRepository;

    @Autowired
    CustomSingleEventListener customSingleEventListener;


    @Sql(scripts = "classpath:insert-test-data.sql")
    @Test
    public void disJointFoundIfFirstEqualAndSecondNot() {
        customSingleEventListener.cleanUpSqlList();

        Executable runnable = () -> {
            executeQuery();
        };
        assertThrows(IncorrectResultSizeDataAccessException.class, runnable);

        Optional<String> sqlEvent = customSingleEventListener.getSqlEvent();
        if (sqlEvent.isPresent()) {
            log.info("\nexplain {}", sqlEvent.get());
        } else {
            fail();
        }
        Optional<String> sqlEvent2 = customSingleEventListener.getSqlEvent();
        if (sqlEvent2.isPresent()) {
            fail();
        }
    }

    private void executeQuery() {
        homeRepository.findByDisjointFailed(1L, "Васька-2");
    }


}