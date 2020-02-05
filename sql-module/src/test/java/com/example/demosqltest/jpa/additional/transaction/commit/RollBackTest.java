package com.example.demosqltest.jpa.additional.transaction.commit;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import com.example.demosqltest.spy6.CustomSingleEventListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@ActiveProfiles("test-containers")
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RollBackTest {

    @Autowired
    HomeRepository homeRepository;

    @Autowired
    CustomSingleEventListener customSingleEventListener;

    // TODO: 01.02.2020 fix it
    @Order(1)
    @Test
    @Transactional// Обязательное указание
    @Rollback(true)
    public void successSaving() {
        HomeEntity entity = new HomeEntity();
        entity.setId(1L);
        entity.setName("Васька");
        homeRepository.saveAndFlush(entity);
    }

    @Disabled("Рассказать почему")
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