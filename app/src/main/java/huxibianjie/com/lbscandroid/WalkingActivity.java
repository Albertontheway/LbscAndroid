package huxibianjie.com.lbscandroid;

import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import huxibianjie.com.lbscandroid.Activity.FenxiangActivity;
import huxibianjie.com.lbscandroid.Activity.RegisterActivity;
import huxibianjie.com.lbscandroid.Adapart.MyAdapter;
import huxibianjie.com.lbscandroid.Adapart.UserNumber;
import huxibianjie.com.lbscandroid.bean.SharePreference;
import huxibianjie.com.lbscandroid.bean.TimeUtils;
import huxibianjie.com.lbscandroid.constant.HttpConstant;
import huxibianjie.com.lbscandroid.constant.OkHttpClientManager;
import huxibianjie.com.lbscandroid.pedomemter.StepService;
import huxibianjie.com.lbscandroid.util.AppUtils;
import huxibianjie.com.lbscandroid.util.Constant;
import huxibianjie.com.lbscandroid.util.DensityUtil;
import huxibianjie.com.lbscandroid.util.Md5Utils;
import huxibianjie.com.lbscandroid.util.NetUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by adu on 2016/10/21.
 */

public class WalkingActivity extends AutoLayoutActivity implements Handler.Callback, View.OnClickListener {

    //热量千卡


    public static final String WALKCAMPID = "walkCampId";


    private long stepnumber = 0;
    private long lastStep = 0;
    private boolean isPause = false;
    private boolean isStart = false;
    private long timemm = 0;
    private long thisTime = 0;
    Dialog dialog;
    //循环获取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL = 500;
    public static final int TIMECOM = 30;
    private OkHttpClient client;
    private SharedPreferences sp;
    private SharedPreferences sp1;
    String SECKEY;
    String Usernumber;
    String time;

    private Messenger messenger;
    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    private Handler delayHandler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isPause && isStart) {
                thisTime = thisTime + TIME_INTERVAL;
                timemm = thisTime / 60000;
                delayHandler.sendEmptyMessageDelayed(TIMECOM, TIME_INTERVAL);
            }

        }
    };
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyAdapter mAdapter;
    List<UserNumber.ContentBeanX.ContentBean> data = new ArrayList<UserNumber.ContentBeanX.ContentBean>();

    SharePreference sharePreference = null;
    Boolean isLogin = false;
    Intent intent;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.Config.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private View view;
    private LinearLayout mLinearLayout;
    private RelativeLayout mLlTop;
    private LinearLayout mTopBarLinear;
    private ImageView mShareImageButton;
    private ImageView mLocationImageButton;
    /**
     * 0
     */
    private TextView mManryCount;
    /**
     * 0
     */
    private TextView mCalories;
    /**
     * 0
     */
    private TextView mStepCount;
    /**
     * 提交
     */
    private Button mCalculationButton;
    private RecyclerView mRv;
    private ImageView mDeleteShow;
    /**
     * 上传位置
     */
    private Button mUpShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
