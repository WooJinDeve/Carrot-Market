package com.carrot.global.convert;

import com.carrot.application.post.domain.Category;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToCategoryConverter implements Converter<String, Category>,
        com.fasterxml.jackson.databind.util.Converter<String, Category> {

    @Override
    public Category convert(String categoryName) {
        return Category.findByCategoryName(categoryName);
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Category.class);
    }
}
