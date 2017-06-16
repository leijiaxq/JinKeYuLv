package com.maymeng.jinkeyulv.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 11:05
 * Describe    新的派单bean对象
 */

public class NewDispatchBean extends BaseBean {


    /**
     * StateCode : 200
     * ResponseMessage : 查询成功
     * ResponseData : [{"OrderId":1,"CustomerName":"球","ReportNumber":"","Phone":"","IsRead":true,"CaseId":1},{"OrderId":1,"CustomerName":"球","ReportNumber":"","Phone":"","IsRead":true,"CaseId":2},{"OrderId":1,"CustomerName":"球","ReportNumber":"","Phone":"","IsRead":true,"CaseId":3},{"OrderId":2,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":1},{"OrderId":2,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":2},{"OrderId":2,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":3},{"OrderId":3,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":1},{"OrderId":3,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":2},{"OrderId":3,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":3}]
     */

    public String StateCode;
    public String ResponseMessage;
    public List<ResponseDataBean> ResponseData;

    public static class ResponseDataBean implements Parcelable {
        /**
         * OrderId : 1
         * CustomerName : 球
         * ReportNumber :
         * Phone :
         * IsRead : true
         * CaseId : 1
         */


//        "IsStatus":0   案件中的状态，用于区分已完成或是味完成

        public int OrderId;
        public String CustomerName;
        public String ReportNumber;
        public String Phone;
        public boolean IsRead;
        public int CaseId;
        public boolean IsCheck;
        public String HospitalName;


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
            dest.writeByte(this.IsCheck ? (byte) 1 : (byte) 0);
            dest.writeString(this.HospitalName);
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
            this.IsCheck = in.readByte() != 0;
            this.HospitalName = in.readString();
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
