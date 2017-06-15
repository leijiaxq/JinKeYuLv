package com.maymeng.jinkeyulv.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by  leijiaxq
 * Date        2017/6/14 16:43
 * Describe    信息校验列表也
 */
public class CheckInfoBean extends BaseBean{
    /**
     * StateCode : 200
     * ResponseMessage : 查询成功
     * ResponseData : [{"OrderId":1,"CustomerName":"球","ReportNumber":"","Phone":"","IsRead":true,"CaseId":1},{"OrderId":1,"CustomerName":"球","ReportNumber":"","Phone":"","IsRead":true,"CaseId":2},{"OrderId":1,"CustomerName":"球","ReportNumber":"","Phone":"","IsRead":true,"CaseId":3},{"OrderId":2,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":1},{"OrderId":2,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":2},{"OrderId":2,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":3},{"OrderId":3,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":1},{"OrderId":3,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":2},{"OrderId":3,"CustomerName":"胜多负少","ReportNumber":"","Phone":"","IsRead":false,"CaseId":3}]
     */

    public String StateCode;
    public String ResponseMessage;
    public List<CheckInfoBean.ResponseDataBean> ResponseData;

    public static class ResponseDataBean implements Parcelable {


        public int OrderId;
        public String CustomerName;
        public String Phone;
        public int CaseId;
        public String IDCard;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.OrderId);
            dest.writeString(this.CustomerName);
            dest.writeString(this.Phone);
            dest.writeInt(this.CaseId);
            dest.writeString(this.IDCard);
        }

        public ResponseDataBean() {
        }

        protected ResponseDataBean(Parcel in) {
            this.OrderId = in.readInt();
            this.CustomerName = in.readString();
            this.Phone = in.readString();
            this.CaseId = in.readInt();
            this.IDCard = in.readString();
        }

        public static final Parcelable.Creator<ResponseDataBean> CREATOR = new Parcelable.Creator<ResponseDataBean>() {
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
