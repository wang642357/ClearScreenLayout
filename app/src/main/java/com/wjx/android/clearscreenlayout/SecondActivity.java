package com.wjx.android.clearscreenlayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.wjx.android.clearscreen.ClearScreenLayout;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ClearScreenLayout container = findViewById(R.id.clear_screen);
        container.addDragListener(new ClearScreenLayout.DragListener() {
            @Override
            public void onDragging(@NonNull View dragView, float slideOffset) {
                Log.e(TAG, slideOffset + "");
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
    }

}