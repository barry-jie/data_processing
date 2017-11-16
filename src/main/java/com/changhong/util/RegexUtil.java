package com.changhong.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    //标点符号
    public static final String puncEx1 = "[(\\p{P}|\\p{S}|\\p{Z})]";
    //标点符号，除 +\-./%符号外
    public static final String puncEx2 = "[(\\p{P}|\\p{S}|\\p{Z})&&[^+\\-./%°?@&℃]]";
    //标点符号，除 空格外
    public static final String puncEx3 = "[(\\p{P}|\\p{S}|\\p{Z})&&[^\\s]]";

    //日文
    public static final String japaneseEx = "[\\u3040-\\u309F\\u30A0-\\u30FF]+";

    //中文
    public static final String chineseEx = "[\\u4e00-\\u9fa5]+";
    public static final Pattern chinesePattern = Pattern.compile(chineseEx);

    //数字
    public static final String digitEx = "[零一二三四五六七八九十0-9ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫIVX]+";
    public static final Pattern digitPattern = Pattern.compile(digitEx);

    //括号
    public static final String bracketEx = "(\\(.*?\\))|(（.*?）)|(\\[.*?\\])|【.*?】";
    public static final Pattern bracketPattern = Pattern.compile(bracketEx);

    //带/的括号
    public static final String bracketAndSlashEx = "(\\(.*?/.*?\\))|(（.*?/.*?）)|(\\[.*?/.*?\\])|【.*?/.*?】";
    public static final Pattern bracketAndSlashPattern = Pattern.compile(bracketAndSlashEx);

    //括号和标点符号
    public static final String bracketAndPuncEx1 = "(\\(.*?\\))|(（.*?）)|(\\[.*?\\])|(【.*?】)|([(\\p{P}|\\p{S}|\\p{Z})])";
    //括号和标点符号，除 +\-./%符号外
    public static final String bracketAndPuncEx2 = "(\\(.*?\\))|(（.*?）)|(\\[.*?\\])|(【.*?】)|([(\\p{P}|\\p{S}|\\p{Z})&&[^℃+\\-./%°?@&]])";
    //匹配表达式：1.必须包含中文或英文且可以包含字母、数字、ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ%./+-°?@&;
    //           2.包含数字%./+-
    private static final String patternRegEx1 = "(^((?=.*[\\u4e00-\\u9fa5a-zA-Z].*))[ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ" +
            "0-9A-Za-z\\u4e00-\\u9fa5\\+\\-./%°?@℃&]+$)|(^[ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫ0-9./%+\\-°?@℃&]+$)";
    public static final Pattern pattern1 = Pattern.compile(patternRegEx1);

    public static final String figureNameRegex = "^((?=.*[\\u4e00-\\u9fa5].*))[\\dA-Za-z\\u4e00-\\u9fa5]+$";
    public static final Pattern figureNamePattern = Pattern.compile(figureNameRegex);


    public static final List<String> special_list = new ArrayList<String>();

    static {
        special_list.add("\\");
        special_list.add("$");
        special_list.add("(");
        special_list.add(")");
        special_list.add("*");
        special_list.add("+");
        special_list.add(".");
        special_list.add("[");
        special_list.add("]");
        special_list.add("^");
        special_list.add("{");
        special_list.add("}");
        special_list.add("|");
        special_list.add("?");
    }

    public static boolean isPhone(String phone) {
        String regex = "1\\d{10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    public static String compile(String keyword) {
        for (String s : special_list) {
            if (keyword.contains(s)) {
                keyword = keyword.replaceAll("\\" + s, "\\\\" + s);
            }
        }
        return keyword;
    }
}
