package com.maymeng.jinkeyulv.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.api.RxBus;
import com.maymeng.jinkeyulv.base.BaseApplication;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.BaseNetBean;
import com.maymeng.jinkeyulv.bean.CheckIDCardImagetBean;
import com.maymeng.jinkeyulv.bean.CheckUserBean;
import com.maymeng.jinkeyulv.bean.LoginBean;
import com.maymeng.jinkeyulv.bean.RxBusBean;
import com.maymeng.jinkeyulv.bean.UploadFileBean;
import com.maymeng.jinkeyulv.ui.pop.SelectPicturePop;
import com.maymeng.jinkeyulv.utils.ImageUtil;
import com.maymeng.jinkeyulv.utils.SPUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/4/19 20:41
 * Describe    信息校验--身份验证
 */
public class InfoCheckTwoActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.confirm_tv)
    TextView mConfirmTv;
    @BindView(R.id.check_iv1)
    ImageView mCheckIv1;
    @BindView(R.id.check_iv2)
    ImageView mCheckIv2;

    private List<List<String>> mDatas = new ArrayList<>();

    private int mPostion = 0;   //item 位置

    private long mWaitTime;

    private int mCaseId = 0;

    //用于判断是传身份证背面照片还是反面照片
    private boolean mIsFirst = true;


    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    private List<String> mPath = new ArrayList<>();// 图片选择器的path集合

    public static final int REQUEST_CAMERA = 1;
    private String mImgPath;

    //用于图片上传失败后继续上传一次
    private boolean uploadErrorFirst = true;

    private String mImageIDCard1;
    private String mImageIDCard2;

    //用来保存验证的身份结果信息
    CheckIDCardImagetBean.DataBean mBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_infocheck_two;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("信息校验");
        mBean = new CheckIDCardImagetBean.DataBean();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_skip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_skip:
                Intent intent = new Intent(this, InfoCheckWebShebaoActivity.class);
                intent.putExtra("check_shebao_url", "");
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void loadData() {
        //用于--完结验证后，finish掉页面
        RxBus.getDefault().toObservable(RxBusBean.class)
                .compose(this.<RxBusBean>bindToLifecycle())
                .subscribe(new Action1<RxBusBean>() {
                    @Override
                    public void call(RxBusBean bean) {
                        if (bean.id == Constants.RXBUS_TWO) {
                            finish();
                        }
                    }
                });

        initList();

        initGallery();

    }

    private void initList() {

        List<String> list;
        for (int i = 0; i < 2; i++) {
            list = new ArrayList<>();
            mDatas.add(list);
        }

    }

    @OnClick(R.id.confirm_tv)
    void clickConfirm(View view) {
        mIsFirst = true;
        submitInfoToCheck();
    }

    @OnClick(R.id.check_iv1)
    void clickImage1(View view) {
        mPostion = 0;
        showSelectPicturePop();
    }

    @OnClick(R.id.check_iv2)
    void clickImage2(View view) {
        mPostion = 1;
        showSelectPicturePop();
    }


    private void showSelectPicturePop() {
        SelectPicturePop selectPicturePop = new SelectPicturePop(this, true);
        selectPicturePop.setShareListener(new SelectPicturePop.ShareListener() {
            @Override
            public void onItem(int position) {
                if (position == 1) {
                    //从相册选择
                    selectImage();
                } else if (position == 2) {
                    //拍照
                    selectCamera();
                }
            }
        });
        selectPicturePop.showAtLocation(mTitleTv, Gravity.BOTTOM, 0, 0);
    }


    //拍照获取图片
    private void selectCamera() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            // 所有权限请求被同意
                            mImgPath = createImagePath();
