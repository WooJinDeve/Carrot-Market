package com.carrot.global.convert;

import com.carrot.application.post.domain.Category;
import org.springframework.core.convert.converter.Converter;

public class StringToCategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String categoryName) {
        return Category.findByCategoryName(categoryName);
    }
}
