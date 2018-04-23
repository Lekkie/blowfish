package com.avantir.blowfish.utils;

import java.lang.reflect.Field;

/**
 * Created by lekanomotayo on 17/03/2018.
 */
public class TestUtils {

    public static void setField(Object object, String key, Object value) throws Exception{
        Field field = object.getClass().getDeclaredField(key);
        field.setAccessible(true);
        field.set(object, value);
    }
}
