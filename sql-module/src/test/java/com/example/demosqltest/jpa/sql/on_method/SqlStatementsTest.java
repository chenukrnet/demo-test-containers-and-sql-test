package com.example.demosqltest.jpa.sql.on_method;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-containers")
@SpringBootTest
@Slf4j
class SqlStatementsTest {

    @Autowired
    HomeRepository homeRepository;


    /**
     * @Sql(statements - позволяет выполнить выражение перед тестом
     */
    @Sql(statements = "insert into home_entity (id, name) values (1, 'Васька');")
    @Test
    public void findIt() {

        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            assertEquals("Васька", byId.get().getName());
        } else {
            fail();
        }


    }

    @Sql(statements = "delete from home_entity  where (id, name) = (1, 'Васька');")
    @Sql(statements = "insert into home_entity (id, name) values (1, 'Васька');")
    @Sql(statements = "delete from home_entity  where (id, name) = (1, 'Васька');", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void removeIt() {

        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            homeRepository.delete(byId.get());

        } else {
            fail();
        }
        homeRepository.flush();
        Optional<HomeEntity> secondSearch = homeRepository.findById(1L);
        assertFalse(secondSearch.isPresent());

    }


    @Test
    public void unSuccessOrderByMethodName() {

        Optional<HomeEntity> byId = homeRepository.findById(1L);
        if (byId.isPresent()) {
            fail();
            homeRepository.delete(byId.get());
        } else {
            assertTrue(byId.isEmpty());
        }
        homeRepository.flush();
        Optional<HomeEntity> secondSearch = homeRepository.findById(1L);
        assertFalse(secondSearch.isPresent());

    }

}