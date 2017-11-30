package com.bnrc.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bnrc.util.BusTags;
import com.bnrc.util.CameraSurfaceView;
import com.bnrc.util.collectwifi.BaseActivity;

public class ArActivity extends BaseActivity implements View.OnClickListener,BusTags.IAutoFocus{

    private CameraSurfaceView mCameraSurfaceView;
    private BusTags mBusTags;

    private boolean isClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.cameraSurfaceView);
        mBusTags = (BusTags) findViewById(R.id.busTags);
        mBusTags.setIAutoFocus(this);
        mBusTags.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.busTags:
                Toast.makeText(ArActivity.this,"You clicked a tag", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }


    @Override
    public void autoFocus() {
        mCameraSurfaceView.setAutoFocus();
    }
}
