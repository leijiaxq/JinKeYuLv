package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/4/25 14:50
 * Describe    手机实名校验
 */
public class CheckPhoneBean {

    /**
     * msg : 成功：验证信息一致
     * code : 0
     * data : {"moible_prov":"北京","sex":"M","birthday":"1983-11-12","address":"江苏省苏州市吴中区","mobile_operator":"联通185卡","mobile_city":"北京"}
     */

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean {
        /**
         * moible_prov : 北京
         * sex : M
         * birthday : 1983-11-12
         * address : 江苏省苏州市吴中区
         * mobile_operator : 联通185卡
         * mobile_city : 北京
         */

        public String moible_prov;
        public String sex;
        public String birthday;
        public String address;
        public String mobile_operator;
        public String mobile_city;
    }
}
