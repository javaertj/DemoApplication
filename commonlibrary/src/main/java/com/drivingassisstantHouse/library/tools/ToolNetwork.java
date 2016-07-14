package com.drivingassisstantHouse.library.tools;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 基于静态内部类实现的单例，保证线程安全的网络信息工具类 <per> 使用该工具类之前，记得在AndroidManifest.xml添加权限许可 <xmp>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * </xmp> </per>
 * 
 * 安卓判断网络状态，只需要在相应的Activity的相关方法（onCreat/onResume）调用一行代码即可
 * NetWorkUtils.getInstance(getActivity()).validateNetWork();
 * 
 * 
 * 
 * @author sunji
 * @version 1.0
 */
public class ToolNetwork {

	public final static String NETWORK_CMNET = "CMNET";
	public final static String NETWORK_CMWAP = "CMWAP";
	public final static String NETWORK_WIFI = "WIFI";
	public final static String TAG = "ToolNetwork";
	private static NetworkInfo networkInfo = null;
	private Context mContext = null;

	private ToolNetwork() {
	}

	public static ToolNetwork getInstance() {
		return SingletonHolder.instance;
	}

	public ToolNetwork init(Context context) {
		this.mContext = context;
		return this;
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @return 是/否
	 */
	public boolean isAvailable() {
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (null == manager) {
			return false;
		}
		networkInfo = manager.getActiveNetworkInfo();
		if (null == networkInfo || !networkInfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断网络是否已连接
	 * 
	 * @return 是/否
	 */
	public boolean isConnected() {
		if (!isAvailable()) {
			return false;
		}
		if (!networkInfo.isConnected()) {
			return false;
		}
		return true;
	}

	/**
	 * 检查当前环境网络是否可用，不可用跳转至开启网络界面,不设置网络强制关闭当前Activity
	 */
	public void validateNetWork() {

		if (!isConnected()) {
			Builder dialogBuilder = new Builder(mContext);
			dialogBuilder.setTitle("网络设置");
			dialogBuilder.setMessage("网络不可用，是否现在设置网络？");
			dialogBuilder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((Activity) mContext)
									.startActivityForResult(new Intent(
											Settings.ACTION_SETTINGS), which);
						}
					});
			dialogBuilder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			dialogBuilder.create();
			dialogBuilder.show();
		}
	}

	/**
	 * 获取网络连接信息</br> 无网络：</br> WIFI网络：WIFI</br> WAP网络：CMWAP</br>
	 * NET网络：CMNET</br>
	 * 
	 * @return
	 */
	public String getNetworkType() {
		if (isConnected()) {
			int type = networkInfo.getType();
			if (ConnectivityManager.TYPE_MOBILE == type) {
				Log.i(TAG,
						"networkInfo.getExtraInfo()-->"
								+ networkInfo.getExtraInfo());
				if (NETWORK_CMWAP.equals(networkInfo.getExtraInfo()
						.toLowerCase())) {
					return NETWORK_CMWAP;
				} else {
					return NETWORK_CMNET;
				}
			} else if (ConnectivityManager.TYPE_WIFI == type) {
				return NETWORK_WIFI;
			}
		}

		return "";
	}

	/************************************提供静态方法处理*****************************************/
	/**
	 * 网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * WIFI是否连接
	 * @param context
	 * @return
	 */
	public static boolean isWIFIConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 移动网络是否打开
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取网络连接类型
	 * ConnectivityManager.TYPE_MOBILE
	 * ConnectivityManager.TYPE_WIFI
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 根据域名解析出IP地址
	 *
	 * @param websit
	 *            网站域名
	 * @return 如果成功则返回网站域名对应的IP地址，其他则返回空串
	 */
	public static String resolveWebsitToIpAddress(String websit) {
		String ipAddress = "";
		try {
			ipAddress = InetAddress.getByName(websit).getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return ipAddress;
	}

	/**
	 * 开启或关闭移动数据网络（4.4及5.0上无法使用,因为该方法被移除了）
	 *
	 * @param context 上下文
	 * @param doOpen true 启用数据网络，false关闭数据网络
	 * @return 操作成功则返回true，其他则返回false
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean setMobileDataEnabled(Context context, boolean doOpen)
	{
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isSuccess = false;
		try
		{
			Class<ConnectivityManager> class1 = (Class<ConnectivityManager>) manager.getClass();
			Class[] argsClads = new Class[] { boolean.class };
			Method method = class1.getMethod("setMobileDataEnabled", argsClads);
			method.invoke(manager, doOpen);
			isSuccess = true;
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}

		return isSuccess;
	}


	private static class SingletonHolder {

		private static ToolNetwork instance = new ToolNetwork();
	}
}
