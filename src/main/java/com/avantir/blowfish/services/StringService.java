package com.avantir.blowfish.services;

import org.springframework.stereotype.Service;

/**
 * Created by lekanomotayo on 04/01/2018.
 */

@Service
public class StringService {

    public String upperCaseFirst(String value) {
        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }


    public boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }

    public String lowerCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toLowerCase(array[0]);
        // Return string.
        return new String(array);
    }

    public String leftPad(String str, int len, char pad) {
        if(str == null)
            return null;
        StringBuilder sb = new StringBuilder();
        while (sb.length() + str.length() < len) {
            sb.append(pad);
        }
        sb.append(str);
        String paddedString = sb.toString();
        return paddedString;
    }

    public String rightPad(String str, int len, char pad) {

        if(str == null)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        while (sb.length() < len) {
            sb.append(pad);
        }
        String paddedString = sb.toString();
        return paddedString;
    }

}
