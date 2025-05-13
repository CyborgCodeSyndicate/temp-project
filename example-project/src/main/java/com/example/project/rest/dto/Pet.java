package com.example.project.rest.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pet {

   private Long id;
   private Category category;
   private String name;
   private List<String> photoUrls;
   private List<Tag> tags;
   private Status status;

}
