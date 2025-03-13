package com.bakery.project.model.bakery;

import com.bakery.project.ui.elements.Bakery.InputFields;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import lombok.*;

import static com.bakery.project.ui.elements.Bakery.InputFields.Data.PASSWORD_FIELD;
import static com.bakery.project.ui.elements.Bakery.InputFields.Data.USERNAME_FIELD;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Seller {

    private String name;
    private String surname;

    @InsertionElement(locatorClass = InputFields.class, elementEnum = USERNAME_FIELD, order = 1)
    private String email;

    @InsertionElement(locatorClass = InputFields.class, elementEnum = PASSWORD_FIELD, order = 2)
    private String password;

}
