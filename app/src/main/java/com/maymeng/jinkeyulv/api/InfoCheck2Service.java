package com.maymeng.jinkeyulv.api;

import com.maymeng.jinkeyulv.bean.PagePathBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by  leijiaxq
 * Date        2017/4/25 14:16
 * Describe
 */

public interface InfoCheck2Service {

    //   获取公积金H5接口地址
    @GET("apixanalysis/gjj/page")
    Observable<PagePathBean> getGJJPageNet(@Header("apix-key") String apix_key, @Query("callback_url") String callback_url, @Query("success_url") String success_url, @Query("failed_url") String failed_url, @Query("show_nav_bar") boolean show_nav_bar);

     //   获取社保H5接口地址
    @GET("/apixanalysis/shebao/page")
    Observable<PagePathBean> getSheBaoPageNet(@Header("apix-key") String apix_key, @Query("callback_url") String callback_url, @Query("success_url") String success_url, @Query("failed_url") String failed_url, @Query("show_nav_bar") boolean show_nav_bar);


     //   获取运营商数据分析H5接口地址
    @GET("/apixanalysis/mobile/yys/phone/carrier/page")
    Observable<PagePathBean> getPhonePageNet(@Header("apix-key") String apix_key, @Query("callback_url") String callback_url, @Query("success_url") String success_url, @Query("failed_url") String failed_url, @Query("name") String name, @Query("cert_no") String cert_no/*, @Query("contact_list") String contact_list, @Query("show_nav_bar") boolean show_nav_bar, @Query("phone_no") String phone_no*/);



}
