package com.cpsteam.torontoso.common;

/**
 * Created by Dung on 5/5/2016.
 */
public enum ServiceType {
    Scoliosis(31), TLSO(42);

    private int value;

    ServiceType(int value) {
        this.value = value;
    }

    public static ServiceType fromInteger(int x) {
        switch(x) {
            case 31:
                return Scoliosis;
            case 42:
                return TLSO;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
