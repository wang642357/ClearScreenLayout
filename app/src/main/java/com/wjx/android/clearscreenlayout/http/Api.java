package com.wjx.android.clearscreenlayout.http;

import com.wjx.android.clearscreenlayout.entity.Girl;
import com.wjx.android.clearscreenlayout.entity.ResultEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 作者：wangjianxiong 创建时间：2021/5/7
 */
public interface Api {

    @GET("data/category/Girl/type/Girl/page/{page}/count/{pageCount}")
    Call<ResultEntity<Girl>> getGirls(@Path("page") int page, @Path("pageCount") int pageCount);
}
