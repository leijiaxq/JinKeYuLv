package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/5/2 18:03
 * Describe
 */
public class ReportNumberBean extends BaseBean{


    /**
     * StateCode : 200
     * ResponseMessage : 添加成功
     * ResponseData : {"CaseID":5}
     */

    public String StateCode;
    public String ResponseMessage;
    public ResponseDataBean ResponseData;


    public static class ResponseDataBean {
        /**
         * Report : GZ00000015
         */

        public String Report;

    }
}
