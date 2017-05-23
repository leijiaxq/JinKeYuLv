package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/4/25 14:54
 * Describe    银行卡实名校验bean
 */
public class CheckBankCardBean extends BaseBean{


    /**
     * code : 0
     * msg : 验证信息一致
     * phone_data : {"mobile_prov":"广东","mobile_city":"佛山","mobile_type":"移动预付费卡","phone":"13433172757"}
     * idcard_data : {"birthday":"1986-09-09","idcardno":"450331198609090618","name":"游腾洲","address":"广西壮族自治区桂林市荔浦县","sex":"M"}
     * data : {"bankurl":"http://www.ccb.com","areainfo":"","servicephone":"95533","bankname":"建设银行","cardtype":"借记卡","cardprefixnum":"622700","cardname":"龙卡储蓄卡(银联卡)","bankcardno":"6227003231860083378"}
     */

    public int code;
    public String msg;
    public PhoneDataBean phone_data;
    public IdcardDataBean idcard_data;
    public DataBean data;

    public static class PhoneDataBean {
        /**
         * mobile_prov : 广东
         * mobile_city : 佛山
         * mobile_type : 移动预付费卡
         * phone : 13433172757
         */

        public String mobile_prov;
        public String mobile_city;
        public String mobile_type;
        public String phone;
    }

    public static class IdcardDataBean {
        /**
         * birthday : 1986-09-09
         * idcardno : 450331198609090618
         * name : 游腾洲
         * address : 广西壮族自治区桂林市荔浦县
         * sex : M
         */

        public String birthday;
        public String idcardno;
        public String name;
        public String address;
        public String sex;
    }

    public static class DataBean {
        /**
         * bankurl : http://www.ccb.com
         * areainfo :
         * servicephone : 95533
         * bankname : 建设银行
         * cardtype : 借记卡
         * cardprefixnum : 622700
         * cardname : 龙卡储蓄卡(银联卡)
         * bankcardno : 6227003231860083378
         */

        public String bankurl;
        public String areainfo;
        public String servicephone;
        public String bankname;
        public String cardtype;
        public String cardprefixnum;
        public String cardname;
        public String bankcardno;
    }
}
