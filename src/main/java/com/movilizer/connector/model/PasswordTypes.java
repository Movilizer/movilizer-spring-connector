package com.movilizer.connector.model;


/**
 * Created by Roberto on 03/06/2015.
 */
public enum PasswordTypes {
    DONT_CHANGE_PASSWORD(-1),
    DISABLE_PASSWORD(0),
    PLAIN_TEXT_PASSWORD(1),
    MD5_PASSWORD(2),
    SHA_256_PASSWORD(3),
    SHA_512_PASSWORD(4);

    private final Integer passwordType;

    PasswordTypes(Integer passwordType) {
        this.passwordType = passwordType;
    }

    public Integer getValue() {
        return passwordType;
    }

}
