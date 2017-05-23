package com.maymeng.jinkeyulv.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 通用工具类
 */
public class CommonUtil {

  /**
   * 检查是否有网络
   */
  public static boolean isNetworkAvailable(Context context) {

    NetworkInfo info = getNetworkInfo(context);
    return info != null && info.isAvailable();
  }


  /**
   * 检查是否是WIFI
   */
  public static boolean isWifi(Context context) {

    NetworkInfo info = getNetworkInfo(context);
    if (info != null) {
      if (info.getType() == ConnectivityManager.TYPE_WIFI) {
        return true;
      }
    }
    return false;
  }


  /**
   * 检查是否是移动网络
   */
  public static boolean isMobile(Context context) {

    NetworkInfo info = getNetworkInfo(context);
    if (info != null) {
      if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
        return true;
      }
    }
    return false;
  }


  private static NetworkInfo getNetworkInfo(Context context) {

    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
        Context.CONNECTIVITY_SERVICE);
    return cm.getActiveNetworkInfo();
  }


  /**
   * 检查SD卡是否存在
   */
  private static boolean checkSdCard() {

    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }


  /**
   * 获取手机SD卡总空间
   */
 /* private static long getSDcardTotalSize() {

    if (checkSdCard()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs mStatFs = new StatFs(path.getPath());
      long blockSizeLong = mStatFs.getBlockSizeLong();
      long blockCountLong = mStatFs.getBlockCountLong();

      return blockSizeLong * blockCountLong;
    } else {
      return 0;
    }
  }
*/

  /**
   * 获取SDka可用空间
   */
 /* private static long getSDcardAvailableSize() {

    if (checkSdCard()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs mStatFs = new StatFs(path.getPath());
      long blockSizeLong = mStatFs.getBlockSizeLong();
      long availableBlocksLong = mStatFs.getAvailableBlocksLong();
      return blockSizeLong * availableBlocksLong;
    } else {
      return 0;
    }
  }*/


  /**
   * 获取手机内部存储总空间
   */
 /* public static long getPhoneTotalSize() {

    if (!checkSdCard()) {
      File path = Environment.getDataDirectory();
      StatFs mStatFs = new StatFs(path.getPath());
      long blockSizeLong = mStatFs.getBlockSizeLong();
      long blockCountLong = mStatFs.getBlockCountLong();
      return blockSizeLong * blockCountLong;
    } else {
      return getSDcardTotalSize();
    }
  }*/


  /**
   * 获取手机内存存储可用空间
   */
  /*public static long getPhoneAvailableSize() {

    if (!checkSdCard()) {
      File path = Environment.getDataDirectory();
      StatFs mStatFs = new StatFs(path.getPath());
      long blockSizeLong = mStatFs.getBlockSizeLong();
      long availableBlocksLong = mStatFs.getAvailableBlocksLong();
      return blockSizeLong * availableBlocksLong;
    } else
      return getSDcardAvailableSize();
  }*/

  /**
   * 获取IP地址
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
   *
   * @param useIPv4 是否用IPv4
   * @return IP地址
   */
  public static String getIPAddress(boolean useIPv4) {
    try {
      for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
        NetworkInterface ni = nis.nextElement();
        // 防止小米手机返回10.0.2.15
        if (!ni.isUp()) continue;
        for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
          InetAddress inetAddress = addresses.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            String hostAddress = inetAddress.getHostAddress();
            boolean isIPv4 = hostAddress.indexOf(':') < 0;
            if (useIPv4) {
              if (isIPv4) return hostAddress;
            } else {
              if (!isIPv4) {
                int index = hostAddress.indexOf('%');
                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
              }
            }
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取域名ip地址
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
   *
   * @param domain 域名
   * @return ip地址
   */
  public static String getDomainAddress(final String domain) {
    try {
      ExecutorService exec = Executors.newCachedThreadPool();
      Future<String> fs = exec.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
          InetAddress inetAddress;
          try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
          } catch (UnknownHostException e) {
            e.printStackTrace();
          }
          return null;
        }
      });
      return fs.get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return null;
  }
}
