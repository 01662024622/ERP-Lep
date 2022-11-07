package com.fastwok.crawler.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {
    public static String convetAddress(String string){
        String[] splited = string.split(",");
        String city=splited[splited.length-1].trim();
        splited = city.split("\\s+");
        String code = "";
        for(int i = 0;i<splited.length;i++){
            code=code+splited[i].charAt(0);
        }
        return code;
    }
    public static String formatCode(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        return temp.replaceAll("đ", "d").replaceAll(" ", "").toUpperCase();
    }
}