//                            if (!imageFile.exists()) {
//                                return;
//                            }
                            // 指定调用相机拍照后照片的储存路径
                            File imgFile = new File(mImgPath);
                            Uri imgUri = null;
                            if (Build.VERSION.SDK_INT >= 24) {
                                //如果是7.0或以上，使用getUriForFile()获取文件的Uri
                                imgUri = FileProvider.getUriForFile(InfoCheckTwoActivity.this, "com.maymeng.jinkeyulv.fileprovider", imgFile);
                            } else {
                                imgUri = Uri.fromFile(imgFile);
                            }

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                            startActivityForResult(intent, REQUEST_CAMERA);

                        } else {
                            ToastUtil.showShort("权限拒绝");
                        }
                    }
                });

    }

    private String createImagePath() {
        String imagePath = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
        if (TextUtils.isEmpty(imagePath)) {
            File externalFilesDir = getExternalFilesDir("Caches");
            imagePath = externalFilesDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        }


        return imagePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (TextUtils.isEmpty(mImgPath)) {
                    ToastUtil.showShort("图片路径有误！");
                    return;
                }
                List<String> list = mDatas.get(mPostion);
                list.clear();
                list.add(mImgPath);

                if (mPostion == 0) {
                    mCheckIv1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageUtil.getInstance().displayImage(InfoCheckTwoActivity.this, mImgPath, mCheckIv1);
                } else {
                    mCheckIv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    ImageUtil.getInstance().displayImage(InfoCheckTwoActivity.this, mImgPath, mCheckIv2);
                }
            }
        }
    }

    //从相册中选择图片
    private void selectImage() {
        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            // 所有权限请求被同意

                            GalleryPickConfig();

                        } else {
                            ToastUtil.showShort(InfoCheckTwoActivity.this, "权限拒绝");
                        }
                    }
                });

    }

    //配置gallerypick图片选择器
    private void GalleryPickConfig() {

        if (mDatas.size() > mPostion) {
            List<String> list = mDatas.get(mPostion);
            mPath.clear();
            mPath.addAll(list);

            galleryConfig = new GalleryConfig.Builder()
                    .imageLoader(new ImageUtil.GlideImageLoader())    // ImageLoader 加载框架（必填）
                    .iHandlerCallBack(iHandlerCallBack
                            /*new IHandlerCallBack() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(List<String> photoList) {
                            List<String> list = mDatas.get(mPostion);
                            list.clear();
                            mPath.clear();
                            for (String s : photoList) {
                                mPath.add(s);
                            }
                            list.addAll(mPath);
                            mAdapter.notifyItemChanged(mPostion);
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onError() {

                        }
                    }*/)     // 监听接口（必填）
                    .provider("com.maymeng.jinkeyulv.fileprovider")   // provider(必填)
                    .pathList(mPath)                         // 记录已选的图片
                    .multiSelect(true, 1)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
//                .maxSize(3)                             // 配置多选时 的多选数量。    默认：9
//                    .isOpenCamera(true)
                    .isShowCamera(false)                     // 是否现实相机按钮  默认：false
                    .filePath("/laws/Pictures")          // 图片存放路径
                    .build();
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(InfoCheckTwoActivity.this);
        }

    }

    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<String> photoList) {

                List<String> list = mDatas.get(mPostion);
                list.clear();
               /* mPath.clear();
                for (String s : photoList) {
                    mPath.add(s);
                }
                list.addAll(mPath);*/

                if (photoList.size() > 0) {
                    String str = photoList.get(0);
                    list.add(str);

                    if (mPostion == 0) {
                        mCheckIv1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageUtil.getInstance().displayImage(InfoCheckTwoActivity.this, str, mCheckIv1);
                    } else {
                        mCheckIv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageUtil.getInstance().displayImage(InfoCheckTwoActivity.this, str, mCheckIv2);
                    }
                } else {
                    if (mPostion == 0) {
                        mCheckIv1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        mCheckIv1.setImageResource(R.drawable.icon_34);
//                        ImageUtil.getInstance().displayImage(InfoCheckTwoActivity.this, R.drawable.icon_34, mCheckIv1);
                    } else {
                        mCheckIv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        mCheckIv2.setImageResource(R.drawable.icon_35);
//                        ImageUtil.getInstance().displayImage(InfoCheckTwoActivity.this, R.drawable.icon_35, mCheckIv2);
                    }
                }

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onError() {
            }
        };

    }

    //已经上传的图片数量
    int uploadNUM = 0;

    //将要上传的图片数量
    int uploadNumber = 2;

    /*  //上传的图片所在位置的集合；
      List<Integer> mListIndex;
      //上传的图片地址的集合
      List<String> mListPath;
  */
    //上传图片时判断条件信息
    private void submitInfoToCheck() {

        if (mDatas.get(0).size() == 0) {
            ToastUtil.showShort("请上传身份证正面照片");
            return;
        }
        if (mDatas.get(1).size() == 0) {
            ToastUtil.showShort("请上传身份证背面照片");
            return;
        }

        showProgressDialog("正在提交...");

//        BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        List<String> list = mDatas.get(0);
        uploadFileNet(0, list.get(0));
//            }
//        }, 5000);

    }

    private void uploadFileNet(int flag, String path) {
        //构建要上传的文件
        File file = new File(path);
        if (!file.exists()) {
            hideProgressDialog();
            ToastUtil.showShort("图片有误");
            return;
        }
        //构建要上传的文件
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestFile =RequestBody.create(MediaType.parse("image/jpg"),file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

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
                .uploadFileNet(bean.Token, bean.AccountId + "", flag, body)
                .compose(this.<UploadFileBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadFileBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        showNetError();
//                        uploadNUM++;
//                        ToastUtil.showShort("上传图片出错");
//                        ToastUtil.showLong(e.toString());

                        //图片上传失败后，继续上传该图片一次，如果再次失败，不再上传该张图片
                        if (uploadErrorFirst) {
                            uploadErrorFirst = false;

                            if (mDatas.size() > uploadNUM) {
                                List<String> list = mDatas.get(uploadNUM);
                                uploadFileNet(uploadNUM, list.get(0));
                            }
                        } else {
                            uploadNUM++;
                            ToastUtil.showShort("上传图片出错");

                            //图片全部上传完成
                            if (uploadNUM == uploadNumber) {
                                judeSubmitInfoNet();
                            } else {
                                if (mDatas.size() > uploadNUM) {
                                    List<String> list = mDatas.get(uploadNUM);
                                    uploadFileNet(uploadNUM, list.get(0));
                                }
                            }
                        }
                    }

                    @Override
                    public void onNext(UploadFileBean bean) {
//                        ToastUtil.showShort("上传成功");

                        if (Constants.OK.equals(bean.StateCode)) {
                            uploadNUM++;
                            uploadErrorFirst = true;

                            if (Constants.TOKEN_ERROR.equals(bean.ResponseMessage)) {
                                hideProgressDialog();
                                ToastUtil.showLong(Constants.TOKEN_RELOGIN);
//                                SPUtil.clear(SignUploadActivity.this);
                                SPUtil.put(InfoCheckTwoActivity.this, Constants.ACCOUNT_LOGIN, false);
                                Intent intent = new Intent(InfoCheckTwoActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                finishTask(bean);
                            }

                        } else {
                            hideProgressDialog();
                            ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        }
                    }
                });
    }

    //上传图片成功
    private void setUploadFileBeanData(UploadFileBean bean) {
        if (bean.ResponseData != null) {
            int data = Integer.valueOf(bean.ResponseData.Data);
            if (data == 1) {
                mImageIDCard2 = bean.ResponseData.ImgUrl;
            } else {
                mImageIDCard1 = bean.ResponseData.ImgUrl;
            }
        }

        //图片全部上传完成
        if (uploadNUM == uploadNumber) {
            judeSubmitInfoNet();
        } else {
            if (mDatas.size() > uploadNUM) {
                List<String> list = mDatas.get(uploadNUM);
                uploadFileNet(uploadNUM, list.get(0));
            }
        }
    }

    private void judeSubmitInfoNet() {
        checkIDCardFrontNet("idcard_front", Constants.BASE_URL + mImageIDCard1);
    }


    //调用接口进行信息核验
    private void checkIDCardFrontNet(String cmd, String imageUrl) {
        RetrofitHelper.getInfoCheck3Service()
                .checkIDCardImagetNet(Constants.CHECKINFO_IDCARD_IMAGE_APIX_KEY, cmd, imageUrl)
                .compose(this.<CheckIDCardImagetBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckIDCardImagetBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        ToastUtil.showShort(Constants.CHECK_ERROR);
//                        showNetError();
                    }

                    @Override
                    public void onNext(CheckIDCardImagetBean bean) {
                        if (bean != null) {
                            finishTask(bean);
                           /* long l = System.currentTimeMillis();
                            if (l - mWaitTime >= Constants.WAIT_TIME) {
                                hideProgressDialog();
                                finishTask(bean);
                            } else {
                                BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgressDialog();
                                        finishTask(bean);
                                    }
                                }, Constants.WAIT_TIME);
                            }*/
                        } else {
                            hideProgressDialog();
                            ToastUtil.showShort(Constants.CHECK_ERROR);
                        }

                    }
                });
    }


    //提交身份证验证结果信息给服务器
    private void submitCheckInfoToServiceNet() {
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

       /* if (caseID == 0) {
            if (mBean != null) {
                caseID = mBean.CaseId;
                CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
                if (checkUserBean != null) {
                    checkUserBean.CaseId = caseID;
                }
                BaseApplication.getInstance().setCheckUserBean(checkUserBean);
            }

            if (caseID == 0) {
                caseID = mCaseId;
                CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();
                if (checkUserBean != null) {
                    checkUserBean.CaseId = caseID;
                }
                BaseApplication.getInstance().setCheckUserBean(checkUserBean);
            }
        }*/

        CheckUserBean checkUserBean = BaseApplication.getInstance().getCheckUserBean();

        RetrofitHelper.getBaseApi()
                .uploadIdCardInfo(bean.Token, bean.AccountId + "", 0, checkUserBean.CaseId, mBean.name,
                        mBean.number, mBean.nation, mBean.address, mBean.sex, mBean.date1, mBean.date2, mBean.office)
                .compose(this.<BaseNetBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseNetBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();
                        ToastUtil.showShort(Constants.CHECK_ERROR);
                    }

                    @Override
                    public void onNext(BaseNetBean baseNetBean) {
                        if (Constants.OK.equals(baseNetBean.StateCode)) {
                            if (Constants.TOKEN_ERROR.equals(baseNetBean.ResponseMessage)) {
                                hideProgressDialog();
                                ToastUtil.showLong(Constants.TOKEN_RELOGIN);
                                SPUtil.clear(InfoCheckTwoActivity.this);
                                Intent intent = new Intent(InfoCheckTwoActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                long l = System.currentTimeMillis();
                                if (l - mWaitTime >= Constants.WAIT_TIME) {
                                    hideProgressDialog();
                                    Intent intent = new Intent(InfoCheckTwoActivity.this, InfoCheckTwoResultActivity.class);
                                    startActivity(intent);
                                } else {
                                    BaseApplication.getInstance().mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideProgressDialog();
                                            Intent intent = new Intent(InfoCheckTwoActivity.this, InfoCheckTwoResultActivity.class);
                                            startActivity(intent);
                                        }
                                    }, Constants.WAIT_TIME);
                                }
                            }


//                            hideProgressDialog();
//                            Intent intent = new Intent(InfoCheckOneActivity.this, InfoCheckOneResultActivity.class);
//                            startActivity(intent);
                        } else {
                            hideProgressDialog();
                            ToastUtil.showShort(TextUtils.isEmpty(baseNetBean.ResponseMessage) ? Constants.CHECK_ERROR : baseNetBean.ResponseMessage);
                        }
                    }
                });
    }


    @Override
    public void finishTask(BaseBean bean) {
        if (bean instanceof CheckIDCardImagetBean) {
            setCheckIDCardImagetBeanDate((CheckIDCardImagetBean) bean);
        } else if (bean instanceof UploadFileBean) {
            setUploadFileBeanData((UploadFileBean) bean);
        }
    }

    private void setCheckIDCardImagetBeanDate(CheckIDCardImagetBean bean) {
        if (bean.state == 1) {
            if (bean.data != null) {
                if (mIsFirst) {
                    mIsFirst = false;
                    mBean.name = bean.data.name;
                    mBean.number = bean.data.number;
                    mBean.nation = bean.data.nation;
                    mBean.address = bean.data.address;
                    mBean.sex = bean.data.sex;

                    checkIDCardFrontNet("idcard_back", Constants.BASE_URL +mImageIDCard2);

                } else {
                    if (!TextUtils.isEmpty(bean.data.date1) && bean.data.date1.length() == 8) {
                        StringBuilder sb = new StringBuilder();
                        String substring1 = bean.data.date1.substring(0, 4);
                        String substring2 = bean.data.date1.substring(4, 6);
                        String substring3 = bean.data.date1.substring(6, 8);
                        sb.append(substring1).append("-").append(substring2).append("-").append(substring3);

                        mBean.date1 = sb.toString();
                    } else {
                        mBean.date1 = bean.data.date1;
                    }
                    if (!TextUtils.isEmpty(bean.data.date2) && bean.data.date2.length() == 8) {
                        StringBuilder sb = new StringBuilder();
                        String substring1 = bean.data.date2.substring(0, 4);
                        String substring2 = bean.data.date2.substring(4, 6);
                        String substring3 = bean.data.date2.substring(6, 8);
                        sb.append(substring1).append("-").append(substring2).append("-").append(substring3);

                        mBean.date2 = sb.toString();
                    } else {

                        mBean.date2 = bean.data.date2;
                    }
                    mBean.office = bean.data.office;

                    submitCheckInfoToServiceNet();
                }

            }

        } else {
            hideProgressDialog();
            ToastUtil.showShort(TextUtils.isEmpty(bean.message) ? Constants.CHECK_ERROR : bean.message);
        }
    }


}
