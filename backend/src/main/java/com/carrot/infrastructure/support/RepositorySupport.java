package com.carrot.infrastructure.support;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.util.List;

public class RepositorySupport {

    public static <T> BooleanExpression toInsExpression(final SimpleExpression<T> simpleExpression, final List<T> contained) {
        if (contained == null) {
            return null;
        }
        return simpleExpression.in(contained);
    }


    public static BooleanExpression toContainsExpression(final StringPath stringPath, final String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return stringPath.contains(keyword);
    }

    public static <T> BooleanExpression toEqExpression(final SimpleExpression<T> simpleExpression, final T compared) {
        if (compared == null) {
            return null;
        }
        return simpleExpression.eq(compared);
    }

    public static <T> Slice<T> toSlice(final Pageable pageable, final List<T> items) {
        if (items.size() > pageable.getPageSize()) {
            items.remove(items.size() - 1);
            return new SliceImpl<>(items, pageable, true);
        }
        return new SliceImpl<>(items, pageable, false);
    }
}
