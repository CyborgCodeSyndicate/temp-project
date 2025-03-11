package com.example.project.model.bakery;

import com.example.project.ui.elements.Bakery.InputFields;
import com.example.project.ui.elements.Bakery.SelectFields;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import lombok.*;

import static com.example.project.ui.elements.Bakery.InputFields.Data.*;
import static com.example.project.ui.elements.Bakery.SelectFields.Data.LOCATION_DDL;
import static com.example.project.ui.elements.Bakery.SelectFields.Data.PRODUCTS_DDL;

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
