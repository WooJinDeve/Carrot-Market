package com.carrot.application.post.domain;

import lombok.Getter;

@Getter
public enum PostStatue {

    SALE(1, "판매중"), BOOKED(2, "예약"), SOLD_OUT(3, "판매완료");

    private final int idx;
    private final String state;
    PostStatue(int idx, String state) {
        this.idx = idx;
        this.state = state;
    }
}
