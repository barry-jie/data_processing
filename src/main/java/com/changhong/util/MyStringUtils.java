package com.changhong.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arangodb.entity.BaseDocument;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tangjuan on 2017/6/20.
 */
public class MyStringUtils {


    /**
     * 先split再remove
     * 将每个string参数中的括号及括号中的内容删除，然后将string以“/”分割得到多个子字符串，返回所有子字符串组成的数组
     *
     * @param strings
     * @return
     */
    public static Set<String> addStrings2HashSet(String splitOperatorRegEx, String removeRegEx, boolean toUpperCase,
                                                 boolean toLowerCase, String... strings) {
        Set<String> strSet = new HashSet<String>();

        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            if (MyStringUtils.isNotBlank2(str)) {
                if (splitOperatorRegEx != null && splitOperatorRegEx.length() > 0) {
                    String[] array = str.split(splitOperatorRegEx);
                    for (String s : array) {
                        if (removeRegEx != null && !removeRegEx.equals("")) {
                            s = s.replaceAll(removeRegEx, "");
                        }
                        if (MyStringUtils.isNotBlank2(s)) {
                            strSet.add(s.trim());
                        }
                    }
                } else {
                    if (removeRegEx != null) {
                        str = str.replaceAll(removeRegEx, "");
                    }
                    if (MyStringUtils.isNotBlank2(str)) {
                        strSet.add(str.trim());
                    }
                }
            }
        }
        return strSet.isEmpty() ? null : strSet;
    }

    /**
     * 先remove再split
     * @param splitOperatorRegEx
     * @param removeRegEx
     * @param strings
     * @return
     */
    public static Set<String> addStrings2HashSet2(String splitOperatorRegEx, String removeRegEx, Boolean toUpperCase,
                                                  Boolean toLowerCase, String... strings) {
        Set<String> strSet = new HashSet<String>();

        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            if (MyStringUtils.isNotBlank2(str)) {
                if (removeRegEx != null && !removeRegEx.equals("")) {
                    str = str.replaceAll(removeRegEx, "").trim();
                }
                if (splitOperatorRegEx != null && splitOperatorRegEx.length() > 0) {
                    String[] array = str.split(splitOperatorRegEx);
                    for (String s : array) {
                        if (MyStringUtils.isNotBlank2(s)) {
                            if (toUpperCase != null && toUpperCase) {
                                s = s.toUpperCase();
                            } else if (toLowerCase != null && toLowerCase) {
                                s = s.toLowerCase();
                            }
                            strSet.add(s.trim());
                        }
                    }
                } else {
                    if (MyStringUtils.isNotBlank2(str)) {
                        if (toUpperCase != null && toUpperCase) {
                            str = str.toUpperCase();
                        } else if (toLowerCase != null && toLowerCase) {
                            str = str.toLowerCase();
                        }
                        strSet.add(str.trim());
                    }
                }
            }
        }
        return strSet.isEmpty() ? null : strSet;
    }

    /**
     * 去掉句首和句尾的/
     *
     * @param str
     * @return
     */
    public static String trimBackSlash(String str) {
        String s = null;
        if (MyStringUtils.isNotBlank2(str)) {
            s = str.replaceAll("(^/*)|(/*$)", "");
        }
        return s;
    }

    /**
     * 对日期进行格式化
     *
     * @param originBirthday
     * @return
     */
    public static String formatDateStr(String originBirthday) {
        String birthday = null;

        String regEx1 = "\\d{4}[年\\-—/\\.]\\d{1,2}[月\\-—/\\.]\\d{1,2}[日号]?";
        String regEx2 = "\\d{4}[年\\-—/\\.]\\d{1,2}月?";
        String regEx3 = "\\d{1,2}[月\\-—/\\.]\\d{1,2}[日号]?";
        String regEx4 = "\\d{4}年?";

        try {
            if (MyStringUtils.isNotBlank2(originBirthday)) {
                String replaceRegEx = "(\\(.*\\))|(\\（.*\\）)|(\\(.*\\）)|(\\（.*\\))|(\\[.*\\])|(\\s)|(\\u00A0)";
                originBirthday = originBirthday.replaceAll(replaceRegEx, "").trim();
                originBirthday = originBirthday.trim();

                Pattern p1 = Pattern.compile(regEx1);
                Matcher m1 = p1.matcher(originBirthday);
                if (m1.find()) {
                    originBirthday = m1.group();
                    originBirthday = originBirthday.replaceAll("[年月\\-—/\\.]", "-").replaceAll("[日号]", "");
                    birthday = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(originBirthday));
                    return birthday;
                }

                Pattern p2 = Pattern.compile(regEx2);
                Matcher m2 = p2.matcher(originBirthday);
                if (m2.find()) {
                    originBirthday = m2.group();
                    originBirthday = originBirthday.replaceAll("[年\\-—/\\.]", "-").replaceAll("月", "");
                    birthday = new SimpleDateFormat("yyyy-MM").format(new SimpleDateFormat("yyyy-MM").parse(originBirthday));
                    return birthday;
                }

                Pattern p3 = Pattern.compile(regEx3);
                Matcher m3 = p3.matcher(originBirthday);
                if (m3.find()) {
                    originBirthday = m3.group();
                    originBirthday = originBirthday.replaceAll("[月\\-—/\\.]", "-").replaceAll("[日号]", "");
                    birthday = new SimpleDateFormat("MM-dd").format(new SimpleDateFormat("MM-dd").parse(originBirthday));
                    return birthday;
                }

                Pattern p4 = Pattern.compile((regEx4));
                Matcher m4 = p4.matcher(originBirthday);
                if (m4.find()) {
                    originBirthday = m4.group();
                    birthday = originBirthday.replaceAll("年", "");
                    return birthday;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return birthday;
    }


    public static boolean isNotBlank2(Object str) {
        if (str != null) {
            String strs = str.toString().trim();
            if (!"".equals(strs) && !"null".equalsIgnoreCase(strs) && !"未知".equals(strs) && !"不详".equals(strs)
                    && !"无".equals(strs) && !"其他".equals(strs) && !"其它".equals(strs) &&!"N/A".equalsIgnoreCase(strs)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotBlank(Object str) {
        if (str != null) {
            String strs = str.toString().trim();
            if (!"".equals(strs) && !"null".equals(strs) && !"N/A".equalsIgnoreCase(strs)) {
                return true;
            }
        }
        return false;
    }

    public static Integer parseInteger(Object str) {
        Integer in = null;

        try {
            if (str != null && MyStringUtils.isNotBlank2(str)) {
                in = Integer.parseInt(str.toString().trim());
            }
        } catch (NumberFormatException e) {
        }

        return in;
    }

    public static Double parseDouble(Object str) {

        Double d = null;

        if (MyStringUtils.isNotBlank2(str)) {
            try {
                d = Double.parseDouble(str.toString());
            } catch (NumberFormatException e) {
            }
        }

        return d;
    }

    public static String stringConcat(String... strArray) {
        StringBuilder sb = new StringBuilder();

        boolean allAreNull = true;

        for (String str : strArray) {
            if (str != null) {
                allAreNull = false;
                sb.append(str);
            }
        }

        if (allAreNull == true) {
            sb = null;
        }

        return sb == null ? null : sb.toString();
    }

    /**
     * 去除上标
     *
     * @param originStr
     * @return
     */
    public static String removeByRegEx(String originStr, String removeRegeEx) {
        String str = null;
        if (originStr != null) {
            str = originStr.replaceAll(removeRegeEx, "").trim();
        }
        return str;
    }


    public static Document removeNullValue(Document document) {
        Document newDoc = new Document();
        Set<String> keySet = document.keySet();
        for (String key : keySet) {
            Object value = document.get(key);
            if (value != null) {
                newDoc.put(key, value);
            }
        }

        return newDoc;
    }

    public static <T> T removeNbsp(Object originDoc, Class<T> clazz) {
        String objStr = JSON.toJSONString(originDoc);
        if (MyStringUtils.isNotBlank2(objStr)) {
            objStr = objStr.replaceAll("(&nbsp;)|(\\\\u00A0)", " ")
                    .replaceAll("[\\s\\u00A0]+", " ")
                    .replaceAll("(\\[\\d+\\])|(【\\d+】)", "");
            return JSONObject.parseObject(objStr, clazz);
        }
        return null;
    }

    public static Document removeNbsp(Document originDoc) {
        if (originDoc != null) {
            String objStr = JSON.toJSONString(originDoc);
            if (MyStringUtils.isNotBlank2(objStr)) {
                objStr = objStr.replaceAll("(&nbsp;)|(\\\\u00A0)", " ")
                        .replaceAll("[\\s\\u00A0]+", " ")
                        .replaceAll("(\\[\\d+\\])|(【\\d+】)", "");
            }
            originDoc = Document.parse(objStr);
        }
        return originDoc;
    }

    public static String removeNbsp(String jsonStr) {
        if (MyStringUtils.isNotBlank2(jsonStr)) {
            jsonStr = jsonStr.replaceAll("(&nbsp;)|(\\\\u00A0)", " ")
                    .replaceAll("[\\s\\u00A0]+", " ")
                    .replaceAll("(\\[\\d+\\])|(【\\d+】)", "");
        }
        return jsonStr;
    }

    /**
     * 字符串中的罗马数字转为阿拉伯数字
     * @param str
     * @return
     */
    public static String romanDigit2Arab(String str) {

        String[] romanDigitRegexArray = {"[ⅰⅠ]", "[ⅱⅡ]", "(iii)|(III)|[ⅲⅢ]", "(iiii)|(IIII)|[ⅳⅣ]",
                "[ⅴⅤ]", "[ⅵⅥ]", "[ⅶⅦ]", "(viii)|(VIII)|[ⅷⅧ]", "[ⅸⅨ]", "[ⅹⅩ]"};
        String[] arabDigitArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (int i = 0; i < romanDigitRegexArray.length; i++) {
            str = str.replaceAll(romanDigitRegexArray[i], arabDigitArray[i]);
        }
        return str;
    }

    /**
     * 去除字符串中的季数
     */
    public static void removeSeasonInStr(String str) {
        String regex = "第[一二三四五六七八九十零\\d]+期|第[一二三四五六七八九十零\\d]+部|第[一二三四五六七八九十零\\d]+集|第[一二三四五六七八九十零\\d]+季";
        String numSize = "\\d+期|第[一二三四五六七八九十零\\d]+期|第[一二三四五六七八九十零\\d]+部|[一二三四五六七八九十零\\d]+部|第[一二三四五六七八九十零\\d]+集|[一二三四五六七八九十零\\d]+集|[一二三四五六七八九十零\\d]+季|第[一二三四五六七八九十零\\d]+季|[一二三四五六七八九十零\\d]+卷|第[一二三四五六七八九十零\\d]+卷";

    }


    public static <T> T convert2Simplified(Object object, Class<T> clazz) {
        if (object != null) {
            ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
            String objStr = JSON.toJSONString(object);
            if (MyStringUtils.isNotBlank2(objStr)) {
                objStr = converter.convert(objStr);
            }
            return JSONObject.parseObject(objStr, clazz);
        }
        return null;
    }

    public static Document convert2Simplified(Document originDoc) {
        if (originDoc != null) {
            ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
            String objStr = JSON.toJSONString(originDoc);
            if (MyStringUtils.isNotBlank2(objStr)) {
                objStr = converter.convert(objStr);
            }
            originDoc = Document.parse(objStr);
        }
        return originDoc;
    }

    public static String convert2Simplified(String jsonStr) {
        if (MyStringUtils.isNotBlank2(jsonStr)) {
            ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
            jsonStr = converter.convert(jsonStr);
        }
        return jsonStr;
    }


    public static Document unescapeHtml3(Document originDoc) {
        if (originDoc != null) {
            Set<String> keySet = originDoc.keySet();
            for (String key : keySet) {
                Object value = originDoc.get(key);
                if (value != null) {
                    if (value instanceof String) {
                        String str = StringEscapeUtils.unescapeHtml3(value.toString());
                        originDoc.put(key, str);
                    }
                }
            }
        }
        return originDoc;
    }

    public static String unescapeHtml3(String jsonStr) {
        if (jsonStr != null) {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            Set<String> keySet = jsonObject.keySet();
            for (String key : keySet) {
                Object value = jsonObject.get(key);
                if (value != null) {
                    if (value instanceof String) {
                        String str = StringEscapeUtils.unescapeHtml3(value.toString());
                        jsonObject.put(key, str);
                    }
                }
            }
            jsonStr = JSON.toJSONString(jsonObject);
        }
        return jsonStr;
    }

    public static Set<String> convertQj2Bj(Set<String> originSet) {
        Set<String> newSet = new HashSet<>();
        if (originSet != null) {
            for (String str : originSet) {
                str = BCConvert.qj2bj(str);
                newSet.add(str);
            }
        }
        return newSet.isEmpty() ? null : newSet;
    }

    public static Document convertQj2Bj(Document originDoc) {
        if (originDoc != null) {
            Set<String> keySet = originDoc.keySet();
            for (String key : keySet) {
                if (key.equals("summariness")) {
                    continue;
                }
                Object value = originDoc.get(key);
                if (value != null) {
                    if (value instanceof String) {
                        String str = BCConvert.qj2bj(value.toString());
                        originDoc.put(key, str);
                    } else if (value instanceof Collection) {
                        Object[] valueArray = ((Collection) value).toArray();
                        if (valueArray.length > 0) {
                            if (valueArray[0] instanceof Document) {
                                for (int i = 0; i < valueArray.length; i++) {
                                    Document document = Document.parse(JSON.toJSONString(valueArray[i]));
                                    valueArray[i] = convertQj2Bj(document);
                                }
                            } else {
                                for (int i = 0; i < valueArray.length; i++) {
                                    valueArray[i] = BCConvert.qj2bj(valueArray[i].toString());
                                }
                            }

                            originDoc.put(key, valueArray);
                        }
                    }
                }
            }
        }
        return originDoc;
    }


    public static BaseDocument convertQj2Bj(BaseDocument originDoc) {
        if (originDoc != null) {
            Map<String, Object> propertiesMap = originDoc.getProperties();
            Set<String> keySet = propertiesMap.keySet();
            for (String key : keySet) {
                if (key.equals("summariness")) {
                    continue;
                }
                Object value = propertiesMap.get(key);
                if (value instanceof String) {
                    String str = BCConvert.qj2bj(value.toString());
                    propertiesMap.put(key, str);
                } else if (value instanceof Collection) {
                    Object[] valueArray = ((Collection) value).toArray();
                    if (valueArray.length > 0) {
                        if (valueArray[0] instanceof Document) {
                            for (int i = 0; i < valueArray.length; i++) {
                                Document document = Document.parse(JSON.toJSONString(valueArray[i]));
                                document = MyStringUtils.convertQj2Bj(document);
                                valueArray[i] = document;
                            }
                        } else {
                            for (int i = 0; i < valueArray.length; i++) {
                                valueArray[i] = BCConvert.qj2bj(valueArray[i].toString());
                            }
                        }

                        propertiesMap.put(key, valueArray);
                    }

                }
            }
            originDoc.setProperties(propertiesMap);
        }

        return originDoc;
    }

    public static String convert2OrderedStr(Set<String> set, String separator) {
        String str = null;

        if (set != null && !set.isEmpty() && separator != null) {
            Set<String> tempSet = new TreeSet<>();
            tempSet.addAll(set);
            str = StringUtils.join(tempSet, separator);
        }

        return str;
    }


    public static List<String> sortStringCollByLength(Collection<String> originColl) {
        List<String> sortedList = new ArrayList<>();
        if (originColl != null && !originColl.isEmpty()) {
            sortedList.addAll(originColl);
            String temp;
            for (int i = 0; i < sortedList.size(); i++) {
                for (int j = sortedList.size() - 1; j > i; j--) {
                    if (sortedList.get(i).length() < sortedList.get(j).length()) {
                        temp = sortedList.get(i);
                        sortedList.set(i, sortedList.get(j));
                        sortedList.set(j, temp);
                    }
                }
            }
        }
        return sortedList;
    }



    public static Date parseStr2Date(String str) {
        Date date = null;

        if (MyStringUtils.isNotBlank2(str)) {
            str = str.trim();
            try {
                Pattern p1 = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
                Matcher m1 = p1.matcher(str);
                if (m1.matches()) {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
                    return date;
                }

                Pattern p2 = Pattern.compile("\\d{4}-\\d{1,2}");
                Matcher m2 = p2.matcher(str);
                if (m2.matches()) {
                    date = new SimpleDateFormat("yyyy-MM").parse(str);
                    return date;
                }

                Pattern p3 = Pattern.compile("\\d{1,2}-\\d{1,2}");
                Matcher m3 = p3.matcher(str);
                if (m3.matches()) {
                    date = new SimpleDateFormat("MM-dd").parse(str);
                    return date;
                }

                Pattern p4 = Pattern.compile("\\d{4}");
                Matcher m4 = p4.matcher(str);
                if (m4.matches()) {
                    date = new SimpleDateFormat("yyyy").parse(str);
                    return date;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    public static String getMd5OfString(String message) {
        String md5Value = null;

        if (message != null) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] input = message.getBytes();
                byte[] buff = md.digest(input);
                md5Value = bytesToHex(buff);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return md5Value;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5str = new StringBuffer();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString();
    }

    public static boolean haveCommonElement(Set<String> set1, Set<String> set2) {
        if (set1 == null || set2 == null || set1.isEmpty() || set2.isEmpty()) {
            return false;
        }

        for (String s : set1) {
            if (set2.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
//        String bikeCollName = "clean_actor";
//        FindIterable<Document> findIterable = MongodbUtil.getCollection(bikeCollName).find();
//        MongoCursor<Document> mongoCursor = findIterable.noCursorTimeout(true).iterator();
//        while (mongoCursor.hasNext()) {
//            Document celebBaike = mongoCursor.next();
//            String weight = formatDateStr(celebBaike.getString("birthday"));
////            System.out.println(weight);
//        }


//        String movieName = "可可小爱社会主义核心价值观童谣";
//        String roleName = "巫巫鸦";
//        String actorName = "";
//
//        String uniquegroupid = MyStringUtils.getMd5OfString(roleName + "_" + movieName + "_" + actorName);
//        String roleNameMd5 = MyStringUtils.getMd5OfString(roleName);
//        String actorNameMd5 = MyStringUtils.getMd5OfString(actorName);
//        String movieNameMd5 = MyStringUtils.getMd5OfString(movieName);
//        String movieRoleKey = MyStringUtils.getMd5OfString(movieNameMd5 + "_" + roleNameMd5 + "_" + "角色" + "_" + uniquegroupid);
//        String actorRoleKey = MyStringUtils.getMd5OfString(actorNameMd5 + "_" + roleNameMd5 + "_" + "扮演" + "_" + uniquegroupid);
//
//        System.out.println(movieNameMd5);
//        System.out.println(roleNameMd5);
//        System.out.println(uniquegroupid);
//        System.out.println(movieRoleKey);
//        System.out.println(actorRoleKey);

        String date = "1990-11-11";
        System.out.println(BCConvert.qj2bj(date));
        System.out.println(getMd5OfString("直播"));

    }


}
