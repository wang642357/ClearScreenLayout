package com.wjx.android.clearscreenlayout;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPager2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager2);
        ViewPager2 list = findViewById(R.id.viewpager);
        list.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        list.setAdapter(new MyAdapter(getList()));
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("我是内容$i");
        }
        return list;
    }
}