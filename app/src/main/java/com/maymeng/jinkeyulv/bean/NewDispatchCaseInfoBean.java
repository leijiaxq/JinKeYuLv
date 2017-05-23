package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/5/3 18:22
 * Describe
 */
public class NewDispatchCaseInfoBean extends BaseBean{


    /**
     * StateCode : 200
     * ResponseMessage : 查询成功
     * ResponseData : {"CaseNumber":"string","OutDangerTime":"2017-05-03T06:45:14","OutDangerAddress":"string","CasualtiesType":"string","OutDangerDescription":"string","CustomerName":"","Sex":"","Age":0,"Phone":"","HouseholdRegisterAddress":"","HouseholdRegisterType":"","IDCard":"","LiveInfo":"","LiveStartTime":null,"LiveEndTime":null,"JobStartTime":null,"JobEndTime":null,"SocialSecurity":"","LaborContract":"","SalaryFrom":"","HospitalName":"","SectionBed":"","AuditTime":null,"SurveyImg":"","HomesImg":"","WardImg":"","BedsideCardImg":"","CaseDataImg":"","XCTImg":"","CostImg":"","WholeBodyImg":"","InjuryImg":"","IDCardImg":"","NoticeLetterImg":""}
     */

    public String StateCode;
    public String ResponseMessage;
    public ResponseDataBean ResponseData;

    public static class ResponseDataBean {
        /**
         * CaseNumber : string
         * OutDangerTime : 2017-05-03T06:45:14
         * OutDangerAddress : string
         * CasualtiesType : string
         * OutDangerDescription : string
         * CustomerName :
         * Sex :
         * Age : 0
         * Phone :
         * HouseholdRegisterAddress :
         * HouseholdRegisterType :
         * IDCard :
         * LiveInfo :
         * LiveStartTime : null
         * LiveEndTime : null
         * JobStartTime : null
         * JobEndTime : null
         * SocialSecurity :
         * LaborContract :
         * SalaryFrom :
         * HospitalName :
         * SectionBed :
         * AuditTime : null
         * SurveyImg :
         * HomesImg :
         * WardImg :
         * BedsideCardImg :
         * CaseDataImg :
         * XCTImg :
         * CostImg :
         * WholeBodyImg :
         * InjuryImg :
         * IDCardImg :
         * NoticeLetterImg :
         */

        public String CaseNumber;
        public String OutDangerTime;
        public String OutDangerAddress;
        public String CasualtiesType;
        public String OutDangerDescription;
        public String CustomerName;
        public String Sex;
        public int Age;
        public String Phone;
        public String HouseholdRegisterAddress;
        public String HouseholdRegisterType;
        public String IDCard;
        public String LiveInfo;
        public String LiveStartTime;
        public String LiveEndTime;
        public String JobStartTime;
        public String JobEndTime;
        public String SocialSecurity;
        public String LaborContract;
        public String SalaryFrom;
        public String HospitalName;
        public String SectionBed;
        public String AuditTime;
        public String SurveyImg;
        public String HomesImg;
        public String WardImg;
        public String BedsideCardImg;
        public String CaseDataImg;
        public String XCTImg;
        public String CostImg;
        public String WholeBodyImg;
        public String InjuryImg;
        public String IDCardImg;
        public String NoticeLetterImg;
    }
}
