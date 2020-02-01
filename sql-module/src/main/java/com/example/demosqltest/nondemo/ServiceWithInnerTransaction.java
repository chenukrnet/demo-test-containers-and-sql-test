package com.example.demosqltest.nondemo;

import com.example.demosqltest.jpa.entity.HomeEntity;
import com.example.demosqltest.jpa.repositories.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceWithInnerTransaction {

    public static final String NESTED_TRANSACTION = "nested-transaction";
    private final HomeRepository homeRepository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void nestedThatThrow(String name, Long id) {
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setId(id);
        homeEntity.setName(name + NESTED_TRANSACTION);
        homeEntity.setIsNew(true);
        homeRepository.saveAndFlush(homeEntity);

    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void nestedThatNotThrow(String name) {
        HomeEntity homeEntity = new HomeEntity();
        homeEntity.setName(name + NESTED_TRANSACTION);
        homeRepository.saveAndFlush(homeEntity);
    }
}
