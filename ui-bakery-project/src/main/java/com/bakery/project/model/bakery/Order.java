package com.bakery.project.model.bakery;

import com.bakery.project.ui.elements.bakery.InputFields;
import com.bakery.project.ui.elements.bakery.SelectFields;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.bakery.project.ui.elements.bakery.InputFields.Data.CUSTOMER_FIELD;
import static com.bakery.project.ui.elements.bakery.InputFields.Data.DETAILS_FIELD;
import static com.bakery.project.ui.elements.bakery.InputFields.Data.NUMBER_FIELD;
import static com.bakery.project.ui.elements.bakery.SelectFields.Data.LOCATION_DDL;
import static com.bakery.project.ui.elements.bakery.SelectFields.Data.PRODUCTS_DDL;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {

   private int id;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = CUSTOMER_FIELD, order = 1)
   private String customerName;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = DETAILS_FIELD, order = 2)
   private String customerDetails;

   @InsertionElement(locatorClass = InputFields.class, elementEnum = NUMBER_FIELD, order = 3)
   private String phoneNumber;

   @InsertionElement(locatorClass = SelectFields.class, elementEnum = LOCATION_DDL, order = 4)
   private String location;

   @InsertionElement(locatorClass = SelectFields.class, elementEnum = PRODUCTS_DDL, order = 5)
   private String product;

   private String productDetails;
   private String productCount;
   private String time;
   private String date;

}
