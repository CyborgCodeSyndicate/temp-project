package com.reqres.test.framework.service;

import com.theairebellion.zeus.framework.annotation.WorldName;
import com.theairebellion.zeus.framework.chain.FluentService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@WorldName("Rivendell")
@Service
@Scope
public class CustomService extends FluentService {

    public CustomService somethingCustom() {
        return this;
    }
}
