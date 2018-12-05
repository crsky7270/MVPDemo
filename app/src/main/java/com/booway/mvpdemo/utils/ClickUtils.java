package com.booway.mvpdemo.utils;

/**
 * @author wandun
 * @date 2018/12/5
 * @desc 点击处理方法，防止二次点击
 */
public class ClickUtils {
    // 上次点击时间
    private static long sLastTime;

    /**
     * 判断此次点击是否响应
     *
     * @return 响应则返回true，否则返回false
     */
    public static boolean isClick() {

        long time = TimeUtils.getCurTimeMills();
        if (sLastTime > time || time - sLastTime > 500) {
            synchronized (ClickUtils.class) {
                if (sLastTime > time || time - sLastTime > 500) {
                    sLastTime = time;
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
