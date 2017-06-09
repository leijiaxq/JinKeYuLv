package com.maymeng.jinkeyulv.api;


import com.maymeng.jinkeyulv.bean.AddCaseBean;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.NewDispatchBean;
import com.maymeng.jinkeyulv.bean.NewDispatchCaseInfoBean;
import com.maymeng.jinkeyulv.bean.PictureInfoBean;
import com.maymeng.jinkeyulv.bean.ReportNumberBean;
import com.maymeng.jinkeyulv.bean.SignBean;
import com.maymeng.jinkeyulv.bean.SignUploadBean;
import com.maymeng.jinkeyulv.bean.SubmitInfoBean;
import com.maymeng.jinkeyulv.bean.UploadFileBean;
import com.maymeng.jinkeyulv.bean.VerificationCodeBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Create by  leijiaxq
 * Date       2017/3/2 16:57
 * Describe
 */

public interface BaseService {

    //    获取验证码
    @GET("api/Login")
    Observable<VerificationCodeBean> getVerificationCodeNet(@Query("accountPhone") String accountPhone);

    //    账号登录
    @FormUrlEncoded
    @POST("api/Login")
    Observable<LoginBean> loginUser(@Field("AccountPhone") String AccountPhone, @Field("code") String code, @Field("IP4") String IP4);

    //  添加案件
    @FormUrlEncoded
    @POST("api/Case")
    Observable<AddCaseBean> addCaseNet(@Header("token") String token, @Field("AddAccountId") int AddAccountId, @Field("CustomerName") String CustomerName, @Field("ReportNumber") String ReportNumber, @Field("OutDangerTime") String OutDangerTime,
                                       @Field("OutDangerAddress") String OutDangerAddress, @Field("CasualtiesType") String CasualtiesType, @Field("OutDangerDescription") String OutDangerDescription, @Field("IdCard") String IdCard);


    //  更新案件
    @FormUrlEncoded
    @POST("api/UpdatePost")
    Observable<BaseNetBean> updateCaseNet(@Header("token") String token, @Field("OrderId") int orderId,/* @Field("CaseNumber") String CaseNumber,*/ @Field("OutDangerTime") String OutDangerTime,
                                          @Field("OutDangerAddress") String OutDangerAddress, @Field("CasualtiesType") String CasualtiesType, @Field("OutDangerDescription") String OutDangerDescription);

    //     提交填写的信息
//    @Headers({"Content-type:application/json;charset=UTF-8"})
//    @POST("api/CaseUserInfoTwo")
//    Observable<SubmitInfoBean> submitInfoNet(@Body String body);

    //     提交填写的信息
    @FormUrlEncoded
    @POST("api/UpdateCaseUserInfo")
    Observable<SubmitInfoBean> submitInfoNet(@Header("token") String token, @Field("OrderId") int OrderId//派单ID
            , @Field("CaseId") int CaseId//案件ID
            , @Field("CustomerName") String CustomerName//姓名
            , @Field("Sex") String Sex//性别
            , @Field("Age") int Age//年龄
            , @Field("Phone") String Phone//手机号码
            , @Field("HouseholdRegisterAddress") String HouseholdRegisterAddress//户籍地址
            , @Field("HouseholdRegisterType") String HouseholdRegisterType//户籍性质
            , @Field("IDCard") String IDCard//身份证号
            , @Field("LiveInfo") String LiveInfo//居住地址
            , @Field("LiveStartTime") String LiveStartTime//入住时间
            , @Field("LiveEndTime") String LiveEndTime//至今时间
            , @Field("JobStartTime") String JobStartTime//工作开始时间
            , @Field("JobEndTime") String JobEndTime//工作结束时间
            , @Field("SocialSecurity") String SocialSecurity//是否有社保
            , @Field("LaborContract") String LaborContract//是否签订劳动合同
            , @Field("SalaryFrom") String SalaryFrom//工资发放时间
            , @Field("HospitalName") String HospitalName//医院名称
            , @Field("SectionBed") String SectionBed//科室及床号
            , @Field("AuditTime") String AuditTime//查勘时间
            , @Field("SurveyImg") String SurveyImg//人伤医院查勘表
            , @Field("HomesImg") String HomesImg//人院合影照
            , @Field("WardImg") String WardImg//病房照
            , @Field("BedsideCardImg") String BedsideCardImg//床头卡
            , @Field("CaseDataImg") String CaseDataImg//病例资料
            , @Field("XCTImg") String XCTImg//X光片或CT片
            , @Field("CostImg") String CostImg//费用清单照片
            , @Field("WholeBodyImg") String WholeBodyImg//伤者全身照
            , @Field("InjuryImg") String InjuryImg//伤者伤情局部照
            , @Field("IDCardImg") String IDCardImg//伤者身份证照片
            , @Field("NoticeLetterImg") String NoticeLetterImg//人伤索赔温馨告知函
    );


