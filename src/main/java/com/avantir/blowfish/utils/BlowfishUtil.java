package com.avantir.blowfish.utils;

import com.avantir.blowfish.model.Bin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lekanomotayo on 12/01/2018.
 */
public class BlowfishUtil {



    public static boolean validatePan(String pan) {
        if(pan == null)
            return false;

        try{
            int sum = 0;
            boolean alternate = false;
            for (int i = pan.length() - 1; i >= 0; i--) {
                int n = Integer.parseInt(pan.substring(i, i + 1));
                if (alternate) {
                    n *= 2;
                    if (n > 9) {
                        n = (n % 10) + 1;
                    }
                }
                sum += n;
                alternate = !alternate;
            }
            return (sum % 10 == 0);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean validateExpDate(String expDate){
        if(expDate == null)
            return false;
        try{
            String expYear = expDate.substring(0, 2);
            int expMonth = Integer.parseInt(expDate.substring(2, 4));
            Calendar cal = Calendar.getInstance();
            int thisMonth = cal.get(Calendar.MONTH) + 1;
            int thisYear = cal.get(Calendar.YEAR);
            String thisYearStrPrefix = String.valueOf(thisYear).substring(0, 2);
            String fullExpYear = thisYearStrPrefix + expYear;
            if(Integer.parseInt(fullExpYear) > thisYear)
                return true;

            if(Integer.parseInt(fullExpYear) == thisYear && thisMonth < expMonth)
                return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public static Bin getBinFromPan(List<Bin> binList, String pan){

        List<Bin> matchedBinList = new ArrayList<Bin>();
        for(Bin bin: binList){
            String binStr = bin.getCode();
            if(pan.startsWith(binStr))
                matchedBinList.add(bin);
        }
        if(matchedBinList.size() > 1){
            Bin maxLenBin = matchedBinList.get(0);
            int maxLenBinLen = maxLenBin.getCode().length();
            for(Bin bin : matchedBinList){
                if(maxLenBinLen < bin.getCode().length())
                    maxLenBin = bin;
            }
            return maxLenBin;
        }
        else if(matchedBinList.size() == 1){
            return matchedBinList.get(0);
        }
        return null;
    }


    public static String maskPan(String strText, int start, int end, char maskChar) {

        if(strText == null || strText.equals(""))
            return "";

        if(start < 0)
            start = 0;

        if( end > strText.length() )
            end = strText.length();

        //if(start > end)
        //    return strText;

        int maskLength = strText.length() - end - start;

        if(maskLength <= 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder();
        //StringBuilder sbMaskString = new StringBuilder(maskLength);

        for(int i = 0; i < maskLength; i++){
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }


    public static String getSHA512(String data, String salt){
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return hash;
    }


}
