package com.wjx.android.clearscreenlayout.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者：wangjianxiong 创建时间：2021/5/7
 */
public class ResultEntity<T> {

    public List<T> data;

    public int page;

    @SerializedName("page_count")
    public int pageCount;

    public int status;

    @SerializedName("total_counts")
    public int totalCounts;
}
