package com.ineedhousing.backend.geometry.exceptions;

public class ErroredGeoCodeAPICallException extends RuntimeException{

    public ErroredGeoCodeAPICallException(String message) {
        super(message);
    }

}
