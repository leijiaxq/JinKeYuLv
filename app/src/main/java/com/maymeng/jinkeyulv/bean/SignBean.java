package com.maymeng.jinkeyulv.bean;

import android.os.Parcel;
import android.os.Parcelable;

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

    public static class ResponseDataBean implements Parcelable {
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

        public String EntryApprovalForm;
        public String AgentContract;

        public String AgencyFees;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.OrderId);
            dest.writeString(this.CustomerName);
            dest.writeString(this.ReportNumber);
            dest.writeString(this.Phone);
            dest.writeByte(this.IsRead ? (byte) 1 : (byte) 0);
            dest.writeInt(this.CaseId);
            dest.writeString(this.EntryApprovalForm);
            dest.writeString(this.AgentContract);
            dest.writeString(this.AgencyFees);
        }

        public ResponseDataBean() {
        }

        protected ResponseDataBean(Parcel in) {
            this.OrderId = in.readInt();
            this.CustomerName = in.readString();
            this.ReportNumber = in.readString();
            this.Phone = in.readString();
            this.IsRead = in.readByte() != 0;
            this.CaseId = in.readInt();
            this.EntryApprovalForm = in.readString();
            this.AgentContract = in.readString();
            this.AgencyFees = in.readString();
        }

        public static final Creator<ResponseDataBean> CREATOR = new Creator<ResponseDataBean>() {
            @Override
            public ResponseDataBean createFromParcel(Parcel source) {
                return new ResponseDataBean(source);
            }

            @Override
            public ResponseDataBean[] newArray(int size) {
                return new ResponseDataBean[size];
            }
        };
    }
}
