package com.example.demosqltest.nondemo;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceWithOuterTransaction {

    public static final String TRANSACTION = "transaction";
    private final HomeRepository homeRepository;
    private final ServiceWithInnerTransaction serviceWithInnerTransaction;
    @Transactional
    public void saveFirst(String namePrefix){
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setName(namePrefix+ TRANSACTION);
        try {
            homeRepository.saveAndFlush(homeEntity);
        } catch (Exception e) {
            log.error("Ошибка при сохранении внешней транзакции");
        }
        try {
            log.info("Получен айди внейшней сущности:"+homeEntity.getId());
            serviceWithInnerTransaction.nestedThatThrow(namePrefix,homeEntity.getId());
        } catch (Exception e) {
            log.error("Ошибка при сохранении внутренней транзакции");
        }
    }

    @Transactional
    public void saveSecond(String namePrefix){
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setName(namePrefix+"transaction");
        homeRepository.save(homeEntity);
        serviceWithInnerTransaction.nestedThatThrow(namePrefix, homeEntity.getId());
    }
}
