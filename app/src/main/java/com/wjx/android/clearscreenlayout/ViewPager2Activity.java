package com.wjx.android.clearscreenlayout;

import android.os.Bundle;

import com.wjx.android.clearscreenlayout.adapter.GirlAdapter;
import com.wjx.android.clearscreenlayout.entity.Girl;
import com.wjx.android.clearscreenlayout.entity.ResultEntity;
import com.wjx.android.clearscreenlayout.http.Api;
import com.wjx.android.clearscreenlayout.http.HttpManager;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPager2Activity extends AppCompatActivity {

    private GirlAdapter mGirlAdapter;

    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
        setContentView(R.layout.activity_viewpager2);
        ViewPager2 list = findViewById(R.id.viewpager);
        list.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mGirlAdapter.getData().size() - 1 == position) {
                    getList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        mGirlAdapter = new GirlAdapter();
        list.setAdapter(mGirlAdapter);
        getList();
    }

    private void getList() {
        HttpManager.getInstance().create(Api.class).getGirls(++currentPage, 10).enqueue(
                new Callback<ResultEntity<Girl>>() {
                    @Override
                    public void onResponse(@NotNull Call<ResultEntity<Girl>> call,
                            @NotNull Response<ResultEntity<Girl>> response) {
                        if (response.code() == 200) {
                            ResultEntity<Girl> resultEntity = response.body();
                            if (resultEntity != null) {
                                if (resultEntity.status == 100) {
                                    mGirlAdapter.addData(resultEntity.data);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResultEntity<Girl>> call,
                            @NotNull Throwable t) {

                    }
                });
    }
}