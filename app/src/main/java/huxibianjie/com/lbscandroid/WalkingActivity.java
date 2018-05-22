package huxibianjie.com.lbscandroid;

import android.animation.AnimatorSet;
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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huxibianjie.com.lbscandroid.bean.TimeUtils;
import huxibianjie.com.lbscandroid.constant.HttpConstant;
import huxibianjie.com.lbscandroid.constant.OkHttpClientManager;
import huxibianjie.com.lbscandroid.pedomemter.StepService;
import huxibianjie.com.lbscandroid.util.AppUtils;
import huxibianjie.com.lbscandroid.util.Constant;
import huxibianjie.com.lbscandroid.util.DensityUtil;

/**
 * Created by adu on 2016/10/21.
 */

public class WalkingActivity extends AppCompatActivity implements Handler.Callback {

    //@BindView(R.id.rl_Right) RelativeLayout rlRight;
    @BindView(R.id.ll_top)
    RelativeLayout llTop;
    @BindView(R.id.top_bar_linear)
    LinearLayout topBarLinear;
    @BindView(R.id.step_count)
    TextView stepCount;      //计算步数
    @BindView(R.id.calories)
    TextView calories;         //热量千卡



    public static final String WALKCAMPID = "walkCampId";

    @BindView(R.id.Relativelist)
    RelativeLayout mRelativelist;
    @BindView(R.id.share_imageButton)
    ImageView mShareImageButton;
    @BindView(R.id.manry_count)
    TextView mManryCount;
    @BindView(R.id.Calculation_Button)
    Button mCalculationButton;
    private long stepnumber = 0;
    private long lastStep = 0;
    private boolean isPause = false;
    private boolean isStart = false;
    private long timemm = 0;
    private long thisTime = 0;
    private SharedPreferences sp;
    //循环获取当前时刻的步数中间的间隔时间
    private long TIME_INTERVAL = 500;
    public static final int TIMECOM = 30;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);
        ButterKnife.bind(this);
        initView();
        TimeUtils.getNowTime();
    }


    private void initView() {
        sp = getSharedPreferences(Constant.Config.FILE_NAME, MODE_PRIVATE);
        lastStep = sp.getInt(Constant.Config.stepNum, 0);
        delayHandler = new Handler(this);
//        if (sp.getBoolean(Constant.Config.isStepServiceRunning, false)) {
        setupService();
//        }
        topBarLinear.setBackgroundColor(0);

        if (Build.VERSION.SDK_INT > 18) {
            AppUtils.initSystemBar(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTop.getLayoutParams();
            params.height = DensityUtil.Dp2Px(this, 35);
            llTop.setLayoutParams(params);
            topBarLinear.setPadding(0, AppUtils.getStatusBarHeight(this), 0, 0);
        }
        stepCount.setText(String.valueOf(stepnumber));

        calories.setText(String.valueOf(stepToKcal(stepnumber)));
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {

            case Constant.Config.MSG_FROM_SERVER:
                stepnumber = Long.valueOf(String.valueOf(msg.getData().get(Constant.Config.stepNum))) - lastStep / 2;
                int money = 100;
                if (stepnumber != 0) {

                } else {
                    String url = HttpConstant.POST_SEVEDATES + "?day=" + TimeUtils.getNowTime() + "&step=" + stepnumber + "&money=" + money;
                    Log.e("time", "" + TimeUtils.getNowTime());
                    OkHttpClientManager.getAsyn(HttpConstant.POST_SEVEDATES, new OkHttpClientManager.ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.e("aaaaa", "--------" + request);
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.e("bbbbbbbbbbbbbbbb", "--------" + response);
                        }
                    });
                }
                stepCount.setText(String.valueOf(stepnumber));
                calories.setText(String.valueOf(stepToKcal(stepnumber)));
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
    @OnClick({R.id.Relativelist})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;

            case R.id.Relativelist:
                break;
        }
    }
}
