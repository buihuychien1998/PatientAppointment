package com.labrace.torontoso.common;

/**
 * Created by Dung on 5/5/2016.
 */
public enum RaceType {
    Asian("A"), Black("B"), Caucasian("C"), Hispanic("H"), Other("O");

    private String value;

    RaceType(String value) {
        this.value = value;
    }

    public static RaceType fromCharacter(String race) {
        switch(race) {
            case "A":
                return Asian;
            case "B":
                return Black;
            case "C":
                return Caucasian;
            case "H":
                return Hispanic;
            case "O":
                return Other;
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
