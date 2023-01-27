package com.carrot.support;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class QueryParamUtil {

    public static MultiValueMap<String, String> QueryParam(){
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("size", "20");
        query_param.add("page", "0");
        return query_param;
    }

    public static MultiValueMap<String, String> QueryParam(int size, int page){
        MultiValueMap<String, String> query_param = new LinkedMultiValueMap<>();
        query_param.add("size", String.valueOf(size));
        query_param.add("page", String.valueOf(page));
        return query_param;
    }
}
