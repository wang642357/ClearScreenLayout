package com.wjx.android.clearscreenlayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wjx.android.clearscreen.ClearScreenLayout;
import com.wjx.android.clearscreenlayout.entity.Girl;
import com.wjx.android.clearscreenlayout.entity.ResultEntity;
import com.wjx.android.clearscreenlayout.http.Api;
import com.wjx.android.clearscreenlayout.http.HttpManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NormalActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

    private ImageView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ClearScreenLayout container = findViewById(R.id.clear_screen);
        FrameLayout mask = findViewById(R.id.mask);
        mContent = findViewById(R.id.content);
        container.addDragListener(new ClearScreenLayout.DragListener() {
            @Override
            public void onDragging(@NonNull View dragView, float slideOffset) {
                Log.e(TAG, slideOffset + "");
                mask.setAlpha(slideOffset);
            }

            @Override
            public void onDragToOut(@NonNull View dragView) {
                Log.e(TAG, "onDragToOut");
            }

            @Override
            public void onDragToIn(@NonNull View dragView) {
                Log.e(TAG, "onDragToIn");
            }

            @Override
            public void onDragStateChanged(int newState) {
                Log.e(TAG, "onDragStateChanged:" + newState);
            }
        });
        getList();
    }

    private void getList() {
        HttpManager.getInstance().create(Api.class).getGirls(0, 10).enqueue(
                new Callback<ResultEntity<Girl>>() {
                    @Override
                    public void onResponse(@NotNull Call<ResultEntity<Girl>> call,
                            @NotNull Response<ResultEntity<Girl>> response) {
                        if (response.code() == 200) {
                            ResultEntity<Girl> resultEntity = response.body();
                            if (resultEntity != null) {
                                if (resultEntity.status == 100) {
                                    List<Girl> girls = resultEntity.data;
                                    if (girls != null && girls.size() > 0) {
                                        Girl girl = girls.get(0);
                                        Glide.with(NormalActivity.this).load(girl.url)
                                                .into(mContent);
                                    }
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