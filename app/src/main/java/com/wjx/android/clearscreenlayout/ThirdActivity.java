package com.wjx.android.clearscreenlayout;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/4/27
 */
public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ViewPager2 list = findViewById(R.id.viewpager);
        list.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        list.setAdapter(new MyAdapter2(getList()));
    }

    private List<String> getList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("我是内容$i");
        }
        return list;
    }
}
