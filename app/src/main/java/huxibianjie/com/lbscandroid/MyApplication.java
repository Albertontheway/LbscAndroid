package huxibianjie.com.lbscandroid;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/10/21.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
       // MobSDK.init(this,"25aff7de03a90", "4baa12406874cd5883eda4c557ad40fe");
    }

    public static Context getContext(){
        return context;
    }
}
