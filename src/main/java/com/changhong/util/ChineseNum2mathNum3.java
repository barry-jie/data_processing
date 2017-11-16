package com.changhong.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseNum2mathNum3 {
    static Map<String, Integer> unitMap = new HashMap<String, Integer>();
    static Map<String, Integer> numMap = new HashMap<String, Integer>();
    static Map<String, String> map = new HashMap<>();
    static Pattern p = Pattern.compile("((一|二|三|四|五|六|七|八|九|十|零)+(百|千|万|亿)*)+");

    static {
        unitMap.put("十", 10);
        unitMap.put("百", 100);
        unitMap.put("千", 1000);
        unitMap.put("万", 10000);
        unitMap.put("亿", 100000000);

        numMap.put("零", 0);
        numMap.put("一", 1);
        numMap.put("二", 2);
        numMap.put("三", 3);
        numMap.put("四", 4);
        numMap.put("五", 5);
        numMap.put("六", 6);
        numMap.put("七", 7);
        numMap.put("八", 8);
        numMap.put("九", 9);


        map.put("1", "一");
        map.put("2", "二");
        map.put("3", "三");
        map.put("4", "四");
        map.put("5", "五");
        map.put("6", "六");
        map.put("7", "七");
        map.put("8", "八");
        map.put("9", "九");
        map.put("0", "零");
    }

    public static long chn2digit(String chnStr) {
        //队列
        List<Long> queue = new ArrayList<Long>();
        long tempNum = 0;
        for (int i = 0; i < chnStr.length(); i++) {
            char bit = chnStr.charAt(i);
//	            if(flag){
//	            	System.out.print(bit);  
//	            	return bit;
//	            }

            //数字
            if (numMap.containsKey(bit + "")) {

                tempNum = tempNum + numMap.get(bit + "");

                //一位数、末位数、亿或万的前一位进队列
                if (chnStr.length() == 1
                        | i == chnStr.length() - 1
                        | (i + 1 < chnStr.length() && (chnStr.charAt(i + 1) == '亿' | chnStr
                        .charAt(i + 1) == '万'))) {
                    queue.add(tempNum);
                }
            }
            //单位
            else if (unitMap.containsKey(bit + "")) {

                //遇到十 转换为一十、临时变量进队列
                if (bit == '十') {
                    if (tempNum != 0) {
                        tempNum = tempNum * unitMap.get(bit + "");
                    } else {
                        tempNum = 1 * unitMap.get(bit + "");
                    }
                    queue.add(tempNum);
                    tempNum = 0;
                }

                //遇到千、百 临时变量进队列
                if (bit == '千' | bit == '百') {
                    if (tempNum != 0) {
                        tempNum = tempNum * unitMap.get(bit + "");
                    }
                    queue.add(tempNum);
                    tempNum = 0;
                }

                //遇到亿、万 队列中各元素依次累加*单位值、清空队列、新结果值进队列
                if (bit == '亿' | bit == '万') {
                    long tempSum = 0;
                    if (queue.size() != 0) {
                        for (int j = 0; j < queue.size(); j++) {
                            tempSum += queue.get(j);
                        }
                    } else {
                        tempSum = 1;
                    }
                    tempNum = tempSum * unitMap.get(bit + "");
                    queue.clear();//清空队列
                    queue.add(tempNum);//新结果值进队列
                    tempNum = 0;
                }
            }
        }

        //output
        long sum = 0;
        for (Long i : queue) {
            sum += i;
        }
        return sum;
    }

    public static String go(String str) {
        char[] c = str.toCharArray();
        str = "";
        for (int i = 0; i < c.length; i++) {
            String tmp = map.get(c[i] + "");
            if (tmp == null) {
                str = str + c[i];
            } else {
                str = str + map.get(c[i] + "");
            }
        }

        Pattern p2 = Pattern.compile("十|百|千|万|亿");
        if (!p2.matcher(str).find()) {
            return str;
        }
        p2 = Pattern.compile("[^0-9|一|二|三|四|五|六|七|八|九]+十(一|二|三|四|五|六|七|八|九)");
        if (p2.matcher(str).find()) {
            str = str.replace("十", "一");
            return str;
        }
        String result = "";
        Matcher m = p.matcher(str);
        while (m.find()) {
            result = m.group();
            str = str.replace(result, chn2digit(result) + "");
        }
        c = str.toCharArray();
        String result2 = "";
        for (int i = 0; i < c.length; i++) {
            String tmp = map.get(c[i] + "");
            if (tmp == null) {
                result2 = result2 + c[i];
            } else {
                result2 = result2 + map.get(c[i] + "");
            }
        }
        return result2;
    }

    public static String change(String str) {
        String result2 = "";
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            String tmp = map.get(c[i] + "");
            if (tmp == null) {
                result2 = result2 + c[i];
            } else {
                result2 = result2 + map.get(c[i] + "");
            }
        }
        return result2;
    }

    public static void main(String[] args) {
        System.out.println(change("2017"));
    }


}




/* MongoCursor<Document> iter = MongoDBUtil.getCollection("test", "name_num_test").find().iterator();
    List<Document> list = new ArrayList<>();
	while(iter.hasNext()){
		String result2 = "";
		Document document = iter.next();
		String name = document.getString("name");
		document.remove("_id");
		document.put("name", name);
		document.remove("allName");
		document.remove("name2");
		char[] c = name.toCharArray();
		 for(int i =0;i<c.length;i++){
			 String tmp = map.get(c[i]+"");
			 if(tmp == null){
				 result2 = result2 + c[i];
			 }else{
				 result2 = result2 + map.get(c[i]+"");
			 }
		 }
		 document.put("name2", result2);
		list.add(document);
		if(list.size()> 1000){
			MongoDBUtil.getCollection("test", "name_num_test2").insertMany(list);
			list.clear();
		}
	}
	MongoDBUtil.getCollection("test", "name_num_test2").insertMany(list);*/