package com.maymeng.jinkeyulv.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.maymeng.jinkeyulv.R;
import com.yancy.gallerypick.inter.ImageLoader;
import com.yancy.gallerypick.widget.GalleryImageView;

import java.io.File;


/**
 * Created by leijiaxq
 * Data       2016/12/27 11:55
 * Describe
 */
public class ImageUtil {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH   = "/";

    private static class ImageLoaderHolder {
        private static final ImageUtil INSTANCE = new ImageUtil();

    }

    private ImageUtil() {
    }

    public static final ImageUtil getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }


    //直接加载网络图片
    public void displayImage(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                //                .centerCrop()
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                //                .crossFade()
                .into(imageView);
    }

    //直接加载网络图片
    public void displayImageCrop(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                //                .crossFade()
                //                .fitCenter()
                .into(imageView);
    }

    //直接加载网络图片
    public void displayImageCenter(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                //                .crossFade()
                .fitCenter()
                .into(imageView);
    }

    //加载SD卡图片
    public void displayImage(Context context, File file, ImageView imageView) {
        Glide
                .with(context)
                .load(file)
                //                .centerCrop()
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);

    }

    //加载SD卡图片并设置大小
    public void displayImage(Context context, File file, ImageView imageView, int width, int height) {
        Glide
                .with(context)
                .load(file)
                .override(width, height)
                //                .centerCrop()
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);

    }

    //加载网络图片并设置大小
    public void displayImage(Context context, String url, ImageView imageView, int width, int height) {
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .override(width, height)
                .crossFade()
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);
    }

    //加载drawable图片
    public void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                //                .crossFade()
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);
    }

    //加载drawable图片显示为圆形图片
    public void displayCricleImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);
    }

    //加载网络图片显示为圆形图片
    public void displayCricleImage(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
                .transform(new GlideCircleTransform(context))
                .crossFade()
                //                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);
    }

    //加载SD卡图片显示为圆形图片
    public void displayCricleImage(Context context, File file, ImageView imageView) {
        Glide
                .with(context)
                .load(file)
                //.centerCrop()
                .transform(new GlideCircleTransform(context))
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);

    }


    //加载drawable图片显示为圆角图片
    public void displayRoundImage(Context context, int resId, ImageView imageView,int radius) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade()
                .transform(new GlideRoundTransform(context,radius))
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);
    }

    //加载网络图片显示为圆角图片
    public void displayRoundImage(Context context, String url, ImageView imageView,int radius) {
        Glide
                .with(context)
                .load(url)
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
                .transform(new GlideRoundTransform(context,radius))
                .crossFade()
                //                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);
    }

    //加载SD卡图片显示为圆角图片
    public void displayRoundImage(Context context, File file, ImageView imageView,int radius) {
        Glide
                .with(context)
                .load(file)
                //.centerCrop()
                .transform(new GlideRoundTransform(context,radius))
                .placeholder(R.drawable.default_picture)
                .error(R.drawable.default_picture)
                .into(imageView);

    }


    //将资源ID转为Uri
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }


    public static class GlideCircleTransform extends BitmapTransformation {

        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }



    /**
     * Glide 圆角 Transform
     */
    public static class GlideRoundTransform extends BitmapTransformation {

        private static float radius = 0f;

        /**
         * 构造函数 默认圆角半径 4dp
         *
         * @param context Context
         */
        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        /**
         * 构造函数
         *
         * @param context Context
         * @param dp 圆角半径
         */
        public GlideRoundTransform(Context context, int dp) {
            super(context);
            radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

   /* public static class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
        }

    }*/

    public  static class GlideImageLoader implements ImageLoader {


        @Override
        public void displayImage(Activity activity, Context context, String path, GalleryImageView galleryImageView, int width, int height) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.mipmap.gallery_pick_photo)
                    .centerCrop()
                    .into(galleryImageView);
        }

        @Override
        public void clearMemoryCache() {

        }
    }
}