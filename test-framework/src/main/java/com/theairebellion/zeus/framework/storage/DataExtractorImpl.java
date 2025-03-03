package com.theairebellion.zeus.framework.storage;

import java.util.function.Function;

public class DataExtractorImpl<T> implements DataExtractor<T> {

    private Enum<?> subKey;
    private final Enum<?> key;
    private final Function<Object, T> extractionLogic;


    public DataExtractorImpl(Enum<?> subKey,
                             Enum<?> key,
                             Function<Object, T> extractionLogic) {
        this.subKey = subKey;
        this.key = key;
        this.extractionLogic = extractionLogic;
    }


    public DataExtractorImpl(
        Enum<?> key,
        Function<Object, T> extractionLogic) {
        this.key = key;
        this.extractionLogic = extractionLogic;
    }


    @Override
    public Enum<?> getSubKey() {
        return subKey;
    }


    @Override
    public Enum<?> getKey() {
        return key;
    }


    @Override
    public T extract(Object rawObject) {
        return extractionLogic.apply(rawObject);
    }

}
