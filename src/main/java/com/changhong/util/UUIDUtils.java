package com.changhong.util;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author Chenfei
 *         Date: 13-4-24
 *         Time: 下午5:01
 */
public class UUIDUtils {

    public static void main(String[] args) {
        System.err.println(UUIDUtils.uuid());
    }
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        String temp;
        temp = str.replaceAll("-", "");
        return temp;
    }

    // 获得指定数量的UUID
    public static String[] uuids(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = uuid();
        }
        return ss;
    }


    public static String sn() {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, 12);
    }

    public static String sn(int num) {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, num);
    }
}
