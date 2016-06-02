package comt.sf.youke.dynamiclayout.Activity.Activity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {
	private static String TAG = NetUtil.class.getCanonicalName();
	
	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						Log.i(TAG, "网络连接正常");
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		Log.i(TAG, "网络连接不正常");
		return false;
	} 
}
