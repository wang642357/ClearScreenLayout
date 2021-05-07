package com.wjx.android.clearscreenlayout.app;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.wjx.android.clearscreenlayout.http.OkHttpClientUtil;
import com.wjx.android.clearscreenlayout.http.OkHttpUrlLoader;

import java.io.InputStream;

import androidx.annotation.NonNull;

/**
 * 作者：wangjianxiong 创建时间：2021/5/7
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
            @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(
                OkHttpClientUtil.getOkHttpClient()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
