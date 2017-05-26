package com.maymeng.jinkeyulv.ui.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.CheckUserBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.PictureInfoBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.ui.pop.ReadDataPop;
import com.maymeng.jinkeyulv.utils.DateUtil;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.media.ExifInterface.TAG_DATETIME;
import static android.media.ExifInterface.TAG_GPS_LATITUDE;
import static android.media.ExifInterface.TAG_GPS_LATITUDE_REF;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 22:43
 * Describe    信息校验--相册数据读取页
 */
public class InfoCheckFourActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.end_tv)
    TextView mEndTv;
    @BindView(R.id.data_fetch_tv)
    TextView mDataFetchTv;

    private long mYearMillis = (long) 60 * 60 * 24 * 365 * 1000;

    private List<PictureInfoBean> mDatas = new ArrayList<>();
    private String mIDCard;
    private ReadDataPop mReadDataPop;

    @Override
    public int getLayoutId() {
        return R.layout.activity_infocheck_four;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("信息校验");
    }

    @Override
    public void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
        if (checkUserBean != null) {
            mIDCard = checkUserBean.IDCard;
        }
        mIDCard = "431126197912101210";
    }

    //数据读取
    @OnClick(R.id.data_fetch_tv)
    void clickDataFetch(View view) {

        mReadDataPop = new ReadDataPop(this);
        mReadDataPop.setOnPopListenter(new ReadDataPop.OnPopListenter() {
            @Override
            public void onConfirm() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPhotoInfo();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                submitPictureInfoNet();
                            }
                        });
                    }
                }).start();
            }
        });

        mReadDataPop.showAtLocation(mTitleTv, Gravity.CENTER, 0, 0);

    }


    @OnClick(R.id.end_tv)
    void clickEnd(View view) {
        //用于--完结验证后，finish掉页面
        RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO, new BaseBean()));
        finish();
    }

    private void submitPictureInfoNet() {

        if (mDatas != null && mDatas.size() != 0) {
            LoginBean.ResponseDataBean bean = BaseApplication.getInstance().getLoginBean();
            if (bean == null) {
                bean = new LoginBean.ResponseDataBean();
                int account_id = (int) SPUtil.get(this, Constants.ACCOUNT_ID, 0);
                String account_name = (String) SPUtil.get(this, Constants.ACCOUNT_NAME, "");
                String account_token = (String) SPUtil.get(this, Constants.ACCOUNT_TOKEN, "");
                bean.AccountId = account_id;
                bean.AccountName = account_name;
                bean.Token = account_token;
                BaseApplication.getInstance().setLoginBean(bean);
            }
            RetrofitHelper.getBaseApi()
                    .submitPictureInfoNet(bean.Token,mDatas)
                    .compose(this.<BaseNetBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BaseNetBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mReadDataPop.dismiss();
                            showNetError();
                        }

                        @Override
                        public void onNext(BaseNetBean baseNetBean) {
                            mReadDataPop.dismiss();

                            if (Constants.TOKEN_ERROR.equals(baseNetBean.ResponseMessage)) {
                                ToastUtil.showLong(Constants.TOKEN_RELOGIN);
                                Intent intent = new Intent(InfoCheckFourActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtil.showShort(TextUtils.isEmpty(baseNetBean.ResponseMessage) ? Constants.CHECK_ERROR : baseNetBean.ResponseMessage);
                                //用于--完结验证后，finish掉页面
                                RxBus.getDefault().post(new RxBusBean(Constants.TYPE_TWO, new BaseBean()));
                                finish();
                            }

                        }
                    });
        } else {
            mReadDataPop.dismiss();
            ToastUtil.showShort("没有数据供读取");
        }
    }


    //读取相册中照片的信息
    public void getPhotoInfo() {

//        List<String> list = new ArrayList<>();

        // 获取SDcard卡的路径
        String sdcardPath = Environment.getExternalStorageDirectory().toString();

        ContentResolver mContentResolver = getContentResolver();

        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA}, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media._ID + " DESC");
        while (mCursor.moveToNext()) {
            // 打印LOG查看照片ID的值
//            long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
//            Log.i("Media_ID=", id + "");
            // 过滤掉不须要的图片。仅仅获取拍照后存储照片的相冊里的图片
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            if (path.startsWith(sdcardPath + "/DCIM/100MEDIA") || path.startsWith(sdcardPath + "/DCIM/Camera/")
//                    || path.startsWith(sdcardPath + "DCIM/100Andro")) {
            getImageExifInterfaceData(path);
//            }
        }
        mCursor.close();
    }

    /**
     * TAG_DATETIME  时间日期
     * 　　TAG_FLASH      闪光灯
     * 　　TAG_GPS_LATITUDE     纬度
     * 　　TAG_GPS_LATITUDE_REF    纬度参考
     * 　　TAG_GPS_LONGITUDE         经度
     * 　　TAG_GPS_LONGITUDE_REF     经度参考
     * 　　TAG_IMAGE_LENGTH          图片长
     * 　　TAG_IMAGE_WIDTH          图片宽
     * 　　TAG_MAKE             设备制造商
     * 　　TAG_MODEL          设备型号
     * 　　TAG_ORIENTATION            方向
     * 　　TAG_WHITE_BALANCE        白平衡
     * <p>
     * /**
     * 获取照片其他附属属性
     */
    public void getImageExifInterfaceData(String path) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
            String datetime = exifInterface.getAttribute(TAG_DATETIME);// 拍摄时间
//            String deviceName = exifInterface.getAttribute(ExifInterface.TAG_MAKE);// 设备品牌
//            String deviceModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL); // 设备型号
            if (!TextUtils.isEmpty(datetime)) {
                long millis = DateUtil.string2Millis(datetime, "yyyy:MM:dd HH:mm:ss");

                long currentTimeMillis = System.currentTimeMillis();

                //这张照片的拍摄时间是在一年内
                if (currentTimeMillis - millis < mYearMillis) {

                    String latValue = exifInterface.getAttribute(TAG_GPS_LATITUDE);
                    String lngValue = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    String latRef = exifInterface.getAttribute(TAG_GPS_LATITUDE_REF);
                    String lngRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                    if (latValue != null && latRef != null && lngValue != null && lngRef != null) {
                        try {

                            PictureInfoBean bean = new PictureInfoBean();

                            float output1 = convertRationalLatLonToFloat(latValue, latRef);
                            float output2 = convertRationalLatLonToFloat(lngValue, lngRef);
                            String date = DateUtil.millis2String(millis);
                            bean.ImgDate = date;
                            bean.Latitude = output1;
                            bean.Longitude = output2;
                            bean.IDCard = mIDCard;
                            mDatas.add(bean);

                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 目前想存储Double类型坐标，但是通过getAttribute(String tag)取出来的内容形如：
     * 112/1,39/1,288172/3278,其实就是“度分秒”拆分后，分母除以1的结果
     *
     * @param rationalString
     * @param ref
     * @return
     */
    private static float convertRationalLatLonToFloat(
            String rationalString, String ref) {

        String[] parts = rationalString.split(",");

        String[] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim())
                / Double.parseDouble(pair[1].trim());

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals("S") || ref.equals("W"))) {
            return (float) -result;
        }
        return (float) result;
    }
}
