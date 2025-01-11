package com.theairebellion.zeus.logging;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.concurrent.ConcurrentHashMap;

public class LogZeus {

    private static final ConcurrentHashMap<String, Marker> MARKERS = new ConcurrentHashMap<>();


    private LogZeus() {
    }


    public static Marker registerMarker(String markerName) {
        return MARKERS.computeIfAbsent(markerName, MarkerManager::getMarker);
    }


    public static Marker getMarker(String markerName) {
        return MARKERS.get(markerName);
    }


    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }


    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }

}