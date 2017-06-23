package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/6/23 15:37
 * Describe
 */
public class CheckIDCardImagetBean extends BaseBean{


    /**
     * state : 1
     * data : {"nation":"汉","number":"431126199112191210","name":"雷佳佳","address":"湖南省宁远县水市镇过水岩村一组","sex":"男","date1":"20060612","date2":"20110612","office":"河津市公安局"}
     */

    public int state;
    public String message;
    public DataBean data;

    public static class DataBean {
        /**
         * nation : 汉
         * number : 431126199112191210
         * name : 雷佳佳
         * address : 湖南省宁远县水市镇过水岩村一组
         * sex : 男
         * date1 : 20060612
         * date2 : 20110612
         * office : 河津市公安局
         */

        public String nation;
        public String number;
        public String name;
        public String address;
        public String sex;
        public String date1;
        public String date2;
        public String office;
    }
}
