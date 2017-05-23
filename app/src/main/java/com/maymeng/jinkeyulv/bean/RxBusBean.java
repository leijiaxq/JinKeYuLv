package com.maymeng.jinkeyulv.bean;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 18:30
 * Describe
 */

public class RxBusBean {


    //    public static final int UPLOAD_FINISH = 1;
//    public static final int QUITLOGIN = 2;
//    public static final int CROP_MESSAGE = 3;
    public int id;
    public Object obj;

    public RxBusBean() {
    }

    public RxBusBean(int id, Object obj) {
        this.id = id;
        this.obj = obj;
    }

}
