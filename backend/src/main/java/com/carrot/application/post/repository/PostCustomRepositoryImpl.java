package com.carrot.application.post.repository;

import com.carrot.application.post.domain.Category;
import com.carrot.application.post.domain.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static com.carrot.application.post.domain.entity.QPost.post;
import static com.carrot.application.region.domain.QRegion.region;
import static com.carrot.infrastructure.support.RepositorySupport.*;

@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Post> findWithSearchConditions(List<Long> regionIds, Category category,
                                                String title, Pageable pageable) {
        final JPAQuery<Post> jpaQuery = queryFactory.selectFrom(post)
                .where(
                        toInsExpression(post.id, regionIds),
                        toEqExpression(post.category, category),
                        toContainsExpression(post.content.title, title),
                        post.deletedAt.isNotNull()
                )
                .innerJoin(post.region, region)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(post.id.desc());

        return toSlice(pageable, jpaQuery.fetch());
    }
}
