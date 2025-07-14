package com.reqres.test.framework.data.creator;


import com.reqres.test.framework.rest.dto.request.LoginUser;
import com.reqres.test.framework.rest.dto.request.User;
import com.reqres.test.framework.rest.dto.response.DataResponse;
import com.reqres.test.framework.rest.dto.response.GetUsersResponse;
import com.theairebellion.zeus.api.storage.StorageKeysApi;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.StorageKeysTest;
import io.restassured.response.Response;

import static com.reqres.test.framework.base.World.OLYMPYS;
import static com.reqres.test.framework.data.creator.TestDataCreator.USER_LEADER_FLOW;
import static com.reqres.test.framework.rest.Endpoints.GET_ALL_USERS;
import static com.reqres.test.framework.utils.QueryParams.PAGE_PARAM;
import static com.reqres.test.framework.utils.TestConstants.Pagination.PAGE_TWO;

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

   public static User createJuniorUser() {
      SuperQuest quest = QuestHolder.get();
      DataResponse dataResponse;

      try {
         dataResponse = extractFirstUserFromGetAllUsers(quest);
      } catch (Exception ex) {
         quest.enters(OLYMPYS)
               .request(GET_ALL_USERS.withQueryParam(PAGE_PARAM, PAGE_TWO));
         dataResponse = extractFirstUserFromGetAllUsers(quest);
      }

      return User.builder()
            .name(dataResponse.getFirstName() + " suffix")
            .job("Junior " + dataResponse.getLastName() + " worker")
            .build();
   }

   public static User createSeniorUser() {
      SuperQuest quest = QuestHolder.get();

      User userLeader;
      try {
         userLeader = quest.getStorage()
               .sub(StorageKeysTest.ARGUMENTS)
               .get(USER_LEADER_FLOW, User.class);
      } catch (Exception ex) {
         userLeader = createLeaderUser();
      }

      return User.builder()
            .name("Mr. " + userLeader.getName())
            .job("Senior " + userLeader.getJob())
            .build();
   }

   public static User createIntermediateUser() {
      SuperQuest quest = QuestHolder.get();

      User userLeader;
      try {
         userLeader = quest.getStorage()
               .sub(StorageKeysTest.PRE_ARGUMENTS)
               .get(USER_LEADER_FLOW, User.class);
      } catch (Exception ex) {
         userLeader = createLeaderUser();
      }

      return User.builder()
            .name("Mr. " + userLeader.getName())
            .job("Intermediate " + userLeader.getJob())
            .build();
   }

   private static DataResponse extractFirstUserFromGetAllUsers(SuperQuest quest) {
      return quest.getStorage()
            .sub(StorageKeysApi.API)
            .get(GET_ALL_USERS, Response.class)
            .getBody()
            .as(GetUsersResponse.class)
            .getData()
            .get(0);
   }

}
