package com.maymeng.jinkeyulv.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by  leijiaxq
 * Date        2017/6/14 14:52
 * Describe
 */

public class QueryBean extends BaseBean{


    /**
     * StateCode : 200
     * ResponseMessage : 查询成功
     * ResponseData : [{"CustomerName":"大神","Phone":"","OrderId":1,"CaseId":1,"UserInfoRemake":"","SignRemake":"1","UserInfoState":0,"SignState":0,"InJuryState":0,"CheckState":0,"MedicaState":0,"ClaimState":0,"PleteState":0}]
     */

    public String StateCode;
    public String ResponseMessage;
    public List<ResponseDataBean> ResponseData;

    public static class ResponseDataBean implements Parcelable {
        /**
         * CustomerName : 大神
         * Phone :
         * OrderId : 1
         * CaseId : 1
         * UserInfoRemake :
         * SignRemake : 1
         * UserInfoState : 0
         * SignState : 0
         * InJuryState : 0
         * CheckState : 0
         * MedicaState : 0
         * ClaimState : 0
         * PleteState : 0
         */

        public String CustomerName;
        public String Phone;
        public int OrderId;
        public int CaseId;
        public String UserInfoRemake;   //资料审核驳回原因
        public String SignRemake;      //签约审核驳回原因
        public int UserInfoState;      //资料审核状态
        public int SignState;//签约审核状态
        public int InJuryState;//伤情判定状态
        public int CheckState;//信息校验状态
        public int MedicaState;//用药判定状态
        public int ClaimState;//理赔判定状态
        public int PleteState;//完结状态

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.CustomerName);
            dest.writeString(this.Phone);
            dest.writeInt(this.OrderId);
            dest.writeInt(this.CaseId);
            dest.writeString(this.UserInfoRemake);
            dest.writeString(this.SignRemake);
            dest.writeInt(this.UserInfoState);
            dest.writeInt(this.SignState);
            dest.writeInt(this.InJuryState);
            dest.writeInt(this.CheckState);
            dest.writeInt(this.MedicaState);
            dest.writeInt(this.ClaimState);
            dest.writeInt(this.PleteState);
        }

        public ResponseDataBean() {
        }

        protected ResponseDataBean(Parcel in) {
            this.CustomerName = in.readString();
            this.Phone = in.readString();
            this.OrderId = in.readInt();
            this.CaseId = in.readInt();
            this.UserInfoRemake = in.readString();
            this.SignRemake = in.readString();
            this.UserInfoState = in.readInt();
            this.SignState = in.readInt();
            this.InJuryState = in.readInt();
            this.CheckState = in.readInt();
            this.MedicaState = in.readInt();
            this.ClaimState = in.readInt();
            this.PleteState = in.readInt();
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
