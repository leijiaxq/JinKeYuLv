package com.maymeng.jinkeyulv.utils;

import java.util.regex.Pattern;

/**
 * Create by  leijiaxq
 * Date       2017/3/21 11:52
 * Describe   正则相关工具类
 */

public class RegexUtil {


    private RegexUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
  /*  // 验证手机号码
    public static boolean isMobilePhone(String mobiles) {

        Pattern p = Pattern.compile("^((14[0-9])|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);
        return m.matches();

    }
    // 验证密码（6-26数字字母下划线）
    public static boolean isPassword(String mobiles) {

        Pattern p = Pattern.compile("^[0-9a-zA-Z]{6,20}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }*/

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$", input);
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(CharSequence input) {
        return isMatch("\"^[1-9]\\\\d{7}((0\\\\d)|(1[0-2]))(([0|1|2]\\\\d)|3[0-1])\\\\d{3}$\"", input);
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(CharSequence input) {
        return isMatch("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$", input);
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isZh(CharSequence input) {
        return isMatch("^[\\u4e00-\\u9fa5]+$", input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }


    /**
     * 判断银行卡号
     */
    public static boolean isBankNo(String bankno) {
        int lastNum = Integer.parseInt(bankno.substring(bankno.length() - 1,
                bankno.length()));// 取出最后一位（与luhm进行比较）


        String first15Num = bankno.substring(0, bankno.length() - 1);// 前15或18位

        char[] newArr = new char[first15Num.length()]; // 倒叙装入newArr
        char[] tempArr = first15Num.toCharArray();
        for (int i = 0; i < tempArr.length; i++) {
            newArr[tempArr.length - 1 - i] = tempArr[i];
        }

        int[] arrSingleNum = new int[newArr.length]; // 奇数位*2的积 <9
        int[] arrSingleNum2 = new int[newArr.length];// 奇数位*2的积 >9
        int[] arrDoubleNum = new int[newArr.length]; // 偶数位数组


        for (int j = 0; j < newArr.length; j++) {
            if ((j + 1) % 2 == 1) {// 奇数位
                if ((int) (newArr[j] - 48) * 2 < 9)
                    arrSingleNum[j] = (int) (newArr[j] - 48) * 2;
                else
                    arrSingleNum2[j] = (int) (newArr[j] - 48) * 2;
            } else
// 偶数位
                arrDoubleNum[j] = (int) (newArr[j] - 48);
        }


        int[] arrSingleNumChild = new int[newArr.length]; // 奇数位*2 >9
// 的分割之后的数组个位数
        int[] arrSingleNum2Child = new int[newArr.length];// 奇数位*2 >9
// 的分割之后的数组十位数


        for (int h = 0; h < arrSingleNum2.length; h++) {
            arrSingleNumChild[h] = (arrSingleNum2[h]) % 10;
            arrSingleNum2Child[h] = (arrSingleNum2[h]) / 10;
        }


        int sumSingleNum = 0; // 奇数位*2 < 9 的数组之和
        int sumDoubleNum = 0; // 偶数位数组之和
        int sumSingleNumChild = 0; // 奇数位*2 >9 的分割之后的数组个位数之和
        int sumSingleNum2Child = 0; // 奇数位*2 >9 的分割之后的数组十位数之和
        int sumTotal = 0;
        for (int m = 0; m < arrSingleNum.length; m++) {
            sumSingleNum = sumSingleNum + arrSingleNum[m];
        }


        for (int n = 0; n < arrDoubleNum.length; n++) {
            sumDoubleNum = sumDoubleNum + arrDoubleNum[n];
        }


        for (int p = 0; p < arrSingleNumChild.length; p++) {
            sumSingleNumChild = sumSingleNumChild + arrSingleNumChild[p];
            sumSingleNum2Child = sumSingleNum2Child + arrSingleNum2Child[p];
        }


        sumTotal = sumSingleNum + sumDoubleNum + sumSingleNumChild
                + sumSingleNum2Child;


// 计算Luhm值
        int k = sumTotal % 10 == 0 ? 10 : sumTotal % 10;
        int luhm = 10 - k;


        if (lastNum == luhm) {
            return true;// 验证通过
        } else {
            return false;
        }
    }


}
