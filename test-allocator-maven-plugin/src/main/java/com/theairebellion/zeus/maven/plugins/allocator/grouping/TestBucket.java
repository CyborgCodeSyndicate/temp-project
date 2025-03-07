package com.theairebellion.zeus.maven.plugins.allocator.grouping;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TestBucket {

    private final List<String> classNames;
    private final int totalMethods;

}
