package com.maymeng.jinkeyulv.utils;


import android.app.Activity;

import com.maymeng.jinkeyulv.base.RxBaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by leijiaxq
 * Data       2016/12/20 11:07
 * Describe   ActivityStackUtil工具类，主要管理activity
 */

public class ActivityStackUtil {

    private static List<RxBaseActivity> activities = new ArrayList<>();

    public static void addActivity(RxBaseActivity activity) {
        activities.add(activity);
    }

    public static void removeActivity(RxBaseActivity activity) {
        activity.finish();
        activities.remove(activity);
    }

    public static void removeAllActivities() {

        try {
            for (Activity activity : activities)
                if (activity != null)
                    activity.finish();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            //System.exit(0);
        }

        //activities.clear();
    }

    public static void AppExit() {
        removeAllActivities();
//        System.exit(0);
    }

    /**清除其他的aciticyt*/
    public static void finisOtherActivity(){
        try {
            for (Activity activity : activities)
                if (activity != null){
                    if(!activity.getLocalClassName().equals("ui.activity.MainActivity")){
                        activity.finish();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
