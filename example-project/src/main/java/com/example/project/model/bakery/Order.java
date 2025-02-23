package com.example.project.model.bakery;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {

    private String customerName;
    private String customerSurname;
    private String customerDetails;
    private String phoneNumber;
    private String product;
    private String productDetails;
    private String productCount;
    private String location;
    private String time;
    private String date;

}
