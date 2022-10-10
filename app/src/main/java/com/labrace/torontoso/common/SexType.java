package com.labrace.torontoso.common;

/**
 * Created by Dung on 5/5/2016.
 */
public enum SexType {
    Male("M"), Female("F");

    private String value;

    SexType(String value) {
        this.value = value;
    }

    public static SexType fromCharacter(String sex) {
        switch(sex) {
            case "M":
                return Male;
            case "F":
                return Female;
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
