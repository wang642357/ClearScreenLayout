package com.wjx.android.clearscreenlayout;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/4/27
 */
class MyAdapter2 extends BaseQuickAdapter<String, BaseViewHolder> {
    public MyAdapter2(List<String> data) {
        super(R.layout.layout_drawer_content, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, String s) {
        holder.setText(R.id.content, s);
    }
}
