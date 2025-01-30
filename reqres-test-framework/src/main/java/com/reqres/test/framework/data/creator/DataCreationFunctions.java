package com.reqres.test.framework.data.creator;

import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.GetUsersResponse;
import com.reqres.test.framework.rest.dto.response.UserResponse;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import io.restassured.response.Response;
import manifold.ext.rt.api.Jailbreak;

import java.util.Optional;

import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;

public class DataCreationFunctions {

    public static User createLeaderUser() {
        return User.builder()
                .name("Morpheus")
                .job("Leader")
                .build();
    }

    public static LoginUser createAdminLoginUser() {
        return LoginUser.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();
    }

    public static User createSuffixUser() {
        @Jailbreak Quest quest = QuestHolder.get();

        UserResponse userResponse = Optional.ofNullable(
                        quest.getStorage()
                                .sub(StorageKeysApi.API)
                                .get(GET_ALL_USERS, Response.class)
                                .getBody()
                                .as(GetUsersResponse.class)
                                .getData())
                .filter(users -> !users.isEmpty())
                .map(users -> users.get(0))
                .orElseThrow(() -> new RuntimeException("No users found in the response"));

        return User.builder()
                .name(userResponse.getFirstName() + "suffix")
                .job(userResponse.getLastName() + " worker")
                .build();
    }
}
