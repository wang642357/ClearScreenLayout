package com.wjx.android.clearscreenlayout.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wjx.android.clearscreen.ClearScreenLayout;
import com.wjx.android.clearscreenlayout.R;
import com.wjx.android.clearscreenlayout.entity.Girl;

import androidx.annotation.NonNull;

/**
 * 作者：wangjianxiong 创建时间：2021/4/27
 */
public class GirlAdapter extends BaseQuickAdapter<Girl, BaseViewHolder> implements LoadMoreModule {

    public GirlAdapter() {
        super(R.layout.item_girl);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Girl girl) {
        ImageView content = holder.getView(R.id.content);
        FrameLayout mask = holder.getView(R.id.mask);
        ClearScreenLayout clearScreenLayout = holder.getView(R.id.clear_screen);
        clearScreenLayout.addDragListener(new ClearScreenLayout.DragListener() {
            @Override
            public void onDragging(@NonNull View dragView, float slideOffset) {
                mask.setAlpha(slideOffset);
            }

            @Override
            public void onDragToOut(@NonNull View dragView) {

            }

            @Override
            public void onDragToIn(@NonNull View dragView) {

            }

            @Override
            public void onDragStateChanged(int newState) {

            }
        });
        Glide.with(getContext())
                .load(girl.url)
                .into(content);
        holder.setText(R.id.author, girl.author)
                .setText(R.id.desc, girl.desc)
                .setText(R.id.create_date, girl.createdAt)
                .setText(R.id.publish_date, girl.publishedAt)
                .setText(R.id.title, girl.title);
    }
}
