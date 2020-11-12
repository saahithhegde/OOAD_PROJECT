package com.cash4books.cash4books.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper(){
        if(objectMapper==null){
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

}
