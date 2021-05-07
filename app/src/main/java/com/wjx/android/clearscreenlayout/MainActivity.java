package com.wjx.android.clearscreenlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 作者：wangjianxiong 创建时间：2021/4/28
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView viewpage2 = findViewById(R.id.viewpage2);
        TextView normal = findViewById(R.id.normal);
        viewpage2.setOnClickListener(this);
        normal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
         if (id == R.id.viewpage2) {
             Intent intent = new Intent(MainActivity.this, ViewPager2Activity.class);
             startActivity(intent);
         } else if (id == R.id.normal) {
             Intent intent = new Intent(MainActivity.this, NormalActivity.class);
             startActivity(intent);
         }
    }
}
