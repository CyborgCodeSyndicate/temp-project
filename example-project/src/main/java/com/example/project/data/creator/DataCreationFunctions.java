package com.example.project.data.creator;

import com.example.project.model.bakery.Order;
import com.example.project.model.bakery.Seller;
import com.example.project.model.Student;
import com.example.project.rest.dto.Category;
import com.example.project.rest.dto.Pet;
import com.example.project.rest.dto.Status;
import com.example.project.rest.dto.Tag;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.ui.storage.DataExtractorsUi;
import org.openqa.selenium.NotFoundException;

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
        return Seller.builder()
                .email("barista@vaadin.com")
                .password("barista")
                .build();
    }


    public static Order createValidOrder() {
        return Order.builder()
                .customerName("John Terry")
                .customerDetails("Address")
                .phoneNumber("+1-555-7777")
                .location("Bakery")
                .product("Strawberry Bun")
                .build();
    }


    public static Order createValidLateOrder() {
        SuperQuest superQuest = QuestHolder.get();
        List<String> productList = superQuest.getStorage().get(DataExtractorsUi
                .responseBodyExtraction("?v-r=uidl",
                        "$..orderCard[?(@.fullName=='John Terry')].items[*].product.name"), List.class);
        if(productList.isEmpty()){
            throw new NotFoundException("There is no product element");
        }

        return Order.builder()
                .customerName("Petar Terry")
                .customerDetails("Address")
                .phoneNumber("+1-222-7778")
                .location("Store")
                .product(productList.get(0))
                .build();
    }


    public static String usernameJohn(){
        return "John";
    }

    public static String passwordJohn(){
        return "pass";
    }

}
