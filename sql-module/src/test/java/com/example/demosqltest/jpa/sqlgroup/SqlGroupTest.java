package com.example.demosqltest.jpa.sqlgroup;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-containers")
@SpringBootTest
@Slf4j
class SqlGroupTest {

    @Autowired
    HomeRepository homeRepository;


    /**
     * До java 8 нельзя было использовать повторяемые анотации,  @SqlGroup - позволяла выполнять набор
     * */
    @SqlGroup(value = {
            @Sql(statements = "insert into home_entity (id, name) values (1, 'Васька');"),
            @Sql(value = "classpath:delete-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
                    config = @SqlConfig(separator = ";", commentPrefix = "//")
            )
    })
    @Test
    public void testFindIt() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            assertEquals("Васька", byId.get().getName());
        }else {
            fail();
        }
    }


}