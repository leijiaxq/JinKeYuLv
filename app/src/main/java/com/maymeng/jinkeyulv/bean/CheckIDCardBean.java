package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/4/25 14:21
 * Describe    校验身份证信息返回的bean
 */
public class CheckIDCardBean extends BaseBean{


    /**
     * msg : 成功：姓名和身份证号一致
     * code : 0
     * data : {"cardno":"330727199102104720","birthday":"1987-12-10","sex":"M","name":"刘文","address":"浙江省金华市磐安县"}
     */

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean {
        /**
         * cardno : 330727199102104720
         * birthday : 1987-12-10
         * sex : M
         * name : 刘文
         * address : 浙江省金华市磐安县
         */

        public String cardno;
        public String birthday;
        public String sex;
        public String name;
        public String address;
    }
}
