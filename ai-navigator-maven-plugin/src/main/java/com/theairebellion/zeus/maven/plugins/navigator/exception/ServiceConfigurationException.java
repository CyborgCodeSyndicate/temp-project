package com.theairebellion.zeus.maven.plugins.navigator.exception;

public class ServiceConfigurationException extends RuntimeException {

    public ServiceConfigurationException(String message) {
        super(message);
    }


    public ServiceConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
