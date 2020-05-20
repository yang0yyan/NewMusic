package com.yy.music.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StatusBarUtil {

    @SuppressLint("PrivateApi")
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();

            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(activity.getResources().getColor(color, null));
            window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
    }
}
