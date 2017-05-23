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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maymeng.jinkeyulv.R;
import com.maymeng.jinkeyulv.api.Constants;
import com.maymeng.jinkeyulv.api.RetrofitHelper;
import com.maymeng.jinkeyulv.base.RxBaseActivity;
import com.maymeng.jinkeyulv.bean.BaseBean;
import com.maymeng.jinkeyulv.bean.UploadFileBean;
import com.maymeng.jinkeyulv.ui.adapter.SignUploadAdapter;
import com.maymeng.jinkeyulv.ui.pop.SelectPicturePop;
import com.maymeng.jinkeyulv.utils.ImageUtil;
import com.maymeng.jinkeyulv.utils.ToastUtil;
import com.maymeng.jinkeyulv.bean.SignUploadBean;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by  leijiaxq
 * Date        2017/5/12 16:56
 * Describe    签约文件上传页
 */
public class SignUploadActivity extends RxBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<List<String>> mDatas = new ArrayList<>();
    private List<StringBuilder> mListStringBuider = new ArrayList<>();

    private List<String> mDatas0;  //名称

    private SignUploadAdapter mAdapter;

    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    private int mPostion = 0;   //item 位置
    private int mPostion2 = 0;//item中iamge位置

    private List<String> mPath = new ArrayList<>();// 图片选择器的path集合

    public static final int REQUEST_CAMERA = 1;
    private String mImgPath;
    private int mCaseId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_upload;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mTitleTv.setText("上传合同");
        mCaseId = getIntent().getIntExtra("CaseId", 0);

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        initList();
//        setWriteInfoBeanImg();

        initAdapter();
//        initGallery();

        initStringBuider();
    }

    //用来装上传用的Image地址数据  String
    private void initStringBuider() {
        StringBuilder builder;
        for (int i = 0; i < 2; i++) {
            builder = new StringBuilder();
            mListStringBuider.add(builder);
        }
    }

    //把Image切分设置到对应的list集合中
