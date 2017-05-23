package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/4/28 10:58
 * Describe    用户登录bean
 */
public class LoginBean extends BaseBean{


    /**
     * StateCode : 200
     * ResponseMessage : 登录成功
     * ResponseData : {"AccountId":1,"AccountName":"qiuhongliang","JobName":"xx","LastLoginIP":"127.0.0.1","LastLoginDate":"2017-01-01T00:00:00","LoginCount":2}
     */

    public String StateCode;
    public String ResponseMessage;
    public ResponseDataBean ResponseData;

    public static class ResponseDataBean {
        /**
         * AccountId : 1
         * AccountName : qiuhongliang
         * JobName : xx
         * LastLoginIP : 127.0.0.1
         * LastLoginDate : 2017-01-01T00:00:00
         * LoginCount : 2
         */

        public int AccountId;
        public String AccountName;
        public String JobName;
        public String LastLoginIP;
        public String LastLoginDate;
        public int LoginCount;
    }
}
