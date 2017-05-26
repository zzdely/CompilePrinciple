package com.github.piasy.biv.aspect;

/**
 * Created by 50567 on 17/5/18.
 */

/**
 * byte操作类.
 *
 * @author <a href="mailto:xiexingxing1121@126.com">AmigoXie</a>
 * @version 1.0 Creation date: 2008-4-30 - 下午04:57:23
 */
public class Bytes {
    /**
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 包含汉字的字符串时存在隐患，现调整如下：
     *
     * @param src
     *            要截取的字符串
     * @param start_idx
     *            开始坐标（包括该坐标)
     * @param end_idx
     *            截止坐标（包括该坐标）
     * @return
     */
    public static String substring(String src, int start_idx, int end_idx) {
        byte[] b = src.getBytes();
        String tgt = "";
        for (int i = start_idx; i <= end_idx; i++) {
            tgt += (char) b[i];
        }
        return tgt;
    }
}
