package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/5/4 17:36
 * Describe
 */
public class UploadFileBean extends BaseBean{


    /**
     * StateCode : 200
     * ResponseMessage : 成功
     * ResponseData : {"Data":"1","ImgUrl":"F:\\Project\\MeiMengMeng.WebAPI\\/Image/1493890335644.jpg"}
     */

    public String StateCode;
    public String ResponseMessage;
    public ResponseDataBean ResponseData;

    public static class ResponseDataBean {
        /**
         * Data : 1
         * ImgUrl : F:\Project\MeiMengMeng.WebAPI\/Image/1493890335644.jpg
         */

        public String Data;
        public String ImgUrl;
    }

    @Override
    public String toString() {
        return "UploadFileBean{" +
                "StateCode='" + StateCode + '\'' +
                ", ResponseMessage='" + ResponseMessage + '\'' +
                ", ResponseData=" + ResponseData +
                '}';
    }
}
