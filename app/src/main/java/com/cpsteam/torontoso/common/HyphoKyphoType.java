package com.cpsteam.torontoso.common;

/**
 * Created by Dung on 5/5/2016.
 */
public enum HyphoKyphoType {
    None(0), Mild(-1), Moderate(-2), Severe(-3), Kyphotic(1);

    private int value;

    HyphoKyphoType(int value) {
        this.value = value;
    }

    public static HyphoKyphoType fromInteger(int x) {
        switch(x) {
            case 0:
                return None;
            case -1:
                return Mild;
            case -2:
                return Moderate;
            case -3:
                return Severe;
            case 1:
                return Kyphotic;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
