package com.maymeng.jinkeyulv.bean;

import java.util.List;

/**
 * Created by  leijiaxq
 * Date        2017/5/12 16:40
 * Describe
 */

public class SignBean extends BaseBean {

    /**
     * StateCode : 200
     * ResponseMessage : 查询成功
     * ResponseData : [{"OrderId":82,"CustomerName":"各个地方","ReportNumber":"151654897464","Phone":"15017947081","IsRead":true,"CaseId":99},{"OrderId":1,"CustomerName":"潇洒","ReportNumber":"20170508001","Phone":"15000758312","IsRead":true,"CaseId":1},{"OrderId":2,"CustomerName":"胜多负少","ReportNumber":"20170508002","Phone":"","IsRead":true,"CaseId":2}]
     */

    public String StateCode;
    public String ResponseMessage;
    public List<ResponseDataBean> ResponseData;

    public static class ResponseDataBean {
        /**
         * OrderId : 82
         * CustomerName : 各个地方
         * ReportNumber : 151654897464
         * Phone : 15017947081
         * IsRead : true
         * CaseId : 99
         */

        public int OrderId;
        public String CustomerName;
        public String ReportNumber;
        public String Phone;
        public boolean IsRead;
        public int CaseId;
    }
}
