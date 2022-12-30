package com.carrot.infrastructure.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class Attributes {

    private Map<String, Object> mainAttributes;
    private Map<String, Object> subAttributes;
    private Map<String, Object> otherAttributes;

}
