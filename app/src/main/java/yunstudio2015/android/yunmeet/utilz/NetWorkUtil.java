package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {
	/**
	 * check whether the network can be used.
	 * @return
	 */
	public static boolean checkNetwork(Context context) {

		if(isNetworkConnected(context)==true){
			return isWifiConnected(context) == true || isMobileConnected(context) == true;
		}else{
			return false;
		}
	}
	/**
	 * check if the connection is made
	 * 
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
	 * check whether the wifi is connected
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null){
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			//不再建议使用通过ConnetivityManager对象的getNetworkInfo()方法判断
			if (info != null){
				if (info.getType() == ConnectivityManager.TYPE_WIFI){
					return info.isAvailable();
				}
			}
		}
		return false;
	}

	/**
	 * check if the data connection can be used
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null){
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (info != null){
				if (info.getType() == ConnectivityManager.TYPE_MOBILE){
					return info.isAvailable();
				}

			}
		}
		return false;
	}

}
