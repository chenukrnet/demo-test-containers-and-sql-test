package com.example.demosqltest.jpa.additional.transaction.commit;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import com.example.demosqltest.spy6.CustomSingleEventListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
@ActiveProfiles("test-containers")
@Transactional// показать как ведет себя приложения если выключить
@Commit  //@Rollback(false)
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommitTest {

    @Autowired
    HomeRepository homeRepository;

    @Autowired
    CustomSingleEventListener customSingleEventListener;

    // TODO: 01.02.2020 fix it
    @Order(1)
    @Test
    public void successSaving() {
        HomeEntity entity = new HomeEntity();
        entity.setId(1L);
        entity.setName("Васька");
        homeRepository.saveAndFlush(entity);
    }

    @Order(2)
    @Test
    public void getDataSavingInOtherTest() {
        Optional<HomeEntity> byId = homeRepository.findById(1L);
        if (byId.isPresent()) {
            HomeEntity entity = byId.get();
            assertEquals("Васька", entity.getName());
        } else {
            fail();
        }
    }


}