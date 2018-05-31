package huxibianjie.com.lbscandroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    public static boolean isNetConnected(Context context) {

        boolean isNetConnected;
        // 获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 判断当前网络是否已经连接
            return info.getState() == NetworkInfo.State.CONNECTED;
        } else {
            isNetConnected = false;
        }
        return isNetConnected;

    }
}