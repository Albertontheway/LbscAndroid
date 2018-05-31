package huxibianjie.com.lbscandroid.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huxibianjie.com.lbscandroid.R;
import huxibianjie.com.lbscandroid.WalkingActivity;
import huxibianjie.com.lbscandroid.bean.PostLogin;
import huxibianjie.com.lbscandroid.bean.SharePreference;
import huxibianjie.com.lbscandroid.constant.HttpConstant;
import huxibianjie.com.lbscandroid.constant.OkHttpClientManager;
import huxibianjie.com.lbscandroid.util.AppUtils;
import huxibianjie.com.lbscandroid.util.DensityUtil;
import huxibianjie.com.lbscandroid.util.NetUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AutoLayoutActivity {

    @BindView(R.id.phonenumber)
    EditText mPhonenumber;
    @BindView(R.id.del_phonenumber)
    ImageView mDelPhonenumber;
    @BindView(R.id.phone_code)
    EditText mPhoneCode;
    @BindView(R.id.get_code)
    Button mGetCode;
//    @BindView(R.id.Login_button)
//    Button mLoginButton;
    @BindView(R.id.Register_button)
    Button mRegisterButton;
    @BindView(R.id.top_bar_linear2)
    LinearLayout topBarLinear;
    @BindView(R.id.ll_top2)
    RelativeLayout llTop;
    private  int i=60;
    String phoneNums;
    private OkHttpClient client;
    private Object charSet;
    private boolean netConnected;
    SharePreference sharePreference = null;
    SharedPreferences sp;
    Boolean isLogin = false;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        sp = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        initView();
        initOkHttp();
    }
    private void initOkHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }
//全屏幕适配
    private void initView() {

        topBarLinear.setBackgroundColor(0);

        if (Build.VERSION.SDK_INT > 18) {
            AppUtils.initSystemBar(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llTop.getLayoutParams();
            params.height = DensityUtil.Dp2Px(this, 35);
            llTop.setLayoutParams(params);
            topBarLinear.setPadding(0, AppUtils.getStatusBarHeight(this), 0, 0);
        }

    }

    @OnClick({R.id.phonenumber, R.id.del_phonenumber, R.id.phone_code, R.id.get_code, R.id.Register_button})
    public void onClick1(View v) {

        Intent intent;
        phoneNums = mPhonenumber.getText().toString();
        switch (v.getId()) {
            default:
                break;
            case R.id.phonenumber:
                break;
            case R.id.del_phonenumber:
                break;
            case R.id.phone_code:
                break;
//            case R.id.Login_button:
//                intent = new Intent(this, WalkingActivity.class);
//                startActivity(intent);
//                break;

            case R.id.get_code:
                if (NetUtil.isNetConnected(this)) netConnected = true;
                else netConnected = false;
                if (!netConnected){
                Toast.makeText(this, "当前网络异常", Toast.LENGTH_SHORT).show();
                mGetCode.setClickable(false);
                return;
            }else {
                    mGetCode.setClickable(true);
                }
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                }

        RequestBody requestBodyPostcode = new FormBody.Builder()
                        .add("phone", phoneNums)
                        .build();
                Request requestPostcode = new Request.Builder()
                        .url(HttpConstant.POST_SENTCODE)
                        .post(requestBodyPostcode)
                        .build();

                client.newCall(requestPostcode).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });

                // 2. 把按钮变成不可点击，并且显示倒计时（正在获取）
                mGetCode.setClickable(false);
                mGetCode.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

            case R.id.Register_button:
                if (!netConnected){
                    Toast.makeText(this, "当前网络异常", Toast.LENGTH_SHORT).show();
                    mRegisterButton.setClickable(false);
                    return;
                }else {
                    mRegisterButton.setClickable(true);
                }
                //将收到的验证码和手机号提交再次核对
//                SMSSDK.submitVerificationCode("86", phoneNums, mPhoneCode.getText().toString());
//                createProgressBar();
                //获取输入框
                String code = mPhoneCode.getText().toString();

                //拼接字符串url
                String loading_url =HttpConstant.POST_LOGIN+"?phone="+phoneNums+"&valicode="+code;
                Log.e("onClick1: ",""+loading_url );

                OkHttpClientManager.getAsyn(loading_url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(final String response) {
                        Log.e("aaaaaaaaaa","-----"+response);
                        Gson gson = new Gson();
                        PostLogin postLogin = gson.fromJson(response, PostLogin.class);
                        String errmsg = postLogin.getErrmsg();
                        String SECKEY = postLogin.getContent().getSeckey();
                        String mphone = mPhonenumber.getText().toString();
                        sp.edit().putString("getSeckey",SECKEY).commit();
                        sp.edit().putString("UserNumber",mphone).commit();
                        Log.e("SECKEY-----",sp.getString("getSeckey","") );
                        if (!errmsg.equals("OK")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            sharePreference = new SharePreference(RegisterActivity.this);
                            isLogin = sharePreference.getState();
                            if (isLogin==false) {
                                sharePreference.setState();//将登陆状态设置为true;
                                Intent intent = new Intent(RegisterActivity.this,
                                        WalkingActivity.class);
                                Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        }

                    }
                });
               break;

        }

//获取输入框的状态
        mPhonenumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // 监听如果输入串长度大于0那么就显示clear按钮。
                String s1 = s + "";
                if (s.length() > 0) {
                    mDelPhonenumber.setVisibility(View.VISIBLE);
                } else {
                    mDelPhonenumber.setVisibility(View.INVISIBLE);
                }

            }
        });
//清空输入框内的信息
        mDelPhonenumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 清空输入框
                mPhonenumber.setText("");

            }
        });
    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.login_request_code_btn:
//                // 1. 通过规则判断手机号
//                if (!judgePhoneNums(phoneNums)) {
//                    return;
//                } // 2. 通过sdk发送短信验证
//                SMSSDK.getVerificationCode("86", phoneNums);
//
//                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
//                mGetCode.setClickable(false);
//                mGetCode.setText("重新发送(" + i + ")");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (; i > 0; i--) {
//                            handler.sendEmptyMessage(-9);
//                            if (i <= 0) {
//                                break;
//                            }
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        handler.sendEmptyMessage(-8);
//                    }
//                }).start();
//                break;
//
//            case R.id.login_commit_btn:
//                //将收到的验证码和手机号提交再次核对
//                SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt
//                        .getText().toString());
//                //createProgressBar();
//                break;
//        }
//    }


    /**
     *计时器
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                mGetCode.setText(""+i);
            } else if (msg.what == -8) {
                mGetCode.setText("获取验证码");
                mGetCode.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
            }
        }
    };

    /**
     * progressbar
     */
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 将string类型转换为date类型
     * @param strTime：要转换的string类型的时间，时间格式必须要与formatType的时间格式相同
     * @param formatType：要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
     * @return
     */
    private Date stringToDate(String strTime, String formatType){
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = (Date) formatter.parse(strTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
