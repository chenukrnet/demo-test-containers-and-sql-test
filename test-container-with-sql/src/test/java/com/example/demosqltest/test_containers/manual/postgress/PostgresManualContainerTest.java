package com.example.demosqltest.test_containers.manual.postgress;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("no-dll")
@SpringBootTest
@SpringJUnitConfig(initializers = PostgresManualTestContextInitialise.class)
public class PostgresManualContainerTest {

    @Autowired
    private HomeRepository homeRepository;


    @Sql(statements = "create table home_entity (id bigint CONSTRAINT home_entity_pk PRIMARY KEY , name text )")
    @Sql(statements = "insert into home_entity (id, name) values (1, 'Васька');")
    @Sql(statements = "delete from home_entity where id in (1, 2)", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void testFindIt() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);
        assertTrue(byId.isPresent());
        if (byId.isPresent()) {
            assertEquals("Васька", byId.get().getName());
        } else {
            fail();
        }
    }

}
