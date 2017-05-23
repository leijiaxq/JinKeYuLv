package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/4/28 16:38
 * Describe    提交写入信息返回的bean
 */
public class SubmitInfoBean extends BaseBean{


    /**
     * StateCode : string
     * ResponseMessage : string
     * ResponseData : {}
     */

    public String StateCode;
    public String ResponseMessage;
    public ResponseDataBean ResponseData;

    public static class ResponseDataBean {
    }
}
