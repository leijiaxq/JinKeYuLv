package com.maymeng.jinkeyulv.api;

/**
 * Created by hcc on 2016/11/20 21:32
 * 100332338@qq.com
 * <p>
 * API常量类
 */

public class Constants {

    //        public static final String BASE_URL = "http://www.qiuhongliang.com/";
//    public static final String BASE_URL = "http://192.168.0.158/";
    public static final String BASE_URL = "http://apptest1.jk-yl.com:8086/";
//    public static final String BASE_URL = "http://apptest.jk-yl.com/";

    //身份证，手机号，银行卡校验地址
    public static final String BASE_URL_INFO_CHECK = "http://v.apix.cn/";

    //社保，公积金查询地址
    public static final String BASE_URL_INFO_CHECK2 = "http://e.apix.cn/";

    //信息核验头参数
    public static final String CHECKINFO_IDCARD_APIX_KEY = "10492c22c5fd4509704c1df9796f5e15";
    //社保头参数
    public static final String CHECKINFO_SHEBAO_APIX_KEY = "11f7ee3b73da474c6517568f26540e43";
    //公积金头参数
    public static final String CHECKINFO_GJJ_APIX_KEY = "92073c8fa83c46e176fcd083e185a1d8";

    //公积金头参数
    public static final String CHECKINFO_PHONE_APIX_KEY = "f5c2b4da4d04457451b21d14cb1ae8ec";


    //社保查询H5回调地址
    public static final String SHEBAO_CALLBACK_URL = "http://apptest1.jk-yl.com:8085/shebaoRollBack.aspx";
    //    public static final String SHEBAO_CALLBACK_URL = "http://www.qiuhongliang.com/Result.aspx";
    public static final String SHEBAO_SUCCESS_URL = "http://www.qiuhongliang.com/xinxi/xinxi-cg.html";
    public static final String SHEBAO_FAILED_URL = "http://www.qiuhongliang.com/xinxi/xinxi-sb.html";

    //公积金查询H5回调地址
    public static final String GJJ_CALLBACK_URL = "http://apptest1.jk-yl.com:8085/gongjijinRollBack.aspx";
    //    public static final String GJJ_CALLBACK_URL = "http://www.qiuhongliang.com/Result.aspx";
    public static final String GJJ_SUCCESS_URL = "http://www.qiuhongliang.com/xinxi/xinxi-cg.html";
    public static final String GJJ_FAILED_URL = "http://www.qiuhongliang.com/xinxi/xinxi-sb.html";

    //手机运营商数据查询H5回调地址
    public static final String PHONE_CALLBACK_URL = "http://apptest1.jk-yl.com:8085/phonerollBack.aspx";
    //    public static final String PHONE_CALLBACK_URL = "http://www.qiuhongliang.com/Result.aspx";
    public static final String PHONE_SUCCESS_URL = "http://www.qiuhongliang.com/xinxi/xinxi-cg.html";
    public static final String PHONE_FAILED_URL = "http://www.qiuhongliang.com/xinxi/xinxi-sb.html";


    public static final String CHECK_RESUL_COMPLETED_URL = "http://china.huanqiu.com/";
    public static final String CHECK_RESULT_NEXT_URL = "http://music.baidu.com/";


    public static final String OK = "200";

    public static final String FAILURE = "数据有误！";
//    public static final String FAILURE = "请检查网络设置";

    public static final String ERROR = "数据错误";

    public static final String CHECK_ERROR = "校验错误";


    public static final String TOKEN_ERROR = "缺少token";
    public static final String TOKEN_RELOGIN = "登录信息过期，请重新登录";

    public static final int SIZE = 15;

    //loading等待时间
    public static final long WAIT_TIME = 1000;
    public static final long WAIT_TIME_LOADMORE = 500;


    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;

    public static final int TYPE_FOOT = 4;


    /**
     * RxBus传值使用
     */
    public static final int RXBUS_ONE = 1;
    public static final int RXBUS_TWO = 2;
    public static final int RXBUS_THREE = 3;

    /**
     * 用户信息存储键 sharePreferent
     */
    public static final String ACCOUNT_LOGIN = "AccountLogin";  //用于判断是否登录

    public static final String ACCOUNT_ID = "AccountId";  //用户ID
    public static final String ACCOUNT_NAME = "AccountName";//用户姓名
    public static final String ACCOUNT_TOKEN = "AccountToten";//用户token

}
