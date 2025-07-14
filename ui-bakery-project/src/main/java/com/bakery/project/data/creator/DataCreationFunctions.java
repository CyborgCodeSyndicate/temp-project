package com.bakery.project.data.creator;

import com.bakery.project.data.extractions.CustomDataExtractor;
import com.bakery.project.model.bakery.Order;
import com.bakery.project.model.bakery.Seller;
import com.theairebellion.zeus.framework.quest.QuestHolder;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import java.util.List;
import org.openqa.selenium.NotFoundException;

public class DataCreationFunctions {


   public static Seller createValidSeller() {
      return Seller.builder()
            .email("barista@vaadin.com")
            .password("barista")
            .build();
   }


   public static Order createValidOrder() {
      return Order.builder()
            .id(1)
            .customerName("John Terry")
            .customerDetails("Address")
            .phoneNumber("+1-555-7777")
            .location("Bakery")
            .product("Strawberry Bun")
            .build();
   }


   public static Order createValidLateOrder() {
      SuperQuest superQuest = QuestHolder.get();
      List<String> productList = superQuest.getStorage().get(CustomDataExtractor
                  .responseBodyExtraction("?v-r=uidl",
                        "$..orderCard[?(@.fullName=='John Terry')].items[*].product.name", "for(;;);"),
            List.class);
      if (productList.isEmpty()) {
         throw new NotFoundException("There is no product element");
      }

      return Order.builder()
            .id(2)
            .customerName("Petar Terry")
            .customerDetails("Address")
            .phoneNumber("+1-222-7778")
            .location("Store")
            .product(productList.get(0))
            .build();
   }

}
