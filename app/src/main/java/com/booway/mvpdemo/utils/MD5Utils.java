package com.booway.mvpdemo.utils;

import java.security.MessageDigest;

/**
 * MD5转换
 */
public class MD5Utils
{
    /**
     * 生成32位MD5码
     * @param data
     * @return
     */
    public static String string2MD5(String data)
    {
        StringBuffer hexValue = new StringBuffer();
        try
        {
            byte[] md5Bytes = MessageDigest.getInstance("MD5").digest(data.getBytes("UTF-8"));
            for (int i = 0; i < md5Bytes.length; i++)
            {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return hexValue.toString().toUpperCase();
    }

    public static void main(String[] args)
    {
        String str = "{\"username\":\"xyt\",\"userCode\":\"booway2015\",\"userDeviceInfo\":{\"deviceId\":\"123456\"}}";
        System.out.println("原始：" + str);
        System.out.println("MD5：" + MD5Utils.string2MD5(str));
    }
}
