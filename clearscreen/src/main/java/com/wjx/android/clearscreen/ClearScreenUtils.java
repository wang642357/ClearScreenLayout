package com.wjx.android.clearscreen;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

import androidx.annotation.RestrictTo;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * 作者：wangjianxiong 创建时间：2021/4/30 ClearScreen工具类
 */
@RestrictTo(LIBRARY_GROUP)
public class ClearScreenUtils {

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

}
