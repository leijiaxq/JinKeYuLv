package com.maymeng.jinkeyulv.api;

import com.maymeng.jinkeyulv.bean.CheckBankCardBean;
import com.maymeng.jinkeyulv.bean.CheckIDCardBean;
import com.maymeng.jinkeyulv.bean.CheckPhoneBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by  leijiaxq
 * Date        2017/4/25 14:16
 * Describe
 */

public interface InfoCheckService {

    //    身份校验
    @GET("apixcredit/idcheck/idcard")
    Observable<CheckIDCardBean> checkIDCardNet(@Header("apix-key")String apix_key, @Query("type")String type, @Query("cardno")String cardno, @Query("name")String name);
// @FormUrlEncoded
//    @POST("apixcredit/idcheck/idcard")
//    Observable<CheckIDCardBean> checkIDCardNet(@Header("apix-key")String apix_key, @Field("type")String type, @Field("cardno")String cardno, @Field("name")String name);

    //    手机实名校验
    @GET("apixcredit/idcheck/mobile")
    Observable<CheckPhoneBean> checkPhoneNet(@Header("apix-key")String apix_key, @Query("type")String type, @Query("phone")String phone, @Query("name")String name, @Query("cardno")String cardno);


    //    银行卡实名校验
    @GET("apixcredit/idcheck/bankcard")
    Observable<CheckBankCardBean> checkBankCardNet(@Header("apix-key")String apix_key, @Query("type")String type, @Query("bankcardno")String bankcardno, @Query("name")String name, @Query("idcardno")String idcardno, @Query("phone")String phone);



}
