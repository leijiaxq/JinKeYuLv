package com.maymeng.jinkeyulv.api;

import com.maymeng.jinkeyulv.bean.CheckIDCardImagetBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by  leijiaxq
 * Date        2017/4/25 14:16
 * Describe
 */

public interface InfoCheck3Service {

    //身份证图像信息查询
    @FormUrlEncoded
    @POST("apixlab/idcardrecog/idcardurl")
    Observable<CheckIDCardImagetBean> checkIDCardImagetNet(@Header("apix-key") String apix_key, @Field("cmd") String cmd, @Field("imgurl") String imgurl);


}
