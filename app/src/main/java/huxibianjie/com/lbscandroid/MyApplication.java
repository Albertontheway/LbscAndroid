package huxibianjie.com.lbscandroid;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

/**
 * Created by Administrator on 2016/10/21.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MobclickAgent.setDebugMode(true);//开启调试模式（如果不开启debug运行不会上传umeng统计）
        MobclickAgent.openActivityDurationTrack(false);
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5afd4bacf29d9862e00000e2", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                null);
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);

            }

    public static Context getContext(){
        return context;
    }
}