//    private void setWriteInfoBeanImg() {
//        WriteInfoBean bean = BaseApplication.getInstance().getWriteInfoBean();
//        if (bean != null) {
//            if (!TextUtils.isEmpty(bean.SurveyImg)) {
//                String[] split = bean.SurveyImg.split(";");
//                List<String> list = mDatas.get(0);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.HomesImg)) {
//                String[] split = bean.HomesImg.split(";");
//                List<String> list = mDatas.get(1);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.WardImg)) {
//                String[] split = bean.WardImg.split(";");
//                List<String> list = mDatas.get(2);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.BedsideCardImg)) {
//                String[] split = bean.BedsideCardImg.split(";");
//                List<String> list = mDatas.get(3);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.CaseDataImg)) {
//                String[] split = bean.CaseDataImg.split(";");
//                List<String> list = mDatas.get(4);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.XCTImg)) {
//                String[] split = bean.XCTImg.split(";");
//                List<String> list = mDatas.get(5);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.CostImg)) {
//                String[] split = bean.CostImg.split(";");
//                List<String> list = mDatas.get(6);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.WholeBodyImg)) {
//                String[] split = bean.WholeBodyImg.split(";");
//                List<String> list = mDatas.get(7);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.InjuryImg)) {
//                String[] split = bean.InjuryImg.split(";");
//                List<String> list = mDatas.get(8);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.IDCardImg)) {
//                String[] split = bean.IDCardImg.split(";");
//                List<String> list = mDatas.get(9);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//            if (!TextUtils.isEmpty(bean.NoticeLetterImg)) {
//                String[] split = bean.NoticeLetterImg.split(";");
//                List<String> list = mDatas.get(10);
//                for (int i = 0; i < split.length; i++) {
//                    list.add(split[i]);
//                }
//            }
//
//        }
//    }

    private void initAdapter() {

        mAdapter = new SignUploadAdapter(this, mDatas0, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SignUploadAdapter.OnItemClickListener() {
            @Override
            public void onItemImageClick(View view, int position, int position2) {
                mPostion = position;
                mPostion2 = position2;
                clickItemImage(view);
            }

            @Override
            public void onItemdeleteClick(int position, int position2) {
                mPostion = position;
                mPostion2 = position2;
                if (mDatas.size() > position) {
                    List<String> list = mDatas.get(position);
                    list.remove(position2);
                    mAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onItemBottonClick() {
//                ToastUtil.showShort(WriteInfoFiveActivity.this, "完结");
               /* RxBus.getDefault().post(new RxBusBean(Constants.TYPE_ONE, new BaseBean()));
                finish();*/

                submitInfoToCheck();

            }
        });
    }


    //点击了图片，判断是选择照片，还是进入图片展示页
    private void clickItemImage(View view) {
        if (mDatas.size() > mPostion) {
            List<String> list = mDatas.get(mPostion);
            if (list.size() <= mPostion2) {
//                selectImage();
                showSelectPicturePop();
            } else {
                showImage(view, list, mPostion2);
            }
        }
    }

    //展示图片
    private void showImage(View view, List<String> list, int position2) {
//        ToastUtil.showShort(this, "展示图片" + position2);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            builder.append(str).append(";");
        }

        Intent intent = new Intent(SignUploadActivity.this, ShowPictureActivity.class);
        intent.putExtra("ImageUrl", builder.toString());
        intent.putExtra("ImagePosition", position2);
        startActivity(intent);
    }


    private void showSelectPicturePop() {
        SelectPicturePop selectPicturePop = new SelectPicturePop(this,false);
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
                                imgUri = FileProvider.getUriForFile(SignUploadActivity.this, "com.maymeng.jinkeyulv.fileprovider", imgFile);
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

        return imagePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                List<String> list = mDatas.get(mPostion);

                list.add(mImgPath);
                mAdapter.notifyItemChanged(mPostion);

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
                            ToastUtil.showShort(SignUploadActivity.this, "权限拒绝");
                        }
                    }
                });

    }

    //配置gallerypick图片选择器
    private void GalleryPickConfig() {


        if (mDatas.size() > mPostion) {
            List<String> list = mDatas.get(mPostion);
//            int num = 3 - list.size();
            mPath.clear();
            mPath.addAll(list);

            galleryConfig = new GalleryConfig.Builder()
                    .imageLoader(new ImageUtil.GlideImageLoader())    // ImageLoader 加载框架（必填）
                    .iHandlerCallBack(new IHandlerCallBack() {
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
                    })     // 监听接口（必填）
                    .provider("com.maymeng.jinkeyulv.fileprovider")   // provider(必填)
                    .pathList(mPath)                         // 记录已选的图片
                    .multiSelect(true, 4)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
//                .maxSize(3)                             // 配置多选时 的多选数量。    默认：9
//                    .isOpenCamera(true)
                    .isShowCamera(false)                     // 是否现实相机按钮  默认：false
                    .filePath("/laws/Pictures")          // 图片存放路径
                    .build();
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(SignUploadActivity.this);
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
        };

    }

    private void initList() {
        mDatas0 = new ArrayList<>();

        mDatas0.add("进件审批表");
        mDatas0.add("代理合同");

        List<String> list;
        for (int i = 0; i < 2; i++) {
            list = new ArrayList<>();
            mDatas.add(list);
        }

    }

    private void submitInfoToCheck() {

        showProgressDialog("正在提交...");
        judeFileNeedUpload();

//        submitInfoNet();
    }


    private void submitInfoNet() {
        StringBuilder builder1 = mListStringBuider.get(0);
        StringBuilder builder2 = mListStringBuider.get(1);

        RetrofitHelper.getBaseApi()
                .UpdateSignCaseNet(mCaseId,builder1.toString(),builder2.toString())
                .compose(this.<SignUploadBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignUploadBean>() {
                    @Override
                    public void onCompleted() {
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showNetError();
                    }

                    @Override
                    public void onNext(SignUploadBean bean) {
                        if (Constants.OK.equals(bean.StateCode)) {
                            finishTask(bean);
                        } else {
                            ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? Constants.ERROR : bean.ResponseMessage);
                        }
                    }
                });
    }

    @Override
    public void finishTask(BaseBean bean) {
        if (bean instanceof SignUploadBean) {
            setSignUploadBeanData((SignUploadBean) bean);
        } else if (bean instanceof UploadFileBean) {
            setUploadFileBeanData((UploadFileBean) bean);
        }
    }


    private void setSignUploadBeanData(SignUploadBean bean) {
        ToastUtil.showShort(TextUtils.isEmpty(bean.ResponseMessage) ? "提交成功" : bean.ResponseMessage);
//        RxBus.getDefault().post(new RxBusBean(Constants.TYPE_ONE, new BaseBean()));
        finish();
    }


    //已经上传的图片数量
    int uploadNUM = 0;

    //将要上传的图片数量
    int uploadNumber = 0;

    //上传的图片所在位置的集合；
    List<Integer> mListIndex;
    //上传的图片地址的集合
    List<String> mListPath;

    //判断哪些文件需要上传
    private void judeFileNeedUpload() {
        uploadNumber = 0;
        uploadNUM = 0;

        mListIndex = new ArrayList<>();
        mListPath = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            List<String> list = mDatas.get(i);
            StringBuilder builder = mListStringBuider.get(i);
            builder.delete(0, builder.toString().length());

            for (int j = 0, length = list.size(); j < length; j++) {
                String str = list.get(j);
                if (str.startsWith("/storage")) {
                    uploadNumber++;
//                    uploadFileNet(i, str);
                    mListIndex.add(i);
                    mListPath.add(str);

                } else {
                    builder.append(str).append(";");
                }
            }
        }
        //表示没有新添加的图片，立即更新信息
        if (uploadNumber == 0) {
            judeSubmitInfoNet();
        } else {
            //从第一张图片开始上传
            if (mListIndex.size() > 0 && mListPath.size() > 0) {
                uploadFileNet(mListIndex.get(0), mListPath.get(0));
            }
        }


    }


    private void uploadFileNet(int flag, String path) {
        //构建要上传的文件
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        //构建要上传的文件
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        RequestBody requestFile =RequestBody.create(MediaType.parse("image/jpg"),file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        RetrofitHelper.getBaseApi()
                .uploadFileNet(flag, body)
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
                        uploadNUM++;
                        ToastUtil.showShort("上传图片出错");
//                        ToastUtil.showLong(e.toString());
                        //图片全部上传完成
                        if (uploadNUM == uploadNumber) {
                            judeSubmitInfoNet();
                        } else {
                            if (mListIndex.size() > uploadNUM && mListPath.size() > uploadNUM) {
                                uploadFileNet(mListIndex.get(uploadNUM), mListPath.get(uploadNUM));
                            }
                        }
                    }

                    @Override
                    public void onNext(UploadFileBean bean) {
//                        ToastUtil.showShort("上传成功");
                        uploadNUM++;

                        if (Constants.OK.equals(bean.StateCode)) {
                            finishTask(bean);
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
            StringBuilder builder = mListStringBuider.get(data);
            builder.append(bean.ResponseData.ImgUrl).append(";");
        }

        //图片全部上传完成
        if (uploadNUM == uploadNumber) {
            judeSubmitInfoNet();
        } else {
            if (mListIndex.size() > uploadNUM && mListPath.size() > uploadNUM) {
                uploadFileNet(mListIndex.get(uploadNUM), mListPath.get(uploadNUM));
            }
        }
    }

    //当图片上传完 或者是没有新图片上传时，更新WriteInfoBean 里的信息然后提交
    private void judeSubmitInfoNet() {
       /* WriteInfoBean writeInfoBean = BaseApplication.getInstance().getWriteInfoBean();

        for (int i = 0; i < 11; i++) {
            StringBuilder builder = mListStringBuider.get(i);
            if (i == 0) {
                writeInfoBean.SurveyImg = builder.toString();
            } else if (i == 1) {
                writeInfoBean.HomesImg = builder.toString();
            } else if (i == 2) {
                writeInfoBean.WardImg = builder.toString();
            } else if (i == 3) {
                writeInfoBean.BedsideCardImg = builder.toString();
            } else if (i == 4) {
                writeInfoBean.CaseDataImg = builder.toString();
            } else if (i == 5) {
                writeInfoBean.XCTImg = builder.toString();
            } else if (i == 6) {
                writeInfoBean.CostImg = builder.toString();
            } else if (i == 7) {
                writeInfoBean.WholeBodyImg = builder.toString();
            } else if (i == 8) {
                writeInfoBean.InjuryImg = builder.toString();
            } else if (i == 9) {
                writeInfoBean.IDCardImg = builder.toString();
            } else if (i == 10) {
                writeInfoBean.NoticeLetterImg = builder.toString();
            }
        }
        BaseApplication.getInstance().setWriteInfoBean(writeInfoBean);*/


        submitInfoNet();
    }
}
