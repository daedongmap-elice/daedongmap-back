package com.daedongmap.daedongmap.common.model;

public enum Region {
    GANGNAM("강남"),
    GANGDONG("강동"),
    GANGBOOK("강북"),
    GANGSEO("강서"),
    GURO("구로"),
    NOWON("노원"),
    DOBONG("도봉");


    private final String value;

    Region(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
