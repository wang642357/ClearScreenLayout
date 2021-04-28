package com.wjx.android.clearscreenlayout;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/4/27
 */
class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MyAdapter(List<String> data) {
        super(R.layout.layout_content, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, String s) {
        holder.setText(R.id.content, s);
        View view = holder.getView(R.id.container1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "fdfssdf", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
