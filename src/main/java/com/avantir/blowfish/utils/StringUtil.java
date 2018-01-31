package com.avantir.blowfish.utils;

/**
 * Created by lekanomotayo on 04/01/2018.
 */
public class StringUtil {

    public static String upperCaseFirst(String value) {
        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase(array[0]);
        // Return string.
        return new String(array);
    }


    public static boolean isEmpty(String str){
        return str == null || str.isEmpty();
    }

    public static String lowerCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toLowerCase(array[0]);
        // Return string.
        return new String(array);
    }


    /*
    public static String leftPadWithSpace(String str, int len){

        return String.format("%1$" + len + "s", str);
    }

    public static String rightPadWithSpace(String str, int len){
        return String.format("%1$-" + len + "s", str);
    }
    */


    public static String leftPad(String str, int len, char pad) {
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

    public static String rightPad(String str, int len, char pad) {

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


    public static void main(String[] args){

        //System.out.println("Right padding: " + rightPadWithSpace("SimSnk",12));
        //System.out.println("Right padding: " + leftPadWithSpace("SimSnk",12));
        System.out.println("Left padding: " + leftPad("SimSnk",12, '0'));
        System.out.println("Right padding: " + rightPad("SimSnk",12, '0'));
    }
}
