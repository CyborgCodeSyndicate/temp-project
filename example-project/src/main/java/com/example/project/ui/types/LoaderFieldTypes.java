package com.example.project.ui.types;

import com.theairebellion.zeus.ui.components.loader.LoaderComponentType;

public enum LoaderFieldTypes implements LoaderComponentType {

   MD_LOADER_TYPE;


   public static final String MD_LOADER = "MD_LOADER_TYPE";


   @Override
   public Enum getType() {
      return this;
   }
}
