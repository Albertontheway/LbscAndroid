package huxibianjie.com.lbscandroid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import huxibianjie.com.lbscandroid.R;
import huxibianjie.com.lbscandroid.WalkingActivity;

public class StateActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 开 始 挖 矿
     */
    private ImageView mClickIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        initView();



    }

    private void initView() {
        mClickIntent = (ImageView) findViewById(R.id.click_intent);
        mClickIntent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.click_intent:
                Intent intent = new Intent(this, WalkingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
