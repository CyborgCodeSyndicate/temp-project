package com.example.project.ui.authentication;

import com.example.project.data.test.TestData;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;
import org.aeonbits.owner.ConfigCache;

public class AdminUI implements LoginCredentials {

    private static final TestData testData = ConfigCache.getOrCreate(TestData.class);


    @Override
    public String username() {
        return testData.username();
    }


    @Override
    public String password() {
        return testData.password();
    }

}