// use this setting to improve performance if you know that changes
// in content do not change the layout size of the RecyclerView
// use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(false);//true 从底部开始填充子视图
        mRecyclerView.setLayoutManager(mLayoutManager);
        sp1 = PreferenceManager.getDefaultSharedPreferences(WalkingActivity.this);
        initData();
        initView();
        TimeUtils.getNowTime();
    }

    private void showdelect() {
        dialog = new Dialog(this, R.style.dialog);
        view = getLayoutInflater().inflate(R.layout.button_dalog, null);
        mDeleteShow = (ImageView) view.findViewById(R.id.delete_show);
        mDeleteShow.setOnClickListener(this);
        mUpShow = (Button) view.findViewById(R.id.up_show);
        mUpShow.setOnClickListener(this);


        dialog.setContentView(view);

        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    private void initData() {
        data.clear();
        OkHttpClientManager.getAsyn(HttpConstant.GET_USER_RANKINGS, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "onResponse: " + response.toString());
                Gson gson = new Gson();
                UserNumber userNumber = gson.fromJson(response, UserNumber.class);
                List<UserNumber.ContentBeanX.ContentBean> content = userNumber.getContent().getContent();

                data.addAll(content);
                Log.e("TAG", "onResponse: " + data.size() + "");
                mAdapter = new MyAdapter(WalkingActivity.this, data);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        //定位
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        String country = amapLocation.getCountry();//国家信息
                        String province = amapLocation.getProvince();//省信息
                        String distract = amapLocation.getDistrict();//城区
                        String street = amapLocation.getStreet();//街道
                        Log.e("定位信息", country + "" + province + "" + distract + "" + street);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
//初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }

//设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
    }


    private void initView() {
        mLlTop = (RelativeLayout) findViewById(R.id.ll_top);
        mTopBarLinear = (LinearLayout) findViewById(R.id.top_bar_linear);
        mShareImageButton = (ImageView) findViewById(R.id.share_imageButton);
        mShareImageButton.setOnClickListener(this);
        mLocationImageButton = (ImageView) findViewById(R.id.Location_imageButton);
        mLocationImageButton.setOnClickListener(this);
        mManryCount = (TextView) findViewById(R.id.manry_count);
        mManryCount.setOnClickListener(this);
        mCalories = (TextView) findViewById(R.id.calories);
        mCalories.setOnClickListener(this);
        mStepCount = (TextView) findViewById(R.id.step_count);
        mStepCount.setOnClickListener(this);
        mCalculationButton = (Button) findViewById(R.id.Calculation_Button);
        mCalculationButton.setOnClickListener(this);
        mRv = (RecyclerView) findViewById(R.id.rv);
        mRv.setOnClickListener(this);
        sp = getSharedPreferences(Constant.Config.FILE_NAME, MODE_PRIVATE);
        lastStep = sp.getInt(Constant.Config.stepNum, 0);
        delayHandler = new Handler(this);
//        if (sp.getBoolean(Constant.Config.isStepServiceRunning, false)) {
        setupService();
//        }
        mTopBarLinear.setBackgroundColor(0);

        if (Build.VERSION.SDK_INT > 18) {
            AppUtils.initSystemBar(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLlTop.getLayoutParams();
            params.height = DensityUtil.Dp2Px(this, 55);
            mLlTop.setLayoutParams(params);
            mTopBarLinear.setPadding(0, AppUtils.getStatusBarHeight(this), 0, 0);
        }
        mStepCount.setText(String.valueOf(stepnumber));
        mCalories.setText(String.valueOf(stepToKcal(stepnumber)));

    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            case Constant.Config.MSG_FROM_SERVER:
                stepnumber = Long.valueOf(String.valueOf(msg.getData().get(Constant.Config.stepNum))) + lastStep / 2;
                int money = 100;
                if (stepnumber != 0) {

                } else {
                    String url = HttpConstant.POST_SEVEDATES + "?day=" + TimeUtils.getNowTime() + "&step=" + stepnumber + "&money=" + money;
                    Log.e("time", "" + TimeUtils.getNowTime());
                    OkHttpClientManager.getAsyn(HttpConstant.POST_SEVEDATES, new OkHttpClientManager.ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                        }

                        @Override
                        public void onResponse(String response) {
                        }
                    });
                }
                mStepCount.setText(String.valueOf(stepnumber));
                mCalories.setText(String.valueOf(stepToKcal(stepnumber)));
                delayHandler.sendEmptyMessageDelayed(Constant.Config.REQUEST_SERVER, TIME_INTERVAL);
                break;
            case Constant.Config.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Constant.Config.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case TIMECOM:
                runnable.run();
                break;
        }
        return false;
    }

    private void startAnimation() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        AnimatorSet animatorSetStart = new AnimatorSet();
        animatorSetStart.setInterpolator(new DecelerateInterpolator());
        animatorSetStart.setDuration(1000);
        animatorSetStart.start();

    }

    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    //步数与热量的交
    private float stepToKcal(float step) {
        float i = 388.5f;
        return Math.round(100 * step * i / 10000.0f) / 100.0f;
    }


    //session 统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private long exitTime = 0;//初始时间变量LONG

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                finish();
//                System.exit(0);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //点击事件，历史记录，排行榜，listview
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            default:
                break;
            case R.id.share_imageButton:
                startActivity(new Intent(this, FenxiangActivity.class));
                break;
            case R.id.Calculation_Button:
                sharePreference = new SharePreference(WalkingActivity.this);
                isLogin = sharePreference.getState();
                boolean netConnected = NetUtil.isNetConnected(this);

                if (!netConnected) {
                    Toast.makeText(this, "当前网络异常", Toast.LENGTH_SHORT).show();
                    mCalculationButton.setClickable(false);
                    return;
                } else {
                    mCalculationButton.setClickable(true);
                }
                if (!isLogin) {
                    intent = new Intent(WalkingActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } else {

                    SECKEY = sp1.getString("getSeckey", "");
                    Usernumber = sp1.getString("UserNumber", "");
                    Date now = new Date();
                    long times = now.getTime() / 1000;
                    time = TimeUtils.getNowTime();
                    //Toast.makeText(this, "提交", Toast.LENGTH_SHORT).show();
                    String md5Value = Md5Utils.md5s(Usernumber + String.valueOf(times) + SECKEY);
                    Log.e("timesaaaaaaaa", "" + Usernumber + String.valueOf(times) + SECKEY);
                    Log.e("timesaaaaaaaa", "" + md5Value);
                    Log.e("timesaaaaaaaa", "" + Md5Utils.md5s("182010646491527651362uS1ibFQyRby2"));
                    RequestBody requestBodyPostcode = new FormBody.Builder()

                            .add("day", time)
                            .add("step", String.valueOf(mStepCount))
                            .add("money", "100")

                            .build();
                    okhttp3.Request requestPostcode = new okhttp3.Request.Builder()
                            .url(HttpConstant.POST_SEVEDATES)
                            .post(requestBodyPostcode)
                            .addHeader("x-access-user", Usernumber)
                            .addHeader("x-access-time", String.valueOf(times))
                            .addHeader("x-access-token", md5Value)
                            .build();
                    client = new OkHttpClient();
                    client.newCall(requestPostcode).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {


                        }
                    });


                }
                break;
            case R.id.Location_imageButton:

                showdelect();

                break;

            case R.id.delete_show:
                Toast.makeText(this, "关闭", Toast.LENGTH_SHORT).show();
                dialog.hide();
                break;
            case R.id.up_show:
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                dialog.hide();
                break;
        }
        switch (v.getId()) {
            default:
                break;
            case R.id.delete_show:
                Toast.makeText(this, "关闭", Toast.LENGTH_SHORT).show();
                dialog.hide();
                break;
            case R.id.up_show:
                Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                dialog.hide();
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        //mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

}
