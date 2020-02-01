package com.example.demosqltest.spy6;

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.P6Factory;
import com.p6spy.engine.spy.P6LoadableOptions;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.option.P6OptionsRepository;


public class CustomSpy6Factory implements P6Factory {


    @Override
    public P6LoadableOptions getOptions(P6OptionsRepository optionsRepository) {
        return new P6SpyOptions(optionsRepository);
    }

    @Override
    public JdbcEventListener getJdbcEventListener() {

        return new CustomSingleEventListener();
    }

}
