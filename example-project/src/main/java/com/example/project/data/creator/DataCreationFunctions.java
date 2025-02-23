package com.example.project.data.creator;

import com.example.project.base.World;
import com.example.project.data.test.TestData;
import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.model.Student;
import com.example.project.rest.dto.Category;
import com.example.project.rest.dto.Pet;
import com.example.project.rest.dto.Status;
import com.example.project.rest.dto.Tag;
import com.theairebellion.zeus.db.service.DatabaseService;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.ui.storage.DataExtractorsUi;

import java.util.List;

public class DataCreationFunctions {


    public static Student createValidStudent() {
        // Long id = storage.get(null, Long.class);
        return Student.builder()
                   .name("John")
                   .surname("Smith")
                   // .id(id)
                   .age(20)
                   .build();
    }


    public static Pet createDog() {
        return Pet.builder()
                   .id(0L)
                   .category(new Category(0L, "Middle size Dogs"))
                   .name("Bulldog")
                   .photoUrls(List.of(
                       "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bf/Bulldog_inglese.jpg/800px-Bulldog_inglese.jpg"))
                   .tags(List.of(new Tag(0L, "Rescued")))
                   .status(Status.AVAILABLE)
                   .build();
    }


    public static Seller createValidSeller() {
        /*SuperQuest superQuest = QuestHolder.get();

        Long id = superQuest.getStorage().sub().get(DataExtractorsUi.responseBodyExtraction("api/authenticate", "$.id"), Long.class);

        if(id != null){

        }*/

        return Seller.builder()
                .email("barista@vaadin.com")
                .password("barista")
                .build();
    }


    public static Order createValidOrder() {
        return Order.builder()
                .customerName("John")
                .customerSurname("Terry")
                .customerDetails("Address")
                .phoneNumber("+1-555-7777")
                .location("Bakery")
                .product("Strawberry Bun")
                .build();
    }


    public static String usernameJohn(){
        return "John";
    }

    public static String passwordJohn(){
        return "pass";
    }

}
