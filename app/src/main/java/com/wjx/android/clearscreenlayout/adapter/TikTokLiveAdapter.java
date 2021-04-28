package com.wjx.android.clearscreenlayout.adapter;

import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.wjx.android.clearscreenlayout.R;
import com.wjx.android.clearscreenlayout.bean.TikTokBean;
import com.wjx.android.clearscreenlayout.control.TikTokView;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * 作者：wangjianxiong 创建时间：2021/4/28
 */
public class TikTokLiveAdapter
        extends BaseQuickAdapter<TikTokBean, TikTokLiveAdapter.TikTokViewHolder> {

    public TikTokLiveAdapter(List<TikTokBean> urls) {
        super(R.layout.layout_tik_tok, urls);
    }

    @Override
    protected void convert(@NonNull TikTokViewHolder holder, TikTokBean item) {
        TikTokView prepareview = holder.getView(R.id.prepareview);
        Glide.with(getContext())
                .load(item.thumbImage)
                .placeholder(android.R.color.white)
                .into(prepareview.thumb);
    }

    public static class TikTokViewHolder extends BaseViewHolder {

        public TikTokViewHolder(View view) {
            super(view);
            view.setTag(this);
        }
    }
}
