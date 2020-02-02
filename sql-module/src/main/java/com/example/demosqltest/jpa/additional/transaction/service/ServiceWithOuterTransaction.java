package com.example.demosqltest.jpa.additional.transaction.service;

import com.example.demosqltest.jpa.additional.transaction.entity.HomeEntityAutoGeneratedId;
import com.example.demosqltest.jpa.additional.transaction.repository.HomeWithAutoGeneratedIdRepository;
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
    private final HomeWithAutoGeneratedIdRepository homeWithAutoGeneratedIdRepository;
    private final ServiceWithInnerRequiredNewTransaction serviceWithInnerRequiredNewTransaction;

    @Transactional
    public void excludeDoubleInDatabase(String namePrefix) {
        HomeEntity homeEntity = null;
        try {
            homeEntity = serviceWithInnerRequiredNewTransaction.saveInSeparateTransaction(namePrefix);

        } catch (Exception e) {
            log.error("Ошибка при сохранении внешней транзакции");
        }
        try {
            log.info("Получен айди внейшней сущности:" + homeEntity.getId());
            serviceWithInnerRequiredNewTransaction.nestedThatThrow(namePrefix, homeEntity.getId());
        } catch (Exception e) {
            log.error("Ошибка при сохранении внутренней транзакции", e);
        }
    }

    @Transactional()
    public void deadLock(String namePrefix) {
        HomeEntity homeEntity = null;
        try {
            homeEntity = new HomeEntity();
            homeEntity.setName(namePrefix + TRANSACTION);
            homeEntity.setId(1L);// В случае если нет @GeneratedAuto
            homeRepository.saveAndFlush(homeEntity);


        } catch (Exception e) {
            log.error("Ошибка при сохранении внешней транзакции");
        }
        try {
            log.info("Получен айди внейшней сущности:" + homeEntity.getId());
            serviceWithInnerRequiredNewTransaction.nestedThatThrow(namePrefix, homeEntity.getId());
        } catch (Exception e) {
            log.error("Ошибка при сохранении внутренней транзакции", e);
        }
    }


    @Transactional
    public void falsePositive(String namePrefix) {
        HomeEntityAutoGeneratedId homeEntity = new HomeEntityAutoGeneratedId();
        homeEntity.setName(namePrefix + TRANSACTION);
        try {
            homeWithAutoGeneratedIdRepository.saveAndFlush(homeEntity);
        } catch (Exception e) {
            log.error("Ошибка при сохранении внешней транзакции");
        }
        try {
            log.info("Получен айди внейшней сущности:" + homeEntity.getId());
            serviceWithInnerRequiredNewTransaction.nestedTransactionWithAutoGenerated(namePrefix, homeEntity.getId());
        } catch (Exception e) {
            log.error("Ошибка при сохранении внутренней транзакции",e);
        }
    }


    @Transactional
    public void saveFirstWithoutTryCatch(String namePrefix) {
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setName(namePrefix + "transaction");
        homeRepository.save(homeEntity);
        serviceWithInnerRequiredNewTransaction.nestedThatThrow(namePrefix, homeEntity.getId());
    }
}
