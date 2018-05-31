package huxibianjie.com.lbscandroid.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huxibianjie.com.lbscandroid.R;
import huxibianjie.com.lbscandroid.WalkingActivity;
import huxibianjie.com.lbscandroid.util.AppUtils;
import huxibianjie.com.lbscandroid.util.DensityUtil;


public class FenxiangActivity extends AutoLayoutActivity {

    @BindView(R.id.back_up)
    ImageView mBackUp;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    @BindView(R.id.ll_top3)
    RelativeLayout mLlTop3;
    @BindView(R.id.top_bar_linear3)
    LinearLayout mTopBarLinear3;
    @BindView(R.id.QR_code)
    ImageView mQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenxiang);
        ButterKnife.bind(this);
        mTopBarLinear3.setBackgroundColor(0);

        if (Build.VERSION.SDK_INT > 18) {
            AppUtils.initSystemBar(this);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLlTop3.getLayoutParams();
            params.height = DensityUtil.Dp2Px(this, 55);
            mLlTop3.setLayoutParams(params);
            mTopBarLinear3.setPadding(0, AppUtils.getStatusBarHeight(this), 0, 0);
        }
    }

    @OnClick({R.id.back_up, R.id.QR_code})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.back_up:
                startActivity(new Intent(this, WalkingActivity.class));
                break;
            case R.id.QR_code:

                break;
        }
    }
}
