package com.bakery.project.model.bakery;

import com.bakery.project.ui.elements.bakery.InputFields;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.bakery.project.ui.elements.bakery.InputFields.Data.PASSWORD_FIELD;
import static com.bakery.project.ui.elements.bakery.InputFields.Data.USERNAME_FIELD;

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
