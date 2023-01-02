package com.carrot.application.post.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    DIGITAL("디지털기기"),
    APPLIANCES("생활가전"),
    FURNITURE("가구/인테리어"),
    KITCHEN("생활/주방"),
    CHILDREN("유아동"),
    BOOK("도서"),
    CLOTHES("의류"),
    SPORTS("스포츠/레저"),
    TICKET("티켓/교환권"),;

    private final String name;
    Category(String name) {
        this.name = name;
    }

    public static Category findByCategoryName(String name){
        return Arrays.stream(values())
                .filter(c -> c.hasName(name))
                .findAny()
                .orElse(null);
    }

    private boolean hasName(String name){
        return this.name.equals(name);
    }
}