    //    获取后台添加的派单信息                                      CurrentPage PageSize
    @GET("api/SubmitOrder")
    Observable<NewDispatchBean> getNewDispatchNet(@Header("token") String token, @Query("accountId") int AccountId, @Query("currentPage") int CurrentPage, @Query("pageSize") int PageSize);


    //    更改后台添加的派单信息为已读
    @FormUrlEncoded
    @POST("api/SetRead")
    Observable<BaseNetBean> setNewDispatchReadNet(@Header("token") String token, @Field("orderId") int orderId);

    //    根据案件ID获取案件信息
    @GET("api/Case")
    Observable<NewDispatchCaseInfoBean> getNewDispatchCaseInfoNet(@Header("token") String token, @Query("caseId") int caseId);

    //    根据用户ID获取所有关联的派单信息
    @GET("api/GetAll")
    Observable<NewDispatchBean> getAllByAccountID(@Header("token") String token, @Query("accountId") int accountId, @Query("CurrentPage") int CurrentPage, @Query("PageSize") int PageSize);

    //    根据条件查询派单:比如身份证号，姓名，案件号
    @GET("api/CaseUserInfo")
    Observable<NewDispatchBean> getCaseUserInfoByOpition(@Header("token") String token, @Query("accountID") int accountID, @Query("currentPage") int currentPage, @Query("pageSize") int pageSize, @Query("value") String value);


    //    文件上传
    @Multipart
    @POST("api/UploadImage")
    Observable<UploadFileBean> uploadFileNet(@Header("token") String token, @Part("data") int data, @Part MultipartBody.Part file);


    //    身份校验和银行卡校验信息提交后台  ValidCode 1为身份证信息，2为银行卡信息
    @FormUrlEncoded
    @POST("api/CaseUserInfo")
    Observable<BaseNetBean> submitCheckInfoToServiceNet(@Header("token") String token, @Field("ValidCode") int ValidCode, @Field("IDCard") String IDCard, @Field("HouseholdRegister") String HouseholdRegister);

    //   上传图片的时间和经纬度
//    @FormUrlEncoded
    @POST("api/Geography")
    Observable<BaseNetBean> submitPictureInfoNet(@Header("token") String token, @Body List<PictureInfoBean> list);

    //    根据用户ID获取所有未签约的派单
    @GET("api/GetSignCase")
    Observable<SignBean> getSignByAccountID(@Header("token") String token, @Query("accountId") int accountId, @Query("currentPage") int currentPage, @Query("pageSize") int pageSize);


    //    提交签约文件
    @FormUrlEncoded
    @POST("api/UpdateSignCase")
    Observable<SignUploadBean> UpdateSignCaseNet(@Header("token") String token, @Field("CaseId") int CaseId, @Field("EntryApprovalForm") String EntryApprovalForm, @Field("AgentContract") String AgentContract, @Field("AgencyFees") String AgencyFees);

     //    根据用户ID获取报案编号
    @GET("api/GetReportNumber")
    Observable<ReportNumberBean> getReportNumberByAccountID(@Header("token") String token, @Query("accountId") int accountId);

  //    完结验证
    @FormUrlEncoded
    @POST("api/EndValid")
    Observable<BaseNetBean> endValidNet(@Header("token") String token, @Field("AccountId") int AccountId, @Field("IdCard") String IdCard);
}
