package com.sunland.contactbook.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunland.contactbook.MyApplication;
import com.sunland.contactbook.R;
import com.sunland.contactbook.utils.WindowInfoUtils;

import butterknife.ButterKnife;

public class Ac_base extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView nav_back;
    private LinearLayout container;
    private TextView title;
    public MyApplication mApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_base);
        toolbar = findViewById(R.id.base_toolbar);
        nav_back = findViewById(R.id.nav_back);
        container = findViewById(R.id.container);
        title = findViewById(R.id.toolbar_title);
        mApplication = (MyApplication) getApplication();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (nav_back != null || nav_back.getVisibility() == View.VISIBLE) {
            nav_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        initWindow();

    }

    public void setContentLayout(int layout) {
        getLayoutInflater().inflate(layout, container, true);
        ButterKnife.bind(this);

    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            int actionBarHeight = (int) actionbarSizeTypedArray.getDimension(0, 0);
            actionbarSizeTypedArray.recycle();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, WindowInfoUtils.getStatusBarHeight(this) + actionBarHeight);
            toolbar.setLayoutParams(lp);
        }
    }

    public void setNavVisible(boolean isVisible) {
        if (isVisible) {
            nav_back.setVisibility(View.VISIBLE);
        } else {
            nav_back.setVisibility(View.INVISIBLE);
        }
    }

    public void setToolbarTitle(String title) {
        this.title.setText(title);
    }

    public void hop2Activity(Class<? extends Ac_base> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void hop2Activity(Class<? extends Ac_base> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
}
