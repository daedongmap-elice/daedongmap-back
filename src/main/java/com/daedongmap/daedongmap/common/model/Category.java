package com.daedongmap.daedongmap.common.model;

public enum Category {
    KOREAN("한식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    STREET_FOOD("분식"),
    TRANSPORT_CAFE("기사식당"),
    SNACK("간식"),
    BUFFET("뷔페");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
