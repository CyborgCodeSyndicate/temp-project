package com.example.project.model.bakery;

import com.example.project.ui.elements.InputFields;
import com.theairebellion.zeus.ui.annotations.InsertionElement;
import lombok.*;

import static com.example.project.ui.elements.InputFields.Data.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Seller {

    private String name;
    private String surname;
    @InsertionElement(locatorClass = InputFields.class, elementEnum = USERNAME, order = 1)
    private String email;
    @InsertionElement(locatorClass = InputFields.class, elementEnum = PASSWORD, order = 2)
    private String password;
    private int age;

}
