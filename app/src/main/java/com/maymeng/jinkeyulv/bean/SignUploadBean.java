package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/5/12 17:17
 * Describe
 */
public class SignUploadBean extends BaseBean{


    /**
     * StateCode : 200
     * ResponseMessage : 上传成功
     * ResponseData : {"CaseId":1,"EntryApprovalForm":"string","AgentContract":"string"}
     */

    public String StateCode;
    public String ResponseMessage;
    public ResponseDataBean ResponseData;

    public static class ResponseDataBean {
        /**
         * CaseId : 1
         * EntryApprovalForm : string
         * AgentContract : string
         */

        public int CaseId;
        public String EntryApprovalForm;
        public String AgentContract;
    }
}
