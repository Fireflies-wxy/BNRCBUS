package com.bnrc.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bnrc.util.collectwifi.BaseActivity;

public class ArActivity extends BaseActivity {

    private ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        picture = (ImageView) findViewById(R.id.picture);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,1);

    }
}
