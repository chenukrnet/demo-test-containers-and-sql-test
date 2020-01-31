package com.example.demosqltest.jpa.transaction.commit;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import com.example.demosqltest.nondemo.ServiceWithOuterTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collection;

import static com.example.demosqltest.nondemo.ServiceWithInnerTransaction.NESTED_TRANSACTION;
import static com.example.demosqltest.nondemo.ServiceWithOuterTransaction.TRANSACTION;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Sql("crate table home_entity (id int, name text)")
@Sql(statements = "truncate  home_entity")
class ServiceWithOuterTransactionTest {

    @Autowired
    ServiceWithOuterTransaction serviceWithOuterTransaction;
    @Autowired
    HomeRepository homeRepository;
    @Test
    void saveFirst() {
        String namePrefix = "throw-1";
        serviceWithOuterTransaction.saveFirst(namePrefix);
        Collection<HomeEntity> allByName = homeRepository.findAllByName(namePrefix + TRANSACTION);
        assertEquals(1,allByName.size());
        Collection<HomeEntity> nested = homeRepository.findAllByName(namePrefix + NESTED_TRANSACTION);
        assertEquals(0,nested.size());
    }

    @Test
    void saveSecond() {
    }
}