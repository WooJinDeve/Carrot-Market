package com.carrot.support.fixture;

import com.carrot.application.post.domain.entity.BookedHistory;
import com.carrot.application.post.domain.entity.Post;
import com.carrot.application.user.domain.User;

public class BookedHistoryFixture {

    public static BookedHistory get(User seller, Post post){
        return BookedHistory.builder()
                .seller(seller)
                .post(post)
                .build();
    }

    public static BookedHistory get(User seller, User booker, Post post){
        return BookedHistory.builder()
                .seller(seller)
                .booker(booker)
                .post(post)
                .build();
    }
}
